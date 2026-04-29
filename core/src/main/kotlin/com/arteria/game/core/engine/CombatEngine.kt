package com.arteria.game.core.engine

import com.arteria.game.core.data.EncounterData
import com.arteria.game.core.data.EquipmentRegistry
import com.arteria.game.core.model.ActiveCombat
import com.arteria.game.core.model.CombatLogEntry
import com.arteria.game.core.model.CombatLogType
import com.arteria.game.core.model.GameState
import com.arteria.game.core.model.LevelUp
import com.arteria.game.core.model.SkillState
import com.arteria.game.core.skill.SkillId
import com.arteria.game.core.skill.XPTable
import kotlin.random.Random

data class CombatTickResult(
    val state: GameState,
    val xpGained: Map<SkillId, Double> = emptyMap(),
    val resourcesGained: Map<String, Int> = emptyMap(),
    val levelUps: List<LevelUp> = emptyList(),
)

object CombatEngine {
    const val MAX_COMBAT_LOG: Int = 40
    private const val PLAYER_ATTACK_INTERVAL_MS: Long = 1_800L
    private const val PLAYER_ATTACK_INTERVAL_MIN_MS: Long = 900L

    fun startCombat(state: GameState, locationId: String, enemyId: String): GameState? {
        val loc = EncounterData.location(locationId) ?: return null
        val def = EncounterData.enemy(enemyId) ?: return null
        if (enemyId !in loc.enemyIds) return null

        val atkLv = XPTable.levelForXp(state.skills[SkillId.ATTACK]?.xp ?: 0.0)
        val strLv = XPTable.levelForXp(state.skills[SkillId.STRENGTH]?.xp ?: 0.0)
        val defLv = XPTable.levelForXp(state.skills[SkillId.DEFENCE]?.xp ?: 0.0)
        val hpLv = XPTable.levelForXp(state.skills[SkillId.HITPOINTS]?.xp ?: 0.0)

        val weapon = state.equippedGear.weapon?.let { EquipmentRegistry.getById(it) }
        val armor = state.equippedGear.armor?.let { EquipmentRegistry.getById(it) }
        val accessory = state.equippedGear.accessory?.let { EquipmentRegistry.getById(it) }
        val gearAccuracy =
            (weapon?.combatStats?.accuracy ?: 0) +
                (armor?.combatStats?.accuracy ?: 0) +
                (accessory?.combatStats?.accuracy ?: 0)
        val gearMaxHit =
            (weapon?.combatStats?.maxHit ?: 0) +
                (armor?.combatStats?.maxHit ?: 0) +
                (accessory?.combatStats?.maxHit ?: 0)
        val gearMeleeDef =
            (weapon?.combatStats?.meleeDefence ?: 0) +
                (armor?.combatStats?.meleeDefence ?: 0) +
                (accessory?.combatStats?.meleeDefence ?: 0)
        val gearSpeedBonusMs =
            (weapon?.combatStats?.attackSpeedBonusMs ?: 0L) +
                (armor?.combatStats?.attackSpeedBonusMs ?: 0L) +
                (accessory?.combatStats?.attackSpeedBonusMs ?: 0L)

        val playerAccuracy = 5 + atkLv * 3 + gearAccuracy
        val playerMaxHit = (1 + strLv / 2 + gearMaxHit).coerceAtLeast(1)
        val playerMeleeDef = (defLv + gearMeleeDef).coerceAtLeast(1)
        val playerMaxHp = 10 + hpLv * 3
        val playerAttackIntervalMs =
            (PLAYER_ATTACK_INTERVAL_MS - gearSpeedBonusMs).coerceAtLeast(PLAYER_ATTACK_INTERVAL_MIN_MS)

        val combat = ActiveCombat(
            locationId = locationId,
            enemyId = def.id,
            enemyName = def.name,
            enemyMaxHp = def.maxHp,
            enemyCurrentHp = def.maxHp,
            enemyAttack = def.attack,
            enemyDefence = def.defence,
            enemyAccuracy = def.accuracy,
            enemyAttackIntervalMs = def.attackIntervalMs,
            enemyAttackAccMs = 0L,
            playerMaxHp = playerMaxHp,
            playerCurrentHp = playerMaxHp,
            playerAttackIntervalMs = playerAttackIntervalMs,
            playerAttackAccMs = 0L,
            playerAccuracy = playerAccuracy,
            playerMaxHit = playerMaxHit,
            playerMeleeDefence = playerMeleeDef,
            killCount = 0,
            xpPerKill = def.xpPerKill,
        )

        val skills = stopAllTraining(state.skills)
        val log = listOf(
            CombatLogEntry(CombatLogType.INFO, "You enter ${loc.name}."),
            CombatLogEntry(CombatLogType.INFO, "A ${def.name} attacks!"),
        )
        return state.copy(
            skills = skills,
            activeCombat = combat,
            combatLog = appendLog(state.combatLog, log),
        )
    }

    fun fleeCombat(state: GameState): GameState =
        state.copy(activeCombat = null, combatLog = emptyList())

    fun processCombatTick(state: GameState, deltaMs: Long, random: Random): CombatTickResult {
        val combat = state.activeCombat ?: return CombatTickResult(state)
        val bank = state.bank.toMutableMap()
        val skills = state.skills.toMutableMap()
        val xpGained = mutableMapOf<SkillId, Double>()
        val resGained = mutableMapOf<String, Int>()
        val levelUps = mutableListOf<LevelUp>()
        val newLog = mutableListOf<CombatLogEntry>()

        var c = combat
        var playerAcc = c.playerAttackAccMs + deltaMs
        var enemyAcc = c.enemyAttackAccMs + deltaMs

        while (playerAcc >= c.playerAttackIntervalMs) {
            playerAcc -= c.playerAttackIntervalMs
            val hitChance = hitChancePlayer(c.playerAccuracy, c.enemyDefence)
            if (random.nextDouble() < hitChance) {
                val dmg = random.nextInt(1, c.playerMaxHit + 1)
                val newHp = (c.enemyCurrentHp - dmg).coerceAtLeast(0)
                newLog.add(
                    CombatLogEntry(
                        CombatLogType.PLAYER_HIT,
                        "You hit ${c.enemyName} for $dmg.",
                    ),
                )
                c = c.copy(enemyCurrentHp = newHp, playerAttackAccMs = playerAcc, enemyAttackAccMs = enemyAcc)
                if (newHp <= 0) {
                    val killResult = onKill(state, c, skills, bank, xpGained, resGained, levelUps, newLog, random)
                    return CombatTickResult(
                        state = killResult.first.copy(combatLog = appendLog(state.combatLog, newLog)),
                        xpGained = xpGained.toMap(),
                        resourcesGained = resGained.toMap(),
                        levelUps = levelUps.toList(),
                    )
                }
            } else {
                newLog.add(
                    CombatLogEntry(CombatLogType.PLAYER_MISS, "You swing and miss."),
                )
            }
        }

        while (enemyAcc >= c.enemyAttackIntervalMs) {
            enemyAcc -= c.enemyAttackIntervalMs
            val hitChance = hitChanceEnemy(c.enemyAccuracy, c.playerMeleeDefence)
            if (random.nextDouble() < hitChance) {
                val maxDmg = c.enemyAttack.coerceAtLeast(1)
                val dmg = random.nextInt(1, maxDmg + 1)
                val newHp = (c.playerCurrentHp - dmg).coerceAtLeast(0)
                newLog.add(
                    CombatLogEntry(
                        CombatLogType.ENEMY_HIT,
                        "${c.enemyName} hits you for $dmg.",
                    ),
                )
                c = c.copy(playerCurrentHp = newHp, playerAttackAccMs = playerAcc, enemyAttackAccMs = enemyAcc)
                if (newHp <= 0) {
                    newLog.add(
                        CombatLogEntry(CombatLogType.PLAYER_DEATH, "You fall. The encounter ends."),
                    )
                    val cleared = state.copy(
                        skills = skills,
                        bank = bank,
                        activeCombat = null,
                        combatLog = appendLog(state.combatLog, newLog),
                    )
                    return CombatTickResult(cleared)
                }
            } else {
                newLog.add(
                    CombatLogEntry(CombatLogType.ENEMY_MISS, "${c.enemyName} misses."),
                )
            }
        }

        c = c.copy(playerAttackAccMs = playerAcc, enemyAttackAccMs = enemyAcc)
        val nextState = state.copy(
            skills = skills,
            bank = bank,
            activeCombat = c,
            combatLog = appendLog(state.combatLog, newLog),
        )
        return CombatTickResult(
            state = nextState,
            xpGained = xpGained.toMap(),
            resourcesGained = resGained.toMap(),
            levelUps = levelUps.toList(),
        )
    }

    private fun onKill(
        state: GameState,
        combat: ActiveCombat,
        skills: MutableMap<SkillId, SkillState>,
        bank: MutableMap<String, Int>,
        xpGained: MutableMap<SkillId, Double>,
        resGained: MutableMap<String, Int>,
        levelUps: MutableList<LevelUp>,
        newLog: MutableList<CombatLogEntry>,
        random: Random,
    ): Pair<GameState, ActiveCombat> {
        val def = EncounterData.enemy(combat.enemyId) ?: return state to combat
        newLog.add(CombatLogEntry(CombatLogType.KILL, "${def.name} is defeated."))

        val split = combat.xpPerKill / 4.0
        val meleeSkills = listOf(
            SkillId.ATTACK,
            SkillId.STRENGTH,
            SkillId.DEFENCE,
            SkillId.HITPOINTS,
        )
        for (sid in meleeSkills) {
            val before = skills[sid]?.xp ?: 0.0
            val after = before + split
            val oldLv = XPTable.levelForXp(before)
            val newLv = XPTable.levelForXp(after)
            skills[sid] = (skills[sid] ?: SkillState(skillId = sid)).copy(xp = after)
            xpGained[sid] = (xpGained[sid] ?: 0.0) + split
            if (newLv > oldLv) {
                levelUps.add(LevelUp(sid, oldLv, newLv))
            }
        }

        for (drop in def.drops) {
            if (random.nextDouble() < drop.chance) {
                val qty = random.nextInt(drop.minQty, drop.maxQty + 1)
                bank[drop.itemId] = (bank[drop.itemId] ?: 0) + qty
                resGained[drop.itemId] = (resGained[drop.itemId] ?: 0) + qty
                newLog.add(CombatLogEntry(CombatLogType.LOOT, "Loot: ${drop.itemId} x$qty"))
            }
        }

        val respawned = combat.copy(
            enemyCurrentHp = def.maxHp,
            killCount = combat.killCount + 1,
            playerAttackAccMs = 0L,
            enemyAttackAccMs = 0L,
        )
        val nextState = state.copy(skills = skills, bank = bank, activeCombat = respawned)
        return nextState to respawned
    }

    private fun hitChancePlayer(accuracy: Int, enemyDef: Int): Double =
        (0.5 + (accuracy - enemyDef) * 0.02).coerceIn(0.05, 0.95)

    private fun hitChanceEnemy(enemyAcc: Int, playerDef: Int): Double =
        (0.5 + (enemyAcc - playerDef) * 0.02).coerceIn(0.05, 0.95)

    private fun stopAllTraining(skills: Map<SkillId, SkillState>): Map<SkillId, SkillState> =
        skills.mapValues { (_, s) ->
            if (s.isTraining) {
                s.copy(isTraining = false, currentActionId = null, actionProgressMs = 0L)
            } else {
                s
            }
        }

    private fun appendLog(
        current: List<CombatLogEntry>,
        newEntries: List<CombatLogEntry>,
    ): List<CombatLogEntry> =
        (current + newEntries).takeLast(MAX_COMBAT_LOG)
}
