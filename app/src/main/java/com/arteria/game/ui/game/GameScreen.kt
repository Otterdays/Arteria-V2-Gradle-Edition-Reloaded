package com.arteria.game.ui.game

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arteria.game.core.skill.SkillId
import com.arteria.game.data.game.GameRepository
import com.arteria.game.ui.theme.ArteriaPalette

private enum class GameTab(val label: String, val icon: ImageVector) {
    SKILLS("Skills", Icons.Filled.Star),
    BANK("Bank", Icons.Filled.Home),
    COMBAT("Combat", Icons.Filled.Favorite),
    SETTINGS("Settings", Icons.Filled.Settings),
}

@Composable
fun GameScreen(
    profileId: String,
    gameRepository: GameRepository,
    accountDisplayName: String,
    onBackToAccounts: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val gameViewModel: GameViewModel = viewModel(
        factory = GameViewModel.factory(profileId, gameRepository),
    )
    val gameState by gameViewModel.gameState.collectAsStateWithLifecycle()
    val offlineReport by gameViewModel.offlineReport.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) }
    var expandedSkillId by remember { mutableStateOf<SkillId?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        gameViewModel.levelUpEvents.collect { levelUp ->
            snackbarHostState.showSnackbar(
                "${levelUp.skillId.displayName} leveled up! Level ${levelUp.newLevel}",
            )
        }
    }

    val bgBrush = remember {
        Brush.verticalGradient(
            colors = listOf(
                ArteriaPalette.BgDeepSpaceTop,
                ArteriaPalette.BgDeepSpaceMid,
                ArteriaPalette.BgDeepSpaceBottom,
            ),
        )
    }

    val expanded = expandedSkillId
    if (expanded != null && gameState != null) {
        val skillState = gameState!!.skills[expanded]
        if (skillState != null) {
            SkillDetailScreen(
                skillId = expanded,
                skillState = skillState,
                onBack = { expandedSkillId = null },
                onStartTraining = { actionId -> gameViewModel.startTraining(expanded, actionId) },
                onStopTraining = { gameViewModel.stopTraining(expanded) },
            )
            return
        }
    }

    val report = offlineReport
    if (report != null && (report.xpGained.isNotEmpty() || report.resourcesGained.isNotEmpty())) {
        OfflineReportDialog(
            report = report,
            onDismiss = gameViewModel::dismissOfflineReport,
        )
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(bgBrush),
        containerColor = ArteriaPalette.BgDeepSpaceTop,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar(
                containerColor = ArteriaPalette.BgCard,
                contentColor = ArteriaPalette.TextPrimary,
            ) {
                GameTab.entries.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = {
                            Text(
                                tab.label,
                                style = MaterialTheme.typography.labelSmall,
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ArteriaPalette.AccentPrimary,
                            selectedTextColor = ArteriaPalette.AccentPrimary,
                            unselectedIconColor = ArteriaPalette.TextMuted,
                            unselectedTextColor = ArteriaPalette.TextMuted,
                            indicatorColor = ArteriaPalette.BgCardHover,
                        ),
                    )
                }
            }
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
                when (tab) {
                    0 -> SkillsScreen(
                        skills = gameState?.skills ?: emptyMap(),
                        onSkillClick = { expandedSkillId = it },
                    )
                    1 -> BankScreen(bank = gameState?.bank ?: emptyMap())
                    2 -> CombatScreen()
                    3 -> SettingsScreen(
                        accountDisplayName = accountDisplayName,
                        onBackToAccounts = onBackToAccounts,
                    )
                }
            }
        }
    }
}

