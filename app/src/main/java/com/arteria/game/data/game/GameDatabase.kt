package com.arteria.game.data.game

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [SkillStateEntity::class, BankItemEntity::class, GameMetaEntity::class],
    version = 3,
    exportSchema = false,
)
abstract class GameDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    companion object {
        /** Adds offline audit column on `game_meta` (Phase 4 persistence hardening). */
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE game_meta ADD COLUMN lastOfflineTickAppliedAt INTEGER NOT NULL DEFAULT 0",
                )
            }
        }

        /** Adds equipment slot columns and activeCompanionId to `game_meta`. */
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE game_meta ADD COLUMN equippedWeapon TEXT")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN equippedTool TEXT")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN equippedArmor TEXT")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN equippedAccessory TEXT")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN activeCompanionId TEXT")
            }
        }
    }
}

