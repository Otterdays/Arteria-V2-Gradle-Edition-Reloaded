package com.arteria.game.data.game

import android.content.Context
import androidx.room.Room
import androidx.room.withTransaction
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.room.testing.MigrationTestHelper
import com.arteria.game.core.model.GameState
import com.arteria.game.core.model.SkillState
import com.arteria.game.core.skill.SkillId
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Phase 4 exit: deterministic Room migration + cold start restores persisted game rows.
 */
@RunWith(AndroidJUnit4::class)
class GameDatabaseMigrationTest {

    private lateinit var helper: MigrationTestHelper

    @Before
    fun setup() {
        helper = MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            GameDatabase::class.java,
        )
    }

    @After
    fun tearDown() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        context.deleteDatabase(MIGRATION_DB)
        context.deleteDatabase(ROUNDTRIP_DB)
    }

    @Test
    fun migrate1To2_preservesGameMetaAndAddsColumn() {
        helper.createDatabase(MIGRATION_DB, 1).apply {
            execSQL(CREATE_SKILL_STATES_V1)
            execSQL(CREATE_BANK_ITEMS_V1)
            execSQL(CREATE_GAME_META_V1)
            execSQL(
                "INSERT INTO game_meta (profileId, lastSaveTimestamp) VALUES ('p_mig', 424242L)",
            )
            close()
        }

        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.databaseBuilder(context, GameDatabase::class.java, MIGRATION_DB)
            .addMigrations(GameDatabase.MIGRATION_1_2)
            .allowMainThreadQueries()
            .build()
        try {
            val meta = runBlocking { db.gameDao().getGameMeta("p_mig") }
            assertNotNull(meta)
            assertEquals(424242L, meta!!.lastSaveTimestamp)
            assertEquals(0L, meta.lastOfflineTickAppliedAt)
        } finally {
            db.close()
        }
    }

    @Test
    fun coldStart_saveCloseReopen_restoresGameState() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        context.deleteDatabase(ROUNDTRIP_DB)

        val db1 = Room.databaseBuilder(context, GameDatabase::class.java, ROUNDTRIP_DB)
            .addMigrations(GameDatabase.MIGRATION_1_2)
            .allowMainThreadQueries()
            .build()
        val repo1 = GameRepository(
            dao = db1.gameDao(),
            runInTransaction = { block -> db1.withTransaction { block() } },
        )
        val seed = GameState(
            profileId = "p_rt",
            skills = mapOf(
                SkillId.MINING to SkillState(
                    skillId = SkillId.MINING,
                    xp = 12.5,
                    isTraining = false,
                    currentActionId = null,
                    actionProgressMs = 0L,
                ),
            ),
            bank = mapOf("copper_ore" to 3),
            lastSaveTimestamp = 1_700_000_000_000L,
            lastOfflineTickAppliedAt = 1_700_000_000_001L,
        )
        repo1.saveGameState(seed)
        db1.close()

        val db2 = Room.databaseBuilder(context, GameDatabase::class.java, ROUNDTRIP_DB)
            .addMigrations(GameDatabase.MIGRATION_1_2)
            .allowMainThreadQueries()
            .build()
        val repo2 = GameRepository(
            dao = db2.gameDao(),
            runInTransaction = { block -> db2.withTransaction { block() } },
        )
        try {
            val loaded = repo2.loadGameState("p_rt")
            assertEquals(12.5, loaded.skills.getValue(SkillId.MINING).xp, 0.001)
            assertEquals(3, loaded.bank["copper_ore"])
            assertEquals(1_700_000_000_001L, loaded.lastOfflineTickAppliedAt)
        } finally {
            db2.close()
        }
    }

    private companion object {
        const val MIGRATION_DB = "game-migration-test.db"
        const val ROUNDTRIP_DB = "game-roundtrip-test.db"

        const val CREATE_SKILL_STATES_V1 =
            "CREATE TABLE IF NOT EXISTS `skill_states` (" +
                "`profileId` TEXT NOT NULL, `skillId` TEXT NOT NULL, `xp` REAL NOT NULL, " +
                "`isTraining` INTEGER NOT NULL, `currentActionId` TEXT, " +
                "`actionProgressMs` INTEGER NOT NULL, " +
                "PRIMARY KEY(`profileId`, `skillId`)" +
                ")"

        const val CREATE_BANK_ITEMS_V1 =
            "CREATE TABLE IF NOT EXISTS `bank_items` (" +
                "`profileId` TEXT NOT NULL, `itemId` TEXT NOT NULL, " +
                "`quantity` INTEGER NOT NULL, " +
                "PRIMARY KEY(`profileId`, `itemId`)" +
                ")"

        const val CREATE_GAME_META_V1 =
            "CREATE TABLE IF NOT EXISTS `game_meta` (" +
                "`profileId` TEXT NOT NULL, `lastSaveTimestamp` INTEGER NOT NULL, " +
                "PRIMARY KEY(`profileId`)" +
                ")"
    }
}
