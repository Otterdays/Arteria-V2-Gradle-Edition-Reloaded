package com.arteria.game.ui.game

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arteria.game.data.preferences.UserPreferences
import com.arteria.game.data.preferences.asProvider
import com.arteria.game.ui.theme.ArteriaContentColors
import com.arteria.game.ui.theme.ArteriaPalette
import com.arteria.game.ui.theme.LocalArteriaDarkSpace
import com.arteria.game.ui.theme.LocalUserPreferencesRepository
import com.arteria.game.ui.theme.rememberArteriaSpaceBackgroundBrush
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arteria.game.core.data.SkillDataRegistry
import com.arteria.game.core.skill.SkillId
import com.arteria.game.core.skill.XPTable
import com.arteria.game.data.game.GameRepository
import com.arteria.game.ui.account.AccountSessionInfo
import com.arteria.game.ui.audio.IdleSoundscapePlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    profileId: String,
    gameRepository: GameRepository,
    accountSession: AccountSessionInfo,
    onRefreshAccountSession: suspend () -> Unit,
    onRenameDisplayName: suspend (String) -> String?,
    onResetGameProgress: suspend () -> Result<Unit>,
    onDeleteProfileEverywhere: suspend () -> Result<Unit>,
    onProfileFullyDeleted: () -> Unit,
    onBackToAccounts: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val userPrefsRepository = LocalUserPreferencesRepository.current
    val userPrefs by userPrefsRepository.userPreferences.collectAsStateWithLifecycle(
        initialValue = UserPreferences.DEFAULT,
    )
    val gameViewModel: GameViewModel = viewModel(
        factory = GameViewModel.factory(
            profileId,
            gameRepository,
            userPrefsRepository.asProvider(),
        ),
    )
    val gameState by gameViewModel.gameState.collectAsStateWithLifecycle()
    val offlineReport by gameViewModel.offlineReport.collectAsStateWithLifecycle()
    val activeRandomEvent by gameViewModel.activeRandomEvent.collectAsStateWithLifecycle()
    val achievements by gameViewModel.achievements.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) }
    var expandedSkillId by remember { mutableStateOf<SkillId?>(null) }
    var comingSoonSkillId by remember { mutableStateOf<SkillId?>(null) }
    var showSettings by remember { mutableStateOf(false) }
    var showChronicle by remember { mutableStateOf(false) }
    var showEquipment by remember { mutableStateOf(false) }

    // Track recent level-ups for the Hub screen (1g)
    val recentLevelUps = remember { mutableStateListOf<Pair<SkillId, Int>>() }

    val snackbarHostState = remember { SnackbarHostState() }

    // ── Derived state for the bottom bar ─────────────────────────────────────
    val hasTrainingActive by remember {
        derivedStateOf { gameState?.skills?.values?.any { it.isTraining } == true }
    }
    val activeTrainingProgress by remember {
        derivedStateOf {
            val trainSkill = gameState?.skills?.values?.firstOrNull { it.isTraining }
                ?: return@derivedStateOf 0f
            XPTable.progressToNextLevel(trainSkill.xp)
        }
    }
    val bankHasOpportunity by remember {
        derivedStateOf {
            val bank = gameState?.bank ?: return@derivedStateOf false
            SkillDataRegistry.actionRegistry.values.any { action ->
                action.inputItems.isNotEmpty() &&
                    action.inputItems.all { (id, qty) -> (bank[id] ?: 0) >= qty }
            }
        }
    }

    // Close overlays in reverse priority: settings > equipment > chronicle > dialogs > detail
    BackHandler(enabled = showSettings) { showSettings = false }
    BackHandler(enabled = showEquipment && !showSettings) { showEquipment = false }
    BackHandler(enabled = showChronicle && !showSettings && !showEquipment) {
        showChronicle = false
    }
    BackHandler(enabled = comingSoonSkillId != null && !showSettings) {
        comingSoonSkillId = null
    }
    BackHandler(enabled = expandedSkillId != null && !showSettings) { expandedSkillId = null }

    val haptics = LocalHapticFeedback.current
    LaunchedEffect(gameViewModel) {
        gameViewModel.levelUpEvents.collect { levelUp ->
            recentLevelUps.add(levelUp.skillId to levelUp.newLevel)
            if (userPrefsRepository.userPreferences.first().hapticsEnabled) {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            }
            snackbarHostState.showSnackbar(
                "${levelUp.skillId.displayName} leveled up! Level ${levelUp.newLevel}",
            )
        }
    }

    LaunchedEffect(gameViewModel) {
        gameViewModel.newlyUnlockedAchievements.collect { progress ->
            if (userPrefsRepository.userPreferences.first().hapticsEnabled) {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            }
            val achievement = com.arteria.game.core.data.AchievementRegistry.getById(progress.achievementId)
            if (achievement != null) {
                snackbarHostState.showSnackbar(
                    "Achievement unlocked: ${achievement.title}",
                )
            }
        }
    }

    // Idle soundscapes — start/stop whenever either toggle changes or this screen leaves
    val soundscapesActive = userPrefs.soundEnabled && userPrefs.idleSoundscapesEnabled
    DisposableEffect(soundscapesActive) {
        val player = if (soundscapesActive) IdleSoundscapePlayer().also { it.start() } else null
        onDispose { player?.stop() }
    }

    val darkSpace = LocalArteriaDarkSpace.current
    val bgBrush = rememberArteriaSpaceBackgroundBrush(darkSpace)
    val scaffoldBg = if (darkSpace) ArteriaPalette.BgDeepSpaceTop else ArteriaPalette.LightSpaceTop

    // Chronicle overlay — full-screen, sits above game content
    if (showChronicle) {
        ChronicleScreen(
            achievements = achievements,
            onBack = { showChronicle = false },
            modifier = Modifier
                .fillMaxSize()
                .background(bgBrush),
        )
        return
    }

    // Equipment overlay — full-screen, sits above game content
    if (showEquipment) {
        val currentState = gameState
        val playerLevel = currentState?.skills?.values
            ?.sumOf { com.arteria.game.core.skill.XPTable.levelForXp(it.xp) } ?: 1
        EquipmentScreen(
            equippedGear = currentState?.equippedGear ?: com.arteria.game.core.model.EquippedGear(),
            playerLevel = playerLevel,
            bank = currentState?.bank ?: emptyMap(),
            onEquip = gameViewModel::equip,
            onUnequip = gameViewModel::unequip,
            modifier = Modifier
                .fillMaxSize()
                .background(bgBrush),
        )
        return
    }

    // Settings overlay — full-screen, sits above game content
    if (showSettings) {
        val scope = rememberCoroutineScope()
        SettingsScreen(
            accountSession = accountSession,
            tickIntervalMs = GameViewModel.TICK_INTERVAL_MS,
            saveIntervalMs = GameViewModel.SAVE_INTERVAL_MS,
            onBack = { showSettings = false },
            onBackToAccounts = onBackToAccounts,
            onRenameDisplayName = onRenameDisplayName,
            onRenameSuccess = {
                scope.launch { onRefreshAccountSession() }
            },
            onResetGameProgress = onResetGameProgress,
            onAfterResetProgress = { gameViewModel.reloadAfterReset() },
            onDeleteProfileEverywhere = onDeleteProfileEverywhere,
            onProfileDeleted = {
                showSettings = false
                onProfileFullyDeleted()
            },
            modifier = modifier,
        )
        return
    }

    val report = offlineReport
    if (report != null && (report.xpGained.isNotEmpty() || report.resourcesGained.isNotEmpty())) {
        OfflineReportDialog(
            report = report,
            onDismiss = gameViewModel::dismissOfflineReport,
        )
    }

    val pendingComingSoon = comingSoonSkillId
    if (pendingComingSoon != null) {
        SkillComingSoonDialog(
            skillId = pendingComingSoon,
            onDismiss = { comingSoonSkillId = null },
        )
    }

    // Random event dialog
    val pendingEvent = activeRandomEvent
    if (pendingEvent != null) {
        RandomEventDialog(
            activeEvent = pendingEvent,
            onDismiss = gameViewModel::dismissRandomEvent,
        )
    }

    AnimatedContent(
        targetState = expandedSkillId,
        transitionSpec = {
            if (targetState != null) {
                // Push into detail — slide in from right, old content slides left
                (slideInHorizontally(tween(300, easing = FastOutSlowInEasing)) { it } +
                    fadeIn(tween(200))).togetherWith(
                    slideOutHorizontally(tween(250)) { -it / 4 } + fadeOut(tween(150)),
                )
            } else {
                // Pop back — slide in from left, old content slides right
                (slideInHorizontally(tween(300, easing = FastOutSlowInEasing)) { -it / 4 } +
                    fadeIn(tween(200))).togetherWith(
                    slideOutHorizontally(tween(250)) { it } + fadeOut(tween(150)),
                )
            }
        },
        label = "skill_nav",
    ) { expandedId ->
        val currentGameState = gameState
        val skillState = if (expandedId != null && currentGameState != null) {
            currentGameState.skills[expandedId]
        } else null

        if (expandedId != null && skillState != null && currentGameState != null) {
            SkillDetailScreen(
                skillId = expandedId,
                skillState = skillState,
                bank = currentGameState.bank,
                onBack = { expandedSkillId = null },
                onStartTraining = { actionId -> gameViewModel.startTraining(expandedId, actionId) },
                onStopTraining = { gameViewModel.stopTraining(expandedId) },
            )
        } else {
            Scaffold(
                modifier = modifier
                    .fillMaxSize()
                    .background(bgBrush),
                containerColor = scaffoldBg,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = accountSession.displayName,
                                style = MaterialTheme.typography.titleMedium,
                                color = ArteriaContentColors.primary(),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        actions = {
                            IconButton(onClick = { showEquipment = true }) {
                                Text(
                                    text = "⚔️",
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            }
                            IconButton(onClick = {
                                if (SkillDataRegistry.isSkillImplemented(SkillId.SUMMONING)) {
                                    expandedSkillId = SkillId.SUMMONING
                                } else {
                                    comingSoonSkillId = SkillId.SUMMONING
                                }
                            }) {
                                Text(
                                    text = "🐾",
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            }
                            IconButton(onClick = { showChronicle = true }) {
                                Text(
                                    text = "🏆",
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            }
                            IconButton(onClick = { showSettings = true }) {
                                Icon(
                                    imageVector = Icons.Filled.Settings,
                                    contentDescription = "Settings",
                                    tint = ArteriaContentColors.secondary(),
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            scrolledContainerColor = ArteriaContentColors.cardSurface(),
                        ),
                    )
                },
                bottomBar = {
                    ArteriaBottomBar(
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it },
                        activeTrainingProgress = activeTrainingProgress,
                        hasTrainingActive = hasTrainingActive,
                        bankHasOpportunity = bankHasOpportunity,
                    )
                },
            ) { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(bgBrush)
                        .padding(padding),
                ) {
                    AnimatedContent(
                        targetState = selectedTab,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "tab_content",
                    ) { tab ->
                        val currentState = gameState
                        when (tab) {
                            0 -> {
                                if (currentState != null) {
                                    HubScreen(
                                        gameState = currentState,
                                        offlineReport = offlineReport,
                                        recentLevelUps = recentLevelUps.toList(),
                                        onDismissOffline = gameViewModel::dismissOfflineReport,
                                        onSkillTap = { skillId ->
                                            if (skillId == SkillId.RESONANCE) {
                                                selectedTab = 4
                                            } else if (SkillDataRegistry.isSkillImplemented(skillId)) {
                                                expandedSkillId = skillId
                                            } else {
                                                comingSoonSkillId = skillId
                                            }
                                        },
                                        onNavigateToSkills = { selectedTab = 1 },
                                        onNavigateToBank = { selectedTab = 2 },
                                        onNavigateToResonance = { selectedTab = 4 },
                                    )
                                }
                            }
                            1 -> SkillsScreen(
                                skills = currentState?.skills ?: emptyMap(),
                                onSkillClick = { skillId ->
                                    if (skillId == SkillId.RESONANCE) {
                                        selectedTab = 4
                                    } else if (SkillDataRegistry.isSkillImplemented(skillId)) {
                                        expandedSkillId = skillId
                                    } else {
                                        comingSoonSkillId = skillId
                                    }
                                },
                            )
                            2 -> BankScreen(bank = currentState?.bank ?: emptyMap())
                            3 -> {
                                if (currentState != null) {
                                    CombatScreen(
                                        gameState = currentState,
                                        onStartEncounter = { loc, enemy ->
                                            gameViewModel.startEncounter(loc, enemy)
                                        },
                                        onFlee = gameViewModel::fleeCombat,
                                    )
                                }
                            }
                            4 -> {
                                if (currentState != null) {
                                    ResonanceScreen(
                                        gameState = currentState,
                                        onPulse = gameViewModel::pulseResonance,
                                        onHeavyPulse = gameViewModel::heavyPulseResonance,
                                        hapticsEnabled = userPrefs.hapticsEnabled,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } // else (scaffold)
    } // AnimatedContent (skill_nav)
}
