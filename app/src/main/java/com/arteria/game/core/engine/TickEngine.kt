package com.arteria.game.core.engine

import com.arteria.game.core.model.GameState
import com.arteria.game.core.model.LevelUp
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.model.SkillState
import com.arteria.game.core.model.TickResult
import com.arteria.game.core.skill.SkillId
import com.arteria.game.core.skill.XPTable

object TickEngine {
    /** Default wall-clock cap for offline catch-up (one day). */
    const val DEFAULT_MAX_OFFLINE_MS: Long = 24L * 60 * 60 * 1000L

    fun processTick(
        state: GameState,
        deltaMs: Long,
        actionRegistry: Map<String, SkillAction>,
    ): TickResult {
        val updatedSkills = mutableMapOf<SkillId, SkillState>()
        val resourcesGained = mutableMapOf<String, Int>()
        val xpGained = mutableMapOf<SkillId, Double>()
        val levelUps = mutableListOf<LevelUp>()
        // Mutable bank — crafting actions deduct inputs from this during the tick
        val updatedBank = state.bank.toMutableMap()

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

            var progressMs = skill.actionProgressMs + deltaMs
            var xp = skill.xp
            var tickXp = 0.0
            var ranOutOfMaterials = false

            while (progressMs >= action.actionTimeMs) {
                // For crafting actions, verify and consume inputs before completing
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
                xp += action.xpPerAction
                tickXp += action.xpPerAction

                if (action.resourceId != null) {
                    resourcesGained[action.resourceId] =
                        (resourcesGained[action.resourceId] ?: 0) + action.resourceAmount
                }
            }

            if (ranOutOfMaterials) {
                // Stop training — no materials left
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

        // Add gathered/crafted resources to bank
        for ((itemId, qty) in resourcesGained) {
            updatedBank[itemId] = (updatedBank[itemId] ?: 0) + qty
        }

        return TickResult(
            state = state.copy(skills = updatedSkills, bank = updatedBank),
            xpGained = xpGained,
            resourcesGained = resourcesGained,
            levelUps = levelUps,
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
            val result = processTick(current, tickIntervalMs, actionRegistry)
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

