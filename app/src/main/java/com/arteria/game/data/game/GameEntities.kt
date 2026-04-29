package com.arteria.game.data.game

import androidx.room.Entity

@Entity(tableName = "skill_states", primaryKeys = ["profileId", "skillId"])
data class SkillStateEntity(
    val profileId: String,
    val skillId: String,
    val xp: Double,
    val isTraining: Boolean,
    val currentActionId: String?,
    val actionProgressMs: Long,
)

@Entity(tableName = "bank_items", primaryKeys = ["profileId", "itemId"])
data class BankItemEntity(
    val profileId: String,
    val itemId: String,
    val quantity: Int,
)

@Entity(tableName = "game_meta", primaryKeys = ["profileId"])
data class GameMetaEntity(
    val profileId: String,
    val lastSaveTimestamp: Long,
    /** Set when offline simulation runs on cold start; 0 if never applied. */
    val lastOfflineTickAppliedAt: Long = 0L,
    // Equipped gear slots (null = nothing equipped in that slot)
    val equippedWeapon: String? = null,
    val equippedTool: String? = null,
    val equippedArmor: String? = null,
    val equippedAccessory: String? = null,
    /** Active companion id (null = no companion summoned). */
    val activeCompanionId: String? = null,
    /** Resonance clicker momentum 0–100 (Room REAL). */
    val momentum: Double = 0.0,
    val anchorEnergy: Int = 0,
    val anchorEnergyAccMs: Long = 0L,
    val totalResonancePulses: Long = 0L,
    val totalHeavyPulses: Long = 0L,
    val peakMomentum: Double = 0.0,
    // Active encounter (null enemy id = no combat)
    val combatEnemyId: String? = null,
    val combatLocationId: String? = null,
    val combatEnemyName: String? = null,
    val combatEnemyHpCur: Int = 0,
    val combatEnemyHpMax: Int = 0,
    val combatEnemyAttack: Int = 0,
    val combatEnemyDefence: Int = 0,
    val combatEnemyAccuracy: Int = 0,
    val combatEnemyIntervalMs: Long = 0L,
    val combatEnemyAccMs: Long = 0L,
    val combatPlayerHpCur: Int = 0,
    val combatPlayerHpMax: Int = 0,
    val combatPlayerIntervalMs: Long = 0L,
    val combatPlayerAccMs: Long = 0L,
    val combatPlayerAccuracy: Int = 0,
    val combatPlayerMaxHit: Int = 0,
    val combatPlayerMeleeDef: Int = 0,
    val combatKillCount: Int = 0,
    val combatXpPerKill: Double = 0.0,
    val combatLogText: String? = null,
)

