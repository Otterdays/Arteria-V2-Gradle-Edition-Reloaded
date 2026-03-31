package com.arteria.game.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.arteria.game.core.data.MiningData
import com.arteria.game.core.engine.TickEngine
import com.arteria.game.core.model.GameState
import com.arteria.game.core.model.LevelUp
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.model.SkillState
import com.arteria.game.core.model.TickResult
import com.arteria.game.core.skill.SkillId
import com.arteria.game.data.game.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(
    private val profileId: String,
    private val repository: GameRepository,
    /** If false, skips the periodic tick/save loop (used in unit tests). */
    private val enableTickLoop: Boolean = true,
) : ViewModel() {
    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState.asStateFlow()

    private val _levelUpEvents = MutableSharedFlow<LevelUp>(extraBufferCapacity = 16)
    val levelUpEvents: SharedFlow<LevelUp> = _levelUpEvents.asSharedFlow()

    /** Offline report shown once on first load. */
    private val _offlineReport = MutableStateFlow<TickResult?>(null)
    val offlineReport: StateFlow<TickResult?> = _offlineReport.asStateFlow()

    private var tickJob: Job? = null
    private var lastSaveTime = 0L

    private val actionRegistry: Map<String, SkillAction> = MiningData.actionRegistry

    init {
        viewModelScope.launch {
            val loaded = repository.loadGameState(profileId)

            val now = System.currentTimeMillis()
            val elapsed = now - loaded.lastSaveTimestamp
            if (elapsed > TICK_INTERVAL_MS * 2) {
                val offlineResult = withContext(Dispatchers.Default) {
                    TickEngine.processOffline(
                        state = loaded,
                        elapsedMs = elapsed,
                        tickIntervalMs = TICK_INTERVAL_MS,
                        actionRegistry = actionRegistry,
                    )
                }
                _gameState.value = offlineResult.state
                if (offlineResult.xpGained.isNotEmpty() || offlineResult.resourcesGained.isNotEmpty()) {
                    _offlineReport.value = offlineResult
                }
            } else {
                _gameState.value = loaded
            }

            if (enableTickLoop) {
                startTickLoop()
            }
        }
    }

    fun dismissOfflineReport() {
        _offlineReport.value = null
    }

    fun startTraining(skillId: SkillId, actionId: String) {
        _gameState.update { state ->
            state?.let {
                val updatedSkills = it.skills.toMutableMap()
                updatedSkills[skillId] = (updatedSkills[skillId] ?: SkillState(skillId = skillId)).copy(
                    isTraining = true,
                    currentActionId = actionId,
                    actionProgressMs = 0L,
                )
                it.copy(skills = updatedSkills)
            }
        }
    }

    fun stopTraining(skillId: SkillId) {
        _gameState.update { state ->
            state?.let {
                val updatedSkills = it.skills.toMutableMap()
                val current = updatedSkills[skillId] ?: return@let it
                updatedSkills[skillId] = current.copy(
                    isTraining = false,
                    currentActionId = null,
                    actionProgressMs = 0L,
                )
                it.copy(skills = updatedSkills)
            }
        }
    }

    private fun startTickLoop() {
        tickJob = viewModelScope.launch {
            lastSaveTime = System.currentTimeMillis()
            while (isActive) {
                delay(TICK_INTERVAL_MS)
                val current = _gameState.value ?: continue
                val result = TickEngine.processTick(current, TICK_INTERVAL_MS, actionRegistry)
                _gameState.value = result.state

                for (levelUp in result.levelUps) {
                    _levelUpEvents.tryEmit(levelUp)
                }

                val now = System.currentTimeMillis()
                if (now - lastSaveTime >= SAVE_INTERVAL_MS) {
                    repository.saveGameState(result.state)
                    lastSaveTime = now
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        val state = _gameState.value ?: return
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            repository.saveGameState(state)
        }
    }

    companion object {
        const val TICK_INTERVAL_MS = 600L
        const val SAVE_INTERVAL_MS = 10_000L

        fun factory(profileId: String, repository: GameRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
                        return GameViewModel(profileId, repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            }
    }
}

