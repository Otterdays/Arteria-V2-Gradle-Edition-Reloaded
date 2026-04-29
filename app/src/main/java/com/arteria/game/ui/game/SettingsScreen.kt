package com.arteria.game.ui.game

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
import java.text.DateFormat
import java.util.Date
import kotlinx.coroutines.launch

// [TRACE: master_settings_suggestions_doc.md — settings backlog]

private sealed class SettingsSubScreen {
    data object None : SettingsSubScreen()
    data object Changelog : SettingsSubScreen()
    data object Licenses : SettingsSubScreen()
    data object Credits : SettingsSubScreen()
}

@Composable
fun SettingsScreen(
    accountSession: AccountSessionInfo,
    tickIntervalMs: Long,
    saveIntervalMs: Long,
    onBack: () -> Unit,
    onBackToAccounts: () -> Unit,
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
    val offlineCapLine = "Offline catch-up is capped at about ${offlineHours}h of simulated time."

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
        verticalArrangement = Arrangement.spacedBy(12.dp),
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
            Text(
                text = "SETTINGS",
                style = MaterialTheme.typography.labelSmall,
                color = ArteriaPalette.Gold,
            )
        }

        dangerError?.let { err ->
            Text(err, color = ArteriaPalette.GoldDim, style = MaterialTheme.typography.bodySmall)
        }

        SettingsHero(
            accountSession = accountSession,
            cadenceLine = cadenceLine,
            offlineCapLine = offlineCapLine,
        )

        settingsCard(title = "Profile") {
            Text(
                text = accountSession.displayName,
                style = MaterialTheme.typography.headlineMedium,
                color = ArteriaContentColors.primary(),
            )
            Text(
                text = "Game mode · ${accountSession.gameMode}",
                style = MaterialTheme.typography.bodyMedium,
                color = ArteriaPalette.AccentHover,
            )
            Text(
                text = "Last played · ${formatLastPlayed(accountSession.lastPlayedAtEpochMs)}",
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaContentColors.secondary(),
            )
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { showRenameDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ArteriaPalette.AccentPrimary),
            ) {
                Text("Edit display name")
            }
            OutlinedButton(
                onClick = onBackToAccounts,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ArteriaPalette.AccentPrimary),
            ) {
                Text("Switch account")
            }
        }

        settingsCard(title = "Appearance") {
            Text("Theme", style = MaterialTheme.typography.bodyMedium, color = ArteriaContentColors.primary())
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val darkSel = prefs.themePreference == ThemePreference.DARK
                TextButtonChip(label = "Dark", selected = darkSel) {
                    updatePrefs { it.copy(themePreference = ThemePreference.DARK) }
                }
                TextButtonChip(
                    label = "Follow system",
                    selected = prefs.themePreference == ThemePreference.FOLLOW_SYSTEM,
                ) {
                    updatePrefs { it.copy(themePreference = ThemePreference.FOLLOW_SYSTEM) }
                }
            }
            settingsSwitchRow(
                title = "Reduce motion",
                subtitle = "Less animation in menus and backgrounds",
                checked = prefs.reduceMotion,
                onCheckedChange = { v -> updatePrefs { it.copy(reduceMotion = v) } },
            )
        }

        settingsCard(title = "Feedback") {
            settingsSwitchRow(
                title = "Haptic feedback",
                subtitle = "Vibration on level-up (when supported)",
                checked = prefs.hapticsEnabled,
                onCheckedChange = { v -> updatePrefs { it.copy(hapticsEnabled = v) } },
            )
        }

        settingsCard(title = "Gameplay") {
            settingsSwitchRow(
                title = "Show offline gains report",
                subtitle = "Summary after time away",
                checked = prefs.showOfflineReport,
                onCheckedChange = { v -> updatePrefs { it.copy(showOfflineReport = v) } },
            )
            Text(
                offlineCapLine,
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaContentColors.muted(),
            )
        }

        settingsCard(title = "Game") {
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
            Text(
                cadenceLine,
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaContentColors.muted(),
            )
        }

        settingsCard(title = "Audio") {
            settingsSwitchRow(
                title = "Sound",
                subtitle = "Master toggle for game audio",
                checked = prefs.soundEnabled,
                onCheckedChange = { v -> updatePrefs { it.copy(soundEnabled = v) } },
            )
            settingsSwitchRow(
                title = "Idle soundscapes",
                subtitle = "Ambient drone while in the game screen (requires Sound on)",
                checked = prefs.idleSoundscapesEnabled,
                onCheckedChange = { v -> updatePrefs { it.copy(idleSoundscapesEnabled = v) } },
            )
            TestSoundButton(soundEnabled = prefs.soundEnabled)
        }

        settingsCard(title = "Notifications") {
            settingsSwitchRow(
                title = "Quest & daily reminders",
                subtitle = "Requires notification permission (not wired yet)",
                checked = false,
                enabled = false,
                onCheckedChange = { },
            )
        }

        settingsCard(title = "Economy") {
            Text(
                "Lumina, shop, and login bonus — planned.",
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaContentColors.muted(),
            )
        }

        settingsCard(title = "About") {
            OutlinedButton(
                onClick = { subScreen = SettingsSubScreen.Changelog },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ArteriaContentColors.secondary()),
            ) {
                Text("What's New")
            }
            OutlinedButton(
                onClick = { subScreen = SettingsSubScreen.Licenses },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ArteriaContentColors.secondary()),
            ) {
                Text("Open source notices")
            }
            OutlinedButton(
                onClick = { subScreen = SettingsSubScreen.Credits },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ArteriaContentColors.secondary()),
            ) {
                Text("Credits")
            }
        }

        if (BuildConfig.DEBUG) {
            settingsCard(title = "Debug") {
                settingsSwitchRow(
                    title = "Remove offline time cap",
                    subtitle = "DEBUG: can simulate very long away times",
                    checked = prefs.debugRemoveOfflineCap,
                    onCheckedChange = { v -> updatePrefs { it.copy(debugRemoveOfflineCap = v) } },
                )
            }
        }

        settingsCard(title = "Danger zone") {
            Text(
                "These actions cannot be undone.",
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaPalette.GoldDim,
            )
            OutlinedButton(
                onClick = { showResetConfirm = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ArteriaPalette.AccentHover),
            ) {
                Text("Reset game progress")
            }
            OutlinedButton(
                onClick = { showDeleteConfirm = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ArteriaPalette.GoldDim),
            ) {
                Text("Delete this profile")
            }
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
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            containerColor = ArteriaContentColors.cardSurface(),
            title = { Text("Reset progress?", color = ArteriaContentColors.primary()) },
            text = {
                Text(
                    "All skills, bank items, and training state for this profile will be wiped.",
                    color = ArteriaContentColors.secondary(),
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
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
                ) { Text("Reset", color = ArteriaPalette.GoldDim) }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) {
                    Text("Cancel", color = ArteriaContentColors.secondary())
                }
            },
        )
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            containerColor = ArteriaContentColors.cardSurface(),
            title = { Text("Delete profile?", color = ArteriaContentColors.primary()) },
            text = {
                Text(
                    "Removes this account and all saved game data. This cannot be undone.",
                    color = ArteriaContentColors.secondary(),
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
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
                ) { Text("Delete", color = ArteriaPalette.GoldDim) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel", color = ArteriaContentColors.secondary())
                }
            },
        )
    }
}

@Composable
private fun SettingsHero(
    accountSession: AccountSessionInfo,
    cadenceLine: String,
    offlineCapLine: String,
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = ArteriaPalette.BgCard.copy(alpha = 0.82f),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ArteriaPalette.AccentPrimary.copy(alpha = 0.38f), RoundedCornerShape(8.dp)),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = "COMMAND SETTINGS",
                style = MaterialTheme.typography.labelSmall,
                color = ArteriaPalette.Gold,
            )
            Text(
                text = accountSession.displayName,
                style = MaterialTheme.typography.headlineMedium,
                color = ArteriaContentColors.primary(),
                fontWeight = FontWeight.Bold,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoPill("Mode", accountSession.gameMode, Modifier.weight(1f))
                InfoPill("Build", "v${BuildConfig.VERSION_NAME}", Modifier.weight(1f))
            }
            InfoPill(
                label = "Last played",
                value = formatLastPlayed(accountSession.lastPlayedAtEpochMs),
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = cadenceLine,
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaContentColors.secondary(),
            )
            Text(
                text = offlineCapLine,
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaContentColors.muted(),
            )
        }
    }
}

@Composable
private fun InfoPill(label: String, value: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(ArteriaPalette.BgInput.copy(alpha = 0.72f))
            .border(1.dp, ArteriaContentColors.border(), RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
    ) {
        Column {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = ArteriaContentColors.muted(),
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = ArteriaContentColors.primary(),
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun settingsCard(title: String, content: @Composable () -> Unit) {
    val shape = RoundedCornerShape(8.dp)
    Surface(
        shape = shape,
        color = ArteriaContentColors.cardSurface().copy(alpha = 0.94f),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ArteriaContentColors.border(), shape),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = ArteriaPalette.Gold,
            )
            content()
        }
    }
}

@Composable
private fun settingsSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, color = ArteriaContentColors.primary())
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = ArteriaContentColors.muted())
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = ArteriaPalette.AccentPrimary,
                checkedTrackColor = ArteriaPalette.AccentPrimary.copy(alpha = 0.45f),
            ),
        )
    }
}

@Composable
private fun TextButtonChip(label: String, selected: Boolean, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            contentColor = if (selected) ArteriaPalette.AccentPrimary else ArteriaContentColors.secondary(),
        ),
    ) {
        Text(label)
    }
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
        titleContentColor = ArteriaContentColors.primary(),
        textContentColor = ArteriaContentColors.secondary(),
        title = { Text("Display name", style = MaterialTheme.typography.titleMedium) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                        error = null
                    },
                    singleLine = true,
                    isError = error != null,
                    supportingText = {
                        error?.let { Text(it, color = ArteriaPalette.GoldDim) }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = ArteriaContentColors.primary(),
                        unfocusedTextColor = ArteriaContentColors.primary(),
                        focusedBorderColor = ArteriaPalette.AccentPrimary,
                        unfocusedBorderColor = ArteriaContentColors.border(),
                        cursorColor = ArteriaPalette.AccentHover,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    scope.launch {
                        val err = onRenameDisplayName(text)
                        if (err == null) {
                            onSuccess()
                        } else {
                            error = err
                        }
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

@Composable
private fun TestSoundButton(soundEnabled: Boolean) {
    val context = LocalContext.current
    OutlinedButton(
        onClick = {
            if (!soundEnabled) return@OutlinedButton
            runCatching {
                val tg = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
                tg.startTone(ToneGenerator.TONE_PROP_BEEP, 160)
                android.os.Handler(context.mainLooper).postDelayed({ tg.release() }, 220L)
            }
        },
        enabled = soundEnabled,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = ArteriaContentColors.secondary(),
            disabledContentColor = ArteriaContentColors.muted(),
        ),
    ) {
        Text("Test sound")
    }
}

private fun formatLastPlayed(epochMs: Long): String {
    if (epochMs <= 0L) {
        return "—"
    }
    val fmt = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
    return fmt.format(Date(epochMs))
}
