package com.arteria.game.ui.account

import com.arteria.game.data.profile.ProfileRecord
import com.arteria.game.data.profile.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AccountViewModelTest {
    @Before
    fun setUpMainDispatcher() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun resetMainDispatcher() {
        Dispatchers.resetMain()
    }

    @Test
    fun createProfile_rejectsInvalidName() = runBlocking {
        val repository = FakeProfileRepository()
        val viewModel = AccountViewModel(repository)

        val created = viewModel.createProfile("x")

        assertFalse(created)
        assertNotNull(viewModel.uiState.value.errorMessage)
        assertEquals(0, repository.currentProfilesCount())
    }

    @Test
    fun continueWithSelectedProfile_setsActiveAndReturnsId() = runBlocking {
        val repository = FakeProfileRepository()
        val viewModel = AccountViewModel(repository)
        val created = repository.createProfile("PilotOne").getOrThrow()

        viewModel.selectAccount(created.id)
        val selectedId = viewModel.continueWithSelectedProfile()

        assertEquals(created.id, selectedId)
        assertEquals(created.id, repository.getActiveProfile()?.id)
    }

    @Test
    fun loadsActiveOrFirstSelection_whenProfilesExist() = runBlocking {
        val repository = FakeProfileRepository()
        val first = repository.createProfile("PilotOne").getOrThrow()
        val second = repository.createProfile("PilotTwo").getOrThrow()
        repository.setActiveProfile(second.id, playedAt = 99L)

        val viewModel = AccountViewModel(repository)

        assertTrue(viewModel.uiState.value.accounts.isNotEmpty())
        assertEquals(second.id, viewModel.uiState.value.selectedId ?: first.id)
    }
}

private class FakeProfileRepository : ProfileRepository {
    private val data = MutableStateFlow<List<ProfileRecord>>(emptyList())

    override fun observeProfiles(): Flow<List<ProfileRecord>> = data

    override suspend fun createProfile(displayName: String): Result<ProfileRecord> {
        if (data.value.any { it.displayName.equals(displayName, ignoreCase = true) }) {
            return Result.failure(IllegalArgumentException("That name is already taken."))
        }
        val now = System.currentTimeMillis()
        val record = ProfileRecord(
            id = "id-${data.value.size + 1}",
            displayName = displayName,
            createdAt = now,
            lastPlayedAt = now,
            gameMode = "Standard",
            isActive = false,
        )
        data.value = data.value + record
        return Result.success(record)
    }

    override suspend fun setActiveProfile(profileId: String, playedAt: Long): Result<Unit> {
        if (data.value.none { it.id == profileId }) {
            return Result.failure(IllegalStateException("Profile not found."))
        }
        data.value = data.value.map { profile ->
            profile.copy(
                isActive = profile.id == profileId,
                lastPlayedAt = if (profile.id == profileId) playedAt else profile.lastPlayedAt,
            )
        }
        return Result.success(Unit)
    }

    override suspend fun getProfileById(profileId: String): ProfileRecord? =
        data.value.firstOrNull { it.id == profileId }

    override suspend fun getActiveProfile(): ProfileRecord? =
        data.value.firstOrNull { it.isActive }

    fun currentProfilesCount(): Int = data.value.size
}
