package com.arteria.game.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.arteria.game.data.profile.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AccountUiState(
    val accounts: List<AccountSlot> = emptyList(),
    val selectedId: String? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)

/** Snapshot for in-game settings (profile row + rename). */
data class AccountSessionInfo(
    val displayName: String,
    val lastPlayedAtEpochMs: Long,
    val gameMode: String,
)

class AccountViewModel(
    private val repository: ProfileRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeProfiles().collectLatest { records ->
                val mapped = records.map { record ->
                    AccountSlot(
                        id = record.id,
                        displayName = record.displayName,
                        gameMode = GameMode.fromLabel(record.gameMode),
                        lastPlayedAtEpochMs = record.lastPlayedAt,
                        isActive = record.isActive,
                    )
                }
                _uiState.update { current ->
                    val restored = current.selectedId?.takeIf { id ->
                        mapped.any { it.id == id }
                    }
                    val selected = restored
                        ?: records.firstOrNull { it.isActive }?.id
                        ?: mapped.firstOrNull()?.id
                    current.copy(
                        accounts = mapped,
                        selectedId = selected,
                        isLoading = false,
                    )
                }
            }
        }
    }

    fun selectAccount(id: String) {
        _uiState.update { it.copy(selectedId = id, errorMessage = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    suspend fun createProfile(displayName: String): Boolean {
        val validationError = validateDisplayName(displayName)
        if (validationError != null) {
            _uiState.update { it.copy(errorMessage = validationError) }
            return false
        }

        val result = repository.createProfile(displayName)
        return result.fold(
            onSuccess = { record ->
                _uiState.update {
                    it.copy(
                        selectedId = record.id,
                        errorMessage = null,
                    )
                }
                true
            },
            onFailure = { error ->
                _uiState.update { it.copy(errorMessage = error.message ?: "Unable to create account.") }
                false
            },
        )
    }

    suspend fun continueWithSelectedProfile(): String? {
        val selected = _uiState.value.selectedId
        if (selected == null) {
            _uiState.update { it.copy(errorMessage = "Choose an account before continuing.") }
            return null
        }

        val result = repository.setActiveProfile(
            profileId = selected,
            playedAt = System.currentTimeMillis(),
        )
        return result.fold(
            onSuccess = {
                _uiState.update { it.copy(errorMessage = null) }
                selected
            },
            onFailure = { error ->
                _uiState.update { it.copy(errorMessage = error.message ?: "Unable to start session.") }
                null
            },
        )
    }

    suspend fun resolveDisplayName(profileId: String): String {
        val fromState = _uiState.value.accounts.firstOrNull { it.id == profileId }?.displayName
        if (fromState != null) {
            return fromState
        }
        return repository.getProfileById(profileId)?.displayName ?: "Adventurer"
    }

    suspend fun resolveSession(profileId: String): AccountSessionInfo? {
        val record = repository.getProfileById(profileId) ?: return null
        return AccountSessionInfo(
            displayName = record.displayName,
            lastPlayedAtEpochMs = record.lastPlayedAt,
            gameMode = record.gameMode,
        )
    }

    /**
     * @return null on success, or a user-facing error string.
     */
    suspend fun updateDisplayName(profileId: String, raw: String): String? {
        val validationError = validateDisplayName(raw)
        if (validationError != null) {
            return validationError
        }
        val result = repository.updateDisplayName(profileId, raw)
        return result.fold(
            onSuccess = { null },
            onFailure = { it.message ?: "Could not update name." },
        )
    }

    private fun validateDisplayName(raw: String): String? {
        val value = raw.trim()
        if (value.length < 3) {
            return "Name must be at least 3 characters."
        }
        if (value.length > 20) {
            return "Name must be 20 characters or less."
        }
        val allowed = Regex("^[A-Za-z0-9 _-]+$")
        if (!allowed.matches(value)) {
            return "Use letters, numbers, spaces, _ or - only."
        }
        return null
    }

    companion object {
        fun factory(repository: ProfileRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
                        return AccountViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            }
    }
}
