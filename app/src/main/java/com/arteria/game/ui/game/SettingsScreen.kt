package com.arteria.game.ui.game

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.arteria.game.BuildConfig
import com.arteria.game.ui.account.AccountSessionInfo
import com.arteria.game.ui.components.ChangelogScreen
import com.arteria.game.ui.theme.ArteriaPalette
import java.text.DateFormat
import java.util.Date
import kotlinx.coroutines.launch

// [TRACE: master_settings_suggestions_doc.md — V1 parity settings slice]

@Composable
fun SettingsScreen(
    accountSession: AccountSessionInfo,
    tickIntervalMs: Long,
    saveIntervalMs: Long,
    onBack: () -> Unit,
    onBackToAccounts: () -> Unit,
    onRenameDisplayName: suspend (String) -> String?,
    onRenameSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showChangelog by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = showChangelog) { showChangelog = false }

    if (showChangelog) {
        ChangelogScreen(onBack = { showChangelog = false }, modifier = modifier)
        return
    }

    val bgBrush = Brush.verticalGradient(
        colors = listOf(
            ArteriaPalette.BgDeepSpaceTop,
            ArteriaPalette.BgDeepSpaceMid,
            ArteriaPalette.BgDeepSpaceBottom,
        ),
    )

    val tickSeconds = tickIntervalMs / 1000f
    val saveSeconds = saveIntervalMs / 1000f
    val cadenceLine = "Ticks every ${tickSeconds}s while active; saves about every ${saveSeconds}s."

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgBrush)
            .statusBarsPadding()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = ArteriaPalette.TextSecondary,
                )
            }
            Text(
                text = "SETTINGS",
                style = MaterialTheme.typography.labelSmall,
                color = ArteriaPalette.Gold,
            )
        }

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = ArteriaPalette.BgCard.copy(alpha = 0.94f),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, ArteriaPalette.Border, RoundedCornerShape(12.dp)),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.labelSmall,
                    color = ArteriaPalette.TextMuted,
                )
                Text(
                    text = accountSession.displayName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = ArteriaPalette.TextPrimary,
                )
                Text(
                    text = "Game mode · ${accountSession.gameMode}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ArteriaPalette.AccentHover,
                )
                Text(
                    text = "Last played · ${formatLastPlayed(accountSession.lastPlayedAtEpochMs)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.TextSecondary,
                )
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { showRenameDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = ArteriaPalette.AccentPrimary,
                    ),
                ) {
                    Text("Edit display name")
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = ArteriaPalette.BgCard.copy(alpha = 0.94f),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, ArteriaPalette.Border, RoundedCornerShape(12.dp)),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "Game",
                    style = MaterialTheme.typography.labelSmall,
                    color = ArteriaPalette.TextMuted,
                )
                Text(
                    text = "Arteria Gradle Edition V2",
                    style = MaterialTheme.typography.bodyLarge,
                    color = ArteriaPalette.TextPrimary,
                )
                Text(
                    text = "Version ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ArteriaPalette.TextSecondary,
                )
                Text(
                    text = cadenceLine,
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.TextMuted,
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = ArteriaPalette.BgCard.copy(alpha = 0.94f),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, ArteriaPalette.Border, RoundedCornerShape(12.dp)),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Audio",
                    style = MaterialTheme.typography.labelSmall,
                    color = ArteriaPalette.TextMuted,
                )
                TestSoundButton()
            }
        }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = { showChangelog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = ArteriaPalette.TextSecondary,
            ),
        ) {
            Text("What's New")
        }

        OutlinedButton(
            onClick = onBackToAccounts,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = ArteriaPalette.AccentPrimary,
            ),
        ) {
            Text("Switch Account")
        }
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
        containerColor = ArteriaPalette.BgCard,
        titleContentColor = ArteriaPalette.TextPrimary,
        textContentColor = ArteriaPalette.TextSecondary,
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
                        focusedTextColor = ArteriaPalette.TextPrimary,
                        unfocusedTextColor = ArteriaPalette.TextPrimary,
                        focusedBorderColor = ArteriaPalette.AccentPrimary,
                        unfocusedBorderColor = ArteriaPalette.Border,
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
                Text("Cancel", color = ArteriaPalette.TextSecondary)
            }
        },
    )
}

@Composable
private fun TestSoundButton() {
    val context = LocalContext.current
    OutlinedButton(
        onClick = {
            runCatching {
                val tg = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
                tg.startTone(ToneGenerator.TONE_PROP_BEEP, 160)
                android.os.Handler(context.mainLooper).postDelayed({ tg.release() }, 220L)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = ArteriaPalette.TextSecondary,
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
