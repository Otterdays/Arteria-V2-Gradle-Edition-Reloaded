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
)

