package com.arteria.game.data.game

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [SkillStateEntity::class, BankItemEntity::class, GameMetaEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class GameDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}

