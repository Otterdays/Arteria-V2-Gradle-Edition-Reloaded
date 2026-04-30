package com.arteria.game.data.game

import com.arteria.game.core.model.ActiveCombat
import com.arteria.game.core.model.CombatLogEntry
import com.arteria.game.core.model.CombatLogType
import com.arteria.game.core.model.SkillState
import com.arteria.game.core.skill.SkillId
import com.arteria.game.core.model.normalizeEquippedGearFromColumns
import com.arteria.game.core.model.GameState

class GameRepository(
    private val dao: GameDao,
    private val runInTransaction: suspend (suspend () -> Unit) -> Unit = { block -> block() },
) {

    suspend fun loadGameState(profileId: String): GameState {
        val skillEntities = dao.getSkillStates(profileId)
        val bankEntities = dao.getBankItems(profileId)
        val meta = dao.getGameMeta(profileId)

        val loaded = skillEntities.mapNotNull { entity ->
            val skillId = runCatching { SkillId.valueOf(entity.skillId) }.getOrNull()
            skillId?.let {
                it to SkillState(
                    skillId = it,
                    xp = entity.xp,
                    isTraining = entity.isTraining,
                    currentActionId = entity.currentActionId,
                    actionProgressMs = entity.actionProgressMs,
                )
            }
        }.toMap()

        val skills = SkillId.entries.associateWith { skillId ->
            loaded[skillId] ?: SkillState(skillId = skillId)
        }

        val bank = bankEntities.associate { it.itemId to it.quantity }

        val equippedGear = normalizeEquippedGearFromColumns(
            weapon = meta?.equippedWeapon,
            head = meta?.equippedHead,
            armor = meta?.equippedArmor,
            accessory = meta?.equippedAccessory,
            ring = meta?.equippedRing,
            ring2Col = meta?.equippedRing2,
            tool = meta?.equippedTool,
        )

        return GameState(
            profileId = profileId,
            skills = skills,
            bank = bank,
            lastSaveTimestamp = meta?.lastSaveTimestamp ?: System.currentTimeMillis(),
            lastOfflineTickAppliedAt = meta?.lastOfflineTickAppliedAt ?: 0L,
            equippedGear = equippedGear,
            activeCompanionId = meta?.activeCompanionId,
            momentum = meta?.momentum ?: 0.0,
            anchorEnergy = meta?.anchorEnergy ?: 0,
            anchorEnergyAccMs = meta?.anchorEnergyAccMs ?: 0L,
            totalResonancePulses = meta?.totalResonancePulses ?: 0L,
            totalHeavyPulses = meta?.totalHeavyPulses ?: 0L,
            peakMomentum = meta?.peakMomentum ?: 0.0,
            activeCombat = activeCombatFromMeta(meta),
            combatLog = combatLogFromText(meta?.combatLogText),
        )
    }

    suspend fun saveGameState(state: GameState) {
        val skillEntities = state.skills.map { (skillId, skill) ->
            SkillStateEntity(
                profileId = state.profileId,
                skillId = skillId.name,
                xp = skill.xp,
                isTraining = skill.isTraining,
                currentActionId = skill.currentActionId,
                actionProgressMs = skill.actionProgressMs,
            )
        }

        val bankEntities = state.bank
            .filter { (_, qty) -> qty > 0 }
            .map { (itemId, qty) ->
                BankItemEntity(
                    profileId = state.profileId,
                    itemId = itemId,
                    quantity = qty,
                )
            }

        val combatMeta = combatMetaFields(state)

        runInTransaction {
            dao.upsertSkillStates(skillEntities)
            dao.clearBank(state.profileId)
            dao.upsertBankItems(bankEntities)
            dao.upsertGameMeta(
                GameMetaEntity(
                    profileId = state.profileId,
                    lastSaveTimestamp = System.currentTimeMillis(),
                    lastOfflineTickAppliedAt = state.lastOfflineTickAppliedAt,
                    equippedWeapon = state.equippedGear.weapon,
                    equippedHead = state.equippedGear.head,
                    equippedTool = state.equippedGear.tool,
                    equippedArmor = state.equippedGear.armor,
                    equippedAccessory = state.equippedGear.accessory,
                    equippedRing = state.equippedGear.ring,
                    equippedRing2 = state.equippedGear.ring2,
                    activeCompanionId = state.activeCompanionId,
                    momentum = state.momentum,
                    anchorEnergy = state.anchorEnergy,
                    anchorEnergyAccMs = state.anchorEnergyAccMs,
                    totalResonancePulses = state.totalResonancePulses,
                    totalHeavyPulses = state.totalHeavyPulses,
                    peakMomentum = state.peakMomentum,
                    combatEnemyId = combatMeta.combatEnemyId,
                    combatLocationId = combatMeta.combatLocationId,
                    combatEnemyName = combatMeta.combatEnemyName,
                    combatEnemyHpCur = combatMeta.combatEnemyHpCur,
                    combatEnemyHpMax = combatMeta.combatEnemyHpMax,
                    combatEnemyAttack = combatMeta.combatEnemyAttack,
                    combatEnemyDefence = combatMeta.combatEnemyDefence,
                    combatEnemyAccuracy = combatMeta.combatEnemyAccuracy,
                    combatEnemyIntervalMs = combatMeta.combatEnemyIntervalMs,
                    combatEnemyAccMs = combatMeta.combatEnemyAccMs,
                    combatPlayerHpCur = combatMeta.combatPlayerHpCur,
                    combatPlayerHpMax = combatMeta.combatPlayerHpMax,
                    combatPlayerIntervalMs = combatMeta.combatPlayerIntervalMs,
                    combatPlayerAccMs = combatMeta.combatPlayerAccMs,
                    combatPlayerAccuracy = combatMeta.combatPlayerAccuracy,
                    combatPlayerMaxHit = combatMeta.combatPlayerMaxHit,
                    combatPlayerMeleeDef = combatMeta.combatPlayerMeleeDef,
                    combatKillCount = combatMeta.combatKillCount,
                    combatXpPerKill = combatMeta.combatXpPerKill,
                    combatLogText = combatMeta.combatLogText,
                ),
            )
        }
    }

    /** Removes all persisted rows for the profile (skills, bank, meta). */
    suspend fun deleteAllGameDataForProfile(profileId: String) {
        runInTransaction {
            dao.deleteSkillStatesForProfile(profileId)
            dao.clearBank(profileId)
            dao.deleteGameMetaForProfile(profileId)
        }
    }

    /** Wipes save data and writes a fresh default game state for the profile. */
    suspend fun resetProgressForProfile(profileId: String) {
        deleteAllGameDataForProfile(profileId)
        val now = System.currentTimeMillis()
        val freshSkills = SkillId.entries.associateWith { skillId -> SkillState(skillId = skillId) }
        saveGameState(
            GameState(
                profileId = profileId,
                skills = freshSkills,
                bank = emptyMap(),
                lastSaveTimestamp = now,
                lastOfflineTickAppliedAt = 0L,
            ),
        )
    }

    private data class CombatMetaColumns(
        val combatEnemyId: String?,
        val combatLocationId: String?,
        val combatEnemyName: String?,
        val combatEnemyHpCur: Int,
        val combatEnemyHpMax: Int,
        val combatEnemyAttack: Int,
        val combatEnemyDefence: Int,
        val combatEnemyAccuracy: Int,
        val combatEnemyIntervalMs: Long,
        val combatEnemyAccMs: Long,
        val combatPlayerHpCur: Int,
        val combatPlayerHpMax: Int,
        val combatPlayerIntervalMs: Long,
        val combatPlayerAccMs: Long,
        val combatPlayerAccuracy: Int,
        val combatPlayerMaxHit: Int,
        val combatPlayerMeleeDef: Int,
        val combatKillCount: Int,
        val combatXpPerKill: Double,
        val combatLogText: String?,
    )

    private fun combatMetaFields(state: GameState): CombatMetaColumns {
        val ac = state.activeCombat
        if (ac == null) {
            return CombatMetaColumns(
                combatEnemyId = null,
                combatLocationId = null,
                combatEnemyName = null,
                combatEnemyHpCur = 0,
                combatEnemyHpMax = 0,
                combatEnemyAttack = 0,
                combatEnemyDefence = 0,
                combatEnemyAccuracy = 0,
                combatEnemyIntervalMs = 0L,
                combatEnemyAccMs = 0L,
                combatPlayerHpCur = 0,
                combatPlayerHpMax = 0,
                combatPlayerIntervalMs = 0L,
                combatPlayerAccMs = 0L,
                combatPlayerAccuracy = 0,
                combatPlayerMaxHit = 0,
                combatPlayerMeleeDef = 0,
                combatKillCount = 0,
                combatXpPerKill = 0.0,
                combatLogText = null,
            )
        }
        return CombatMetaColumns(
            combatEnemyId = ac.enemyId,
            combatLocationId = ac.locationId,
            combatEnemyName = ac.enemyName,
            combatEnemyHpCur = ac.enemyCurrentHp,
            combatEnemyHpMax = ac.enemyMaxHp,
            combatEnemyAttack = ac.enemyAttack,
            combatEnemyDefence = ac.enemyDefence,
            combatEnemyAccuracy = ac.enemyAccuracy,
            combatEnemyIntervalMs = ac.enemyAttackIntervalMs,
            combatEnemyAccMs = ac.enemyAttackAccMs,
            combatPlayerHpCur = ac.playerCurrentHp,
            combatPlayerHpMax = ac.playerMaxHp,
            combatPlayerIntervalMs = ac.playerAttackIntervalMs,
            combatPlayerAccMs = ac.playerAttackAccMs,
            combatPlayerAccuracy = ac.playerAccuracy,
            combatPlayerMaxHit = ac.playerMaxHit,
            combatPlayerMeleeDef = ac.playerMeleeDefence,
            combatKillCount = ac.killCount,
            combatXpPerKill = ac.xpPerKill,
            combatLogText = encodeCombatLog(state.combatLog),
        )
    }

    private fun activeCombatFromMeta(meta: GameMetaEntity?): ActiveCombat? {
        if (meta == null) return null
        val eid = meta.combatEnemyId ?: return null
        return ActiveCombat(
            locationId = meta.combatLocationId.orEmpty(),
            enemyId = eid,
            enemyName = meta.combatEnemyName ?: eid,
            enemyMaxHp = meta.combatEnemyHpMax,
            enemyCurrentHp = meta.combatEnemyHpCur,
            enemyAttack = meta.combatEnemyAttack,
            enemyDefence = meta.combatEnemyDefence,
            enemyAccuracy = meta.combatEnemyAccuracy,
            enemyAttackIntervalMs = meta.combatEnemyIntervalMs,
            enemyAttackAccMs = meta.combatEnemyAccMs,
            playerMaxHp = meta.combatPlayerHpMax,
            playerCurrentHp = meta.combatPlayerHpCur,
            playerAttackIntervalMs = meta.combatPlayerIntervalMs,
            playerAttackAccMs = meta.combatPlayerAccMs,
            playerAccuracy = meta.combatPlayerAccuracy,
            playerMaxHit = meta.combatPlayerMaxHit,
            playerMeleeDefence = meta.combatPlayerMeleeDef,
            killCount = meta.combatKillCount,
            xpPerKill = meta.combatXpPerKill,
        )
    }

    private fun encodeCombatLog(entries: List<CombatLogEntry>): String =
        entries.joinToString("\n") { entry ->
            "${entry.type.ordinal}|${entry.message.replace('\n', ' ')}"
        }

    private fun combatLogFromText(text: String?): List<CombatLogEntry> {
        if (text.isNullOrBlank()) return emptyList()
        return text.lineSequence().mapNotNull { line ->
            val pipe = line.indexOf('|')
            if (pipe < 0) return@mapNotNull null
            val ord = line.substring(0, pipe).toIntOrNull() ?: return@mapNotNull null
            val msg = line.substring(pipe + 1)
            val type = CombatLogType.entries.getOrNull(ord) ?: return@mapNotNull null
            CombatLogEntry(type, msg)
        }.toList()
    }
}
