package com.arteria.game.data.game

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [SkillStateEntity::class, BankItemEntity::class, GameMetaEntity::class],
    version = 6,
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

        /** Resonance clicker columns on `game_meta`. */
        val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE game_meta ADD COLUMN momentum REAL NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN anchorEnergy INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN anchorEnergyAccMs INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN totalResonancePulses INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN totalHeavyPulses INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN peakMomentum REAL NOT NULL DEFAULT 0")
            }
        }

        /** Encounter / active combat columns on `game_meta`. */
        val MIGRATION_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE game_meta ADD COLUMN combatEnemyId TEXT")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN combatLocationId TEXT")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN combatEnemyName TEXT")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN combatEnemyHpCur INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN combatEnemyHpMax INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN combatEnemyAttack INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN combatEnemyDefence INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN combatEnemyAccuracy INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN combatEnemyIntervalMs INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN combatEnemyAccMs INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN combatPlayerHpCur INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN combatPlayerHpMax INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN combatPlayerIntervalMs INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN combatPlayerAccMs INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN combatPlayerAccuracy INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN combatPlayerMaxHit INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN combatPlayerMeleeDef INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN combatKillCount INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN combatXpPerKill REAL NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN combatLogText TEXT")
            }
        }

        /** Expanded equipment: head slot + twin ring pockets. */
        val MIGRATION_5_6: Migration = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE game_meta ADD COLUMN equippedHead TEXT")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN equippedRing TEXT")
                db.execSQL("ALTER TABLE game_meta ADD COLUMN equippedRing2 TEXT")
            }
        }
    }
}

