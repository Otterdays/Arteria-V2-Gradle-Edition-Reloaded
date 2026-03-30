package com.arteria.game.data.profile

import androidx.room.Entity
import androidx.room.PrimaryKey

// [TRACE: DOCS/SCRATCHPAD.md — startup profile/session persistence]
@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey val id: String,
    val displayName: String,
    val createdAt: Long,
    val lastPlayedAt: Long,
    val gameMode: String,
    val isActive: Boolean,
)

data class ProfileRecord(
    val id: String,
    val displayName: String,
    val createdAt: Long,
    val lastPlayedAt: Long,
    val gameMode: String,
    val isActive: Boolean,
)

fun ProfileEntity.toRecord(): ProfileRecord = ProfileRecord(
    id = id,
    displayName = displayName,
    createdAt = createdAt,
    lastPlayedAt = lastPlayedAt,
    gameMode = gameMode,
    isActive = isActive,
)
