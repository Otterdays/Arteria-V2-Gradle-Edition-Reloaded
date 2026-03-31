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
)

