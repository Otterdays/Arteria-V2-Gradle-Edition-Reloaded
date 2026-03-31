package com.arteria.game.ui.game

import com.arteria.game.core.skill.SkillId
import com.arteria.game.data.game.BankItemEntity
import com.arteria.game.data.game.GameDao
import com.arteria.game.data.game.GameMetaEntity
import com.arteria.game.data.game.GameRepository
import com.arteria.game.data.game.SkillStateEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest {
    @Before
    fun setUpMainDispatcher() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDownMainDispatcher() {
        Dispatchers.resetMain()
    }

    @Test
    fun startAndStopTraining_updatesSkillFlags() = runTest {
        val dao = FakeGameDao()
        val repository = GameRepository(dao)
        val viewModel = GameViewModel(
            profileId = "p1",
            repository = repository,
            enableTickLoop = false,
        )
        waitUntilLoaded(viewModel)

        viewModel.startTraining(SkillId.MINING, "mine_copper")
        delay(10)

        val afterStart = viewModel.gameState.value
        assertNotNull(afterStart)
        assertTrue(afterStart!!.skills.getValue(SkillId.MINING).isTraining)

        viewModel.stopTraining(SkillId.MINING)
        delay(10)

        val afterStop = viewModel.gameState.value
        assertNotNull(afterStop)
        assertFalse(afterStop!!.skills.getValue(SkillId.MINING).isTraining)
    }

    @Test
    fun oldSaveTimestamp_producesOfflineReport_thenCanDismiss() = runTest {
        val now = System.currentTimeMillis()
        val dao = FakeGameDao(
            skillStates = mutableListOf(
                SkillStateEntity(
                    profileId = "p2",
                    skillId = SkillId.MINING.name,
                    xp = 0.0,
                    isTraining = true,
                    currentActionId = "mine_copper",
                    actionProgressMs = 0L,
                ),
            ),
            metaByProfile = mutableMapOf(
                "p2" to GameMetaEntity(
                    profileId = "p2",
                    lastSaveTimestamp = now - 5_000L,
                ),
            ),
        )
        val repository = GameRepository(dao)

        val viewModel = GameViewModel(
            profileId = "p2",
            repository = repository,
            enableTickLoop = false,
        )
        waitUntilLoaded(viewModel)

        val report = withTimeout(2_000) {
            while (viewModel.offlineReport.value == null) {
                delay(10)
            }
            viewModel.offlineReport.value
        }
        assertNotNull(report)
        assertTrue(report!!.xpGained.isNotEmpty() || report.resourcesGained.isNotEmpty())

        viewModel.dismissOfflineReport()
        assertNull(viewModel.offlineReport.value)
    }

    private suspend fun waitUntilLoaded(viewModel: GameViewModel) {
        withTimeout(2_000) {
            while (viewModel.gameState.value == null) {
                delay(10)
            }
        }
    }
}

private class FakeGameDao(
    val skillStates: MutableList<SkillStateEntity> = mutableListOf(),
    val bankItems: MutableList<BankItemEntity> = mutableListOf(),
    val metaByProfile: MutableMap<String, GameMetaEntity> = mutableMapOf(),
) : GameDao {
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
}

