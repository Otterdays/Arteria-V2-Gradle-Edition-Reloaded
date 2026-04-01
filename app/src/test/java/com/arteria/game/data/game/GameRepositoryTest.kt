package com.arteria.game.data.game

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GameRepositoryTest {
    @Test
    fun loadGameState_returnsAllSkills_withDefaultsForMissingRows() = runTest {
        val dao = FakeGameDao()
        dao.skillStates += SkillStateEntity(
            profileId = "p1",
            skillId = "MINING",
            xp = 42.0,
            isTraining = true,
            currentActionId = "mine_copper",
            actionProgressMs = 1200L,
        )

        val repository = GameRepository(dao)
        val state = repository.loadGameState("p1")

        assertEquals("p1", state.profileId)
        assertEquals(42.0, state.skills.getValue(com.arteria.game.core.skill.SkillId.MINING).xp, 0.001)
        assertTrue(state.skills.isNotEmpty())
    }

    @Test
    fun saveGameState_persistsSkillsBankAndMeta() = runTest {
        val dao = FakeGameDao()
        val repository = GameRepository(dao)
        val seed = repository.loadGameState("p2").copy(
            bank = mapOf("copper_ore" to 5, "tin_ore" to 2),
        )

        repository.saveGameState(seed)

        assertTrue(dao.skillStates.any { it.profileId == "p2" })
        assertEquals(2, dao.bankItems.count { it.profileId == "p2" })
        assertNotNull(dao.metaByProfile["p2"])
    }

    @Test
    fun saveGameState_roundTripsLastOfflineTickAppliedAt() = runTest {
        val dao = FakeGameDao()
        val repository = GameRepository(dao)
        val seed = repository.loadGameState("p3").copy(lastOfflineTickAppliedAt = 999L)
        repository.saveGameState(seed)
        val loaded = repository.loadGameState("p3")
        assertEquals(999L, loaded.lastOfflineTickAppliedAt)
    }
}

private class FakeGameDao : GameDao {
    val skillStates = mutableListOf<SkillStateEntity>()
    val bankItems = mutableListOf<BankItemEntity>()
    val metaByProfile = mutableMapOf<String, GameMetaEntity>()

    override suspend fun getSkillStates(profileId: String): List<SkillStateEntity> =
        skillStates.filter { it.profileId == profileId }

    override suspend fun upsertSkillStates(states: List<SkillStateEntity>) {
        val profileIds = states.map { it.profileId }.toSet()
        skillStates.removeAll { it.profileId in profileIds }
        skillStates.addAll(states)
    }

    override suspend fun getBankItems(profileId: String): List<BankItemEntity> =
        bankItems.filter { it.profileId == profileId }

    override suspend fun upsertBankItems(items: List<BankItemEntity>) {
        bankItems.addAll(items)
    }

    override suspend fun clearBank(profileId: String) {
        bankItems.removeAll { it.profileId == profileId }
    }

    override suspend fun getGameMeta(profileId: String): GameMetaEntity? =
        metaByProfile[profileId]

    override suspend fun upsertGameMeta(meta: GameMetaEntity) {
        metaByProfile[meta.profileId] = meta
    }

    override suspend fun deleteSkillStatesForProfile(profileId: String) {
        skillStates.removeAll { it.profileId == profileId }
    }

    override suspend fun deleteGameMetaForProfile(profileId: String) {
        metaByProfile.remove(profileId)
    }
}

