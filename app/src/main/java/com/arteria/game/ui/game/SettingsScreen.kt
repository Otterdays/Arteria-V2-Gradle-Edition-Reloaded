package com.arteria.game.ui.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arteria.game.BuildConfig
import com.arteria.game.core.engine.TickEngine
import com.arteria.game.data.preferences.ThemePreference
import com.arteria.game.data.preferences.UserPreferences
import com.arteria.game.ui.account.AccountSessionInfo
import com.arteria.game.ui.components.ChangelogScreen
import com.arteria.game.ui.components.CreditsScreen
import com.arteria.game.ui.components.OpenSourceNoticesScreen
import com.arteria.game.ui.theme.ArteriaContentColors
import com.arteria.game.ui.theme.ArteriaPalette
import com.arteria.game.ui.theme.LocalArteriaDarkSpace
import com.arteria.game.ui.theme.LocalUserPreferencesRepository
import com.arteria.game.ui.theme.rememberArteriaSpaceBackgroundBrush
import kotlinx.coroutines.launch

// [TRACE: DOCS/FUTURE UPDATES/master_settings_suggestions_doc.md — settings backlog]

private sealed class SettingsSubScreen {
    data object None : SettingsSubScreen()
    data object Changelog : SettingsSubScreen()
    data object Licenses : SettingsSubScreen()
    data object Credits : SettingsSubScreen()
}

@Composable
fun SettingsScreen(
    accountSession: AccountSessionInfo,
    gameSnapshot: SettingsGameSnapshot?,
    tickIntervalMs: Long,
    saveIntervalMs: Long,
    onBack: () -> Unit,
    onBackToAccounts: () -> Unit,
    onOpenChronicle: () -> Unit,
    onRenameDisplayName: suspend (String) -> String?,
    onRenameSuccess: () -> Unit,
    onResetGameProgress: suspend () -> Result<Unit>,
    onAfterResetProgress: () -> Unit,
    onDeleteProfileEverywhere: suspend () -> Result<Unit>,
    onProfileDeleted: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val prefsRepo = LocalUserPreferencesRepository.current
    val prefs by prefsRepo.userPreferences.collectAsStateWithLifecycle(UserPreferences.DEFAULT)
    var subScreen by remember { mutableStateOf<SettingsSubScreen>(SettingsSubScreen.None) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var showResetConfirm by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showPlannedExpanded by remember { mutableStateOf(false) }
    var dangerError by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val darkSpace = LocalArteriaDarkSpace.current
    val bgBrush = rememberArteriaSpaceBackgroundBrush(darkSpace)

    val backSubOrSelf: () -> Unit = {
        when (subScreen) {
            is SettingsSubScreen.None -> onBack()
            else -> subScreen = SettingsSubScreen.None
        }
    }

    BackHandler(onBack = backSubOrSelf)

    when (subScreen) {
        is SettingsSubScreen.Changelog -> {
            ChangelogScreen(onBack = { subScreen = SettingsSubScreen.None }, modifier = modifier)
            return
        }
        is SettingsSubScreen.Licenses -> {
            OpenSourceNoticesScreen(onBack = { subScreen = SettingsSubScreen.None }, modifier = modifier)
            return
        }
        is SettingsSubScreen.Credits -> {
            CreditsScreen(onBack = { subScreen = SettingsSubScreen.None }, modifier = modifier)
            return
        }
        is SettingsSubScreen.None -> { }
    }

    val tickSeconds = tickIntervalMs / 1000f
    val saveSeconds = saveIntervalMs / 1000f
    val cadenceLine = "Ticks every ${tickSeconds}s while active; saves about every ${saveSeconds}s."
    val offlineHours = TickEngine.DEFAULT_MAX_OFFLINE_MS / 3_600_000L
    val offlineCapLine = "Offline catch-up capped at ~${offlineHours}h simulated time."

    fun updatePrefs(block: (UserPreferences) -> UserPreferences) {
        scope.launch { prefsRepo.update(block) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgBrush)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            IconButton(onClick = backSubOrSelf) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = ArteriaContentColors.secondary(),
                )
            }
            Text("SETTINGS", style = MaterialTheme.typography.labelSmall, color = ArteriaPalette.Gold)
        }

        dangerError?.let { err ->
            Text(err, color = ArteriaPalette.GoldDim, style = MaterialTheme.typography.bodySmall)
        }

        SettingsHero(accountSession = accountSession, gameSnapshot = gameSnapshot)

        SettingsSectionDivider("Account")
        SettingsCard(title = "Identity") {
            Text(
                accountSession.displayName,
                style = MaterialTheme.typography.titleMedium,
                color = ArteriaContentColors.primary(),
            )
            Text(
                "${accountSession.gameMode} · ${formatLastPlayed(accountSession.lastPlayedAtEpochMs)}",
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaContentColors.secondary(),
            )
            SettingsOutlinedAction(label = "Edit display name", onClick = { showRenameDialog = true })
            SettingsOutlinedAction(label = "Switch account", onClick = onBackToAccounts)
        }

        if (gameSnapshot != null) {
            SettingsSectionDivider("Progress")
            SettingsCard(title = "Journey") {
                SettingsNavRow(
                    title = "Chronicle",
                    subtitle = "${gameSnapshot.achievementsUnlocked} of ${gameSnapshot.achievementsTotal} " +
                        "trophies · Total level ${gameSnapshot.totalLevel}",
                    onClick = onOpenChronicle,
                )
                Text(
                    "Bank holds ${gameSnapshot.bankItemTypes} item types " +
                        "(${gameSnapshot.bankTotalStacks} stacks).",
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaContentColors.muted(),
                )
            }
        }

        SettingsSectionDivider("Experience")
        SettingsCard(title = "Look & feel") {
            Text("Theme", style = MaterialTheme.typography.bodyMedium, color = ArteriaContentColors.primary())
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ThemeChip("Dark", prefs.themePreference == ThemePreference.DARK) {
                    updatePrefs { it.copy(themePreference = ThemePreference.DARK) }
                }
                ThemeChip("Follow system", prefs.themePreference == ThemePreference.FOLLOW_SYSTEM) {
                    updatePrefs { it.copy(themePreference = ThemePreference.FOLLOW_SYSTEM) }
                }
            }
            SettingsSwitchRow(
                title = "Reduce motion",
                subtitle = "Less animation in menus and backgrounds",
                checked = prefs.reduceMotion,
                onCheckedChange = { v -> updatePrefs { it.copy(reduceMotion = v) } },
            )
        }

        SettingsCard(title = "Sound & feedback") {
            SettingsSwitchRow(
                title = "Sound",
                subtitle = "Master toggle for game audio",
                checked = prefs.soundEnabled,
                onCheckedChange = { v -> updatePrefs { it.copy(soundEnabled = v) } },
            )
            SettingsSwitchRow(
                title = "Idle soundscapes",
                subtitle = "Ambient drone on the game screen",
                checked = prefs.idleSoundscapesEnabled,
                onCheckedChange = { v -> updatePrefs { it.copy(idleSoundscapesEnabled = v) } },
            )
            TestSoundButton(soundEnabled = prefs.soundEnabled)
            SettingsSwitchRow(
                title = "Haptic feedback",
                subtitle = "Vibration on level-up and pulses",
                checked = prefs.hapticsEnabled,
                onCheckedChange = { v -> updatePrefs { it.copy(hapticsEnabled = v) } },
            )
        }

        SettingsSectionDivider("Simulation")
        SettingsCard(title = "Offline & ticks") {
            SettingsSwitchRow(
                title = "Show offline gains report",
                subtitle = "Summary after time away",
                checked = prefs.showOfflineReport,
                onCheckedChange = { v -> updatePrefs { it.copy(showOfflineReport = v) } },
            )
            Text(offlineCapLine, style = MaterialTheme.typography.bodySmall, color = ArteriaContentColors.muted())
            Text(cadenceLine, style = MaterialTheme.typography.bodySmall, color = ArteriaContentColors.muted())
        }

        SettingsSectionDivider("About")
        SettingsCard(title = "App info") {
            Text(
                "Arteria Gradle Edition V2",
                style = MaterialTheme.typography.bodyLarge,
                color = ArteriaContentColors.primary(),
            )
            Text(
                "Version ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                style = MaterialTheme.typography.bodyMedium,
                color = ArteriaContentColors.secondary(),
            )
            SettingsNavRow(
                title = "What's New",
                subtitle = "Release notes and highlights",
                onClick = { subScreen = SettingsSubScreen.Changelog },
            )
            SettingsNavRow(
                title = "Open source notices",
                subtitle = "Third-party licenses",
                onClick = { subScreen = SettingsSubScreen.Licenses },
            )
            SettingsNavRow(
                title = "Credits",
                subtitle = "Contributors and attribution",
                onClick = { subScreen = SettingsSubScreen.Credits },
            )
        }

        SettingsCollapsibleCard(
            title = "Planned features",
            subtitle = "Notifications, economy, and more",
            expanded = showPlannedExpanded,
            onToggle = { showPlannedExpanded = !showPlannedExpanded },
        ) {
            SettingsSwitchRow(
                title = "Quest & daily reminders",
                subtitle = "Requires notification permission (not wired yet)",
                checked = false,
                enabled = false,
                onCheckedChange = { },
            )
            Text(
                "Lumina, shop, and login bonus — planned for a future economy slice.",
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaContentColors.muted(),
            )
        }

        if (BuildConfig.DEBUG) {
            SettingsSectionDivider("Developer")
            SettingsCard(title = "Debug") {
                SettingsSwitchRow(
                    title = "Remove offline time cap",
                    subtitle = "Simulate very long away times",
                    checked = prefs.debugRemoveOfflineCap,
                    onCheckedChange = { v -> updatePrefs { it.copy(debugRemoveOfflineCap = v) } },
                )
            }
        }

        SettingsSectionDivider("Danger")
        SettingsCard(title = "Danger zone") {
            Text(
                "These actions cannot be undone.",
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaPalette.GoldDim,
            )
            SettingsOutlinedAction(label = "Reset game progress", onClick = { showResetConfirm = true })
            SettingsOutlinedAction(
                label = "Delete this profile",
                onClick = { showDeleteConfirm = true },
                contentColor = ArteriaPalette.GoldDim,
            )
        }

        Spacer(Modifier.height(8.dp))
    }

    if (showRenameDialog) {
        RenameDisplayNameDialog(
            initialName = accountSession.displayName,
            onDismiss = { showRenameDialog = false },
            onRenameDisplayName = onRenameDisplayName,
            onSuccess = {
                showRenameDialog = false
                onRenameSuccess()
            },
        )
    }

    if (showResetConfirm) {
        DangerConfirmDialog(
            title = "Reset progress?",
            body = "All skills, bank items, and training state for this profile will be wiped.",
            confirmLabel = "Reset",
            onDismiss = { showResetConfirm = false },
            onConfirm = {
                scope.launch {
                    onResetGameProgress().fold(
                        onSuccess = {
                            showResetConfirm = false
                            dangerError = null
                            onAfterResetProgress()
                        },
                        onFailure = { dangerError = it.message ?: "Reset failed." },
                    )
                }
            },
        )
    }

    if (showDeleteConfirm) {
        DangerConfirmDialog(
            title = "Delete profile?",
            body = "Removes this account and all saved game data. This cannot be undone.",
            confirmLabel = "Delete",
            onDismiss = { showDeleteConfirm = false },
            onConfirm = {
                scope.launch {
                    onDeleteProfileEverywhere().fold(
                        onSuccess = {
                            showDeleteConfirm = false
                            onProfileDeleted()
                        },
                        onFailure = { dangerError = it.message ?: "Delete failed." },
                    )
                }
            },
        )
    }
}

@Composable
private fun DangerConfirmDialog(
    title: String,
    body: String,
    confirmLabel: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ArteriaContentColors.cardSurface(),
        title = { Text(title, color = ArteriaContentColors.primary()) },
        text = { Text(body, color = ArteriaContentColors.secondary()) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmLabel, color = ArteriaPalette.GoldDim)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = ArteriaContentColors.secondary())
            }
        },
    )
}

@Composable
private fun RenameDisplayNameDialog(
    initialName: String,
    onDismiss: () -> Unit,
    onRenameDisplayName: suspend (String) -> String?,
    onSuccess: () -> Unit,
) {
    var text by remember(initialName) { mutableStateOf(initialName) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ArteriaContentColors.cardSurface(),
        title = { Text("Display name", style = MaterialTheme.typography.titleMedium) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                    error = null
                },
                singleLine = true,
                isError = error != null,
                supportingText = { error?.let { Text(it, color = ArteriaPalette.GoldDim) } },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = ArteriaContentColors.primary(),
                    unfocusedTextColor = ArteriaContentColors.primary(),
                    focusedBorderColor = ArteriaPalette.AccentPrimary,
                    unfocusedBorderColor = ArteriaContentColors.border(),
                    cursorColor = ArteriaPalette.AccentHover,
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    scope.launch {
                        val err = onRenameDisplayName(text)
                        if (err == null) onSuccess() else error = err
                    }
                },
            ) {
                Text("Save", color = ArteriaPalette.AccentPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = ArteriaContentColors.secondary())
            }
        },
    )
}
