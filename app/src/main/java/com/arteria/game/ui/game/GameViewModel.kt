package com.arteria.game.ui.game

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.arteria.game.core.data.AchievementRegistry
import com.arteria.game.core.data.SkillDataRegistry
import com.arteria.game.core.engine.TickEngine
import com.arteria.game.core.model.AchievementProgress
import com.arteria.game.core.model.ActiveRandomEvent
import com.arteria.game.core.model.AchievementCondition
import com.arteria.game.core.model.GameState
import com.arteria.game.core.model.LevelUp
import com.arteria.game.core.model.RandomEventRegistry
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.model.SkillState
import com.arteria.game.core.model.TickResult
import com.arteria.game.BuildConfig
import com.arteria.game.core.skill.SkillId
import com.arteria.game.core.skill.XPTable
import com.arteria.game.data.game.GameRepository
import com.arteria.game.data.preferences.UserPreferences
import com.arteria.game.data.preferences.UserPreferencesProvider
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
    private val userPreferencesProvider: UserPreferencesProvider,
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

    /** Active random event currently shown to the player. */
    private val _activeRandomEvent = MutableStateFlow<ActiveRandomEvent?>(null)
    val activeRandomEvent: StateFlow<ActiveRandomEvent?> = _activeRandomEvent.asStateFlow()

    /** Achievement progress — derived from game state on each tick. */
    private val _achievements = MutableStateFlow<List<AchievementProgress>>(emptyList())
    val achievements: StateFlow<List<AchievementProgress>> = _achievements.asStateFlow()

    /** Newly unlocked achievements this session (for notifications). */
    private val _newlyUnlocked = MutableSharedFlow<AchievementProgress>(extraBufferCapacity = 8)
    val newlyUnlockedAchievements: SharedFlow<AchievementProgress> = _newlyUnlocked.asSharedFlow()

    private var tickJob: Job? = null
    private var lastSaveTime = 0L
    private var nowProvider: () -> Long = { System.currentTimeMillis() }
    /** Test-only guard to stop infinite loop under virtual-time tests. */
    private var maxTickIterations: Int? = null
    /** Random event trigger probability per tick (1 in N). */
    private val randomEventChance = 200
    private var lastRandomEventTick = 0

    private val actionRegistry: Map<String, SkillAction> = SkillDataRegistry.actionRegistry

    @VisibleForTesting
    internal fun setNowProviderForTests(provider: () -> Long) {
        nowProvider = provider
    }

    @VisibleForTesting
    internal fun setMaxTickIterationsForTests(value: Int?) {
        maxTickIterations = value
    }

    init {
        viewModelScope.launch {
            val prefs = userPreferencesProvider.current()
            val loaded = repository.loadGameState(profileId)

            val now = nowProvider()
            val elapsed = now - loaded.lastSaveTimestamp
            if (elapsed > TICK_INTERVAL_MS * 2) {
                val maxOffline = offlineCapMs(prefs)
                val offlineResult = withContext(Dispatchers.Default) {
                    TickEngine.processOffline(
                        state = loaded,
                        elapsedMs = elapsed,
                        tickIntervalMs = TICK_INTERVAL_MS,
                        actionRegistry = actionRegistry,
                        maxOfflineMs = maxOffline,
                    )
                }
                val afterOffline = offlineResult.state.copy(lastOfflineTickAppliedAt = now)
                _gameState.value = afterOffline
                repository.saveGameState(afterOffline)
                val showReport = prefs.showOfflineReport
                if (showReport &&
                    (offlineResult.xpGained.isNotEmpty() || offlineResult.resourcesGained.isNotEmpty())
                ) {
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

    private fun offlineCapMs(prefs: UserPreferences): Long =
        if (BuildConfig.DEBUG && prefs.debugRemoveOfflineCap) {
            Long.MAX_VALUE
        } else {
            TickEngine.DEFAULT_MAX_OFFLINE_MS
        }

    fun dismissOfflineReport() {
        _offlineReport.value = null
    }

    /** Reload persisted state after settings “reset progress” (same profile id). */
    fun reloadAfterReset() {
        viewModelScope.launch {
            _offlineReport.value = null
            _gameState.value = repository.loadGameState(profileId)
        }
    }

    fun startTraining(skillId: SkillId, actionId: String) {
        _gameState.update { state ->
            state?.let {
                val updatedSkills = it.skills.toMutableMap()
                // Enforce one-at-a-time: stop every other currently-training skill first
                updatedSkills.keys.toList().forEach { id ->
                    if (id != skillId && updatedSkills[id]?.isTraining == true) {
                        updatedSkills[id] = updatedSkills[id]!!.copy(
                            isTraining = false,
                            currentActionId = null,
                            actionProgressMs = 0L,
                        )
                    }
                }
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
            lastSaveTime = nowProvider()
            var tickIterations = 0
            while (isActive) {
                delay(TICK_INTERVAL_MS)
                val current = _gameState.value ?: continue
                val result = TickEngine.processTick(current, TICK_INTERVAL_MS, actionRegistry)
                _gameState.value = result.state

                for (levelUp in result.levelUps) {
                    _levelUpEvents.tryEmit(levelUp)
                }

                // Check achievements
                val updatedAchievements = checkAchievements(result.state)
                _achievements.value = updatedAchievements

                // Random event trigger
                tickIterations += 1
                if (tickIterations - lastRandomEventTick > 50 &&
                    (0 until randomEventChance).random() == 0 &&
                    _activeRandomEvent.value == null
                ) {
                    val event = RandomEventRegistry.getRandom()
                    _activeRandomEvent.value = ActiveRandomEvent(event)
                    lastRandomEventTick = tickIterations
                }

                val now = nowProvider()
                if (now - lastSaveTime >= SAVE_INTERVAL_MS) {
                    repository.saveGameState(result.state)
                    lastSaveTime = now
                }
                val maxIterations = maxTickIterations
                if (maxIterations != null && tickIterations >= maxIterations) {
                    break
                }
            }
        }
    }

    fun dismissRandomEvent() {
        _activeRandomEvent.value = null
    }

    private fun checkAchievements(state: GameState): List<AchievementProgress> {
        val skills = state.skills
        val bank = state.bank
        val totalLevel = skills.values.sumOf { XPTable.levelForXp(it.xp) }

        return AchievementRegistry.all.map { achievement ->
            val currentProgress = when (val condition = achievement.condition) {
                is AchievementCondition.SkillLevel ->
                    XPTable.levelForXp(skills[condition.skillId]?.xp ?: 0.0)
                is AchievementCondition.TotalLevel -> totalLevel
                is AchievementCondition.ItemCollected -> bank[condition.itemId] ?: 0
                is AchievementCondition.BankItems -> bank.values.sum()
                is AchievementCondition.SkillActions -> 0
            }
            val required = AchievementRegistry.getRequiredProgress(achievement.condition)
            val isUnlocked = currentProgress >= required
            val existing = _achievements.value.find { it.achievementId == achievement.id }

            val wasUnlocked = existing?.isUnlocked == true
            if (isUnlocked && !wasUnlocked) {
                val progress = AchievementProgress(
                    achievementId = achievement.id,
                    isUnlocked = true,
                    currentProgress = currentProgress,
                    requiredProgress = required,
                    unlockedAt = System.currentTimeMillis(),
                )
                viewModelScope.launch {
                    _newlyUnlocked.tryEmit(progress)
                }
                progress
            } else {
                existing?.copy(
                    currentProgress = currentProgress.coerceAtLeast(existing.currentProgress),
                ) ?: AchievementProgress(
                    achievementId = achievement.id,
                    isUnlocked = isUnlocked,
                    currentProgress = currentProgress,
                    requiredProgress = required,
                )
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

        fun factory(
            profileId: String,
            repository: GameRepository,
            userPreferencesProvider: UserPreferencesProvider,
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
                        return GameViewModel(profileId, repository, userPreferencesProvider) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            }
    }
}

