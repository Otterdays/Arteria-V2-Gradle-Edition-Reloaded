package com.arteria.game.core.engine

import com.arteria.game.core.data.CompanionRegistry
import com.arteria.game.core.data.EquipmentRegistry
import com.arteria.game.core.data.ResonanceData
import com.arteria.game.core.model.CompanionBonus
import com.arteria.game.core.model.EquippedGear
import com.arteria.game.core.model.GameState
import com.arteria.game.core.model.LevelUp
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.model.SkillState
import com.arteria.game.core.model.TickResult
import com.arteria.game.core.skill.SkillId
import com.arteria.game.core.skill.XPTable
import kotlin.random.Random

data class TickResonanceOptions(
    val hasteMultiplier: Double = 1.0,
    val enableKineticFeedback: Boolean = false,
    val random: Random = Random.Default,
) {
    companion object {
        val Default = TickResonanceOptions()
    }
}

object TickEngine {
    /** Default wall-clock cap for offline catch-up (one day). */
    const val DEFAULT_MAX_OFFLINE_MS: Long = 24L * 60 * 60 * 1000L

    /**
     * Computes the combined XP multiplier from equipped gear for the given skill.
     * Each equipped item contributes its globalXpMultiplier and any per-skill boost.
     */
    private fun gearXpMultiplier(gear: EquippedGear, skillId: SkillId): Double {
        var multiplier = 1.0
        listOfNotNull(gear.weapon, gear.tool, gear.armor, gear.accessory).forEach { itemId ->
            val equip = EquipmentRegistry.getById(itemId) ?: return@forEach
            multiplier *= equip.globalXpMultiplier
            val boost = equip.skillBoosts[skillId] ?: 0.0
            multiplier *= (1.0 + boost)
        }
        return multiplier
    }

    /**
     * Computes the XP multiplier from the active companion for the given skill.
     * Only XpBoost bonuses affect XP. Others (OfflineEfficiency, BankCapacity, ResourceBoost)
     * are handled elsewhere or reserved for future work.
     */
    private fun companionXpMultiplier(activeCompanionId: String?, skillId: SkillId): Double {
        val companion = activeCompanionId?.let { CompanionRegistry.getById(it) } ?: return 1.0
        return when (val bonus = companion.passiveBonus) {
            is CompanionBonus.XpBoost ->
                if (bonus.skillId == null || bonus.skillId == skillId) bonus.multiplier else 1.0
            else -> 1.0
        }
    }

    fun processTick(
        state: GameState,
        deltaMs: Long,
        actionRegistry: Map<String, SkillAction>,
        resonanceOptions: TickResonanceOptions = TickResonanceOptions.Default,
    ): TickResult {
        val updatedSkills = mutableMapOf<SkillId, SkillState>()
        val resourcesGained = mutableMapOf<String, Int>()
        val xpGained = mutableMapOf<SkillId, Double>()
        val levelUps = mutableListOf<LevelUp>()
        val updatedBank = state.bank.toMutableMap()
        val resonanceLevel = XPTable.levelForXp(state.skills[SkillId.RESONANCE]?.xp ?: 0.0)
        var kineticMomentumDelta = 0.0

        for ((skillId, skill) in state.skills) {
            if (!skill.isTraining || skill.currentActionId == null) {
                updatedSkills[skillId] = skill
                continue
            }

            val action = actionRegistry[skill.currentActionId]
            if (action == null) {
                updatedSkills[skillId] = skill.copy(
                    isTraining = false,
                    currentActionId = null,
                    actionProgressMs = 0L,
                )
                continue
            }

            val currentLevel = XPTable.levelForXp(skill.xp)
            if (currentLevel < action.levelRequired) {
                updatedSkills[skillId] = skill.copy(
                    isTraining = false,
                    currentActionId = null,
                    actionProgressMs = 0L,
                )
                continue
            }

            val xpMultiplier = gearXpMultiplier(state.equippedGear, skillId) *
                companionXpMultiplier(state.activeCompanionId, skillId)

            val adjustedDelta = if (skillId == SkillId.RESONANCE) {
                deltaMs
            } else {
                (deltaMs.toDouble() * resonanceOptions.hasteMultiplier).toLong()
            }

            var progressMs = skill.actionProgressMs + adjustedDelta
            var xp = skill.xp
            var tickXp = 0.0
            var ranOutOfMaterials = false

            while (progressMs >= action.actionTimeMs) {
                if (action.inputItems.isNotEmpty()) {
                    val canAfford = action.inputItems.all { (itemId, required) ->
                        (updatedBank[itemId] ?: 0) >= required
                    }
                    if (!canAfford) {
                        ranOutOfMaterials = true
                        break
                    }
                    for ((itemId, required) in action.inputItems) {
                        updatedBank[itemId] = ((updatedBank[itemId] ?: 0) - required).coerceAtLeast(0)
                    }
                }

                progressMs -= action.actionTimeMs
                val earnedXp = action.xpPerAction * xpMultiplier
                xp += earnedXp
                tickXp += earnedXp

                if (resonanceOptions.enableKineticFeedback &&
                    skillId != SkillId.RESONANCE &&
                    resonanceLevel >= ResonanceData.UNLOCK_KINETIC_FEEDBACK &&
                    resonanceOptions.random.nextDouble() < ResonanceData.KINETIC_FEEDBACK_CHANCE
                ) {
                    kineticMomentumDelta += ResonanceData.KINETIC_MOMENTUM_BONUS
                }

                if (action.resourceId != null) {
                    resourcesGained[action.resourceId] =
                        (resourcesGained[action.resourceId] ?: 0) + action.resourceAmount
                }
            }

            if (ranOutOfMaterials) {
                updatedSkills[skillId] = skill.copy(
                    isTraining = false,
                    currentActionId = null,
                    actionProgressMs = 0L,
                    xp = xp,
                )
                if (tickXp > 0.0) {
                    xpGained[skillId] = (xpGained[skillId] ?: 0.0) + tickXp
                }
                continue
            }

            if (tickXp > 0.0) {
                xpGained[skillId] = (xpGained[skillId] ?: 0.0) + tickXp

                val oldLevel = XPTable.levelForXp(skill.xp)
                val newLevel = XPTable.levelForXp(xp)
                if (newLevel > oldLevel) {
                    levelUps.add(LevelUp(skillId, oldLevel, newLevel))
                }
            }

            updatedSkills[skillId] = skill.copy(xp = xp, actionProgressMs = progressMs)
        }

        for ((itemId, qty) in resourcesGained) {
            updatedBank[itemId] = (updatedBank[itemId] ?: 0) + qty
        }

        return TickResult(
            state = state.copy(skills = updatedSkills, bank = updatedBank),
            xpGained = xpGained,
            resourcesGained = resourcesGained,
            levelUps = levelUps,
            kineticMomentumDelta = kineticMomentumDelta,
        )
    }

    fun processOffline(
        state: GameState,
        elapsedMs: Long,
        tickIntervalMs: Long,
        actionRegistry: Map<String, SkillAction>,
        maxOfflineMs: Long = DEFAULT_MAX_OFFLINE_MS,
    ): TickResult {
        val capped = elapsedMs.coerceAtMost(maxOfflineMs)
        val tickCount = capped / tickIntervalMs
        if (tickCount <= 0) {
            return TickResult(state, emptyMap(), emptyMap(), emptyList())
        }

        val totalXpGained = mutableMapOf<SkillId, Double>()
        val totalResourcesGained = mutableMapOf<String, Int>()
        val allLevelUps = mutableListOf<LevelUp>()
        var current = state

        for (i in 0 until tickCount) {
            val result = processTick(current, tickIntervalMs, actionRegistry, TickResonanceOptions.Default)
            current = result.state
            for ((skill, xp) in result.xpGained) {
                totalXpGained[skill] = (totalXpGained[skill] ?: 0.0) + xp
            }
            for ((item, qty) in result.resourcesGained) {
                totalResourcesGained[item] = (totalResourcesGained[item] ?: 0) + qty
            }
            allLevelUps.addAll(result.levelUps)
        }

        return TickResult(current, totalXpGained, totalResourcesGained, allLevelUps)
    }
}
