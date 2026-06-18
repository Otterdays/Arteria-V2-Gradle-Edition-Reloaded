package com.arteria.game.ui.game

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arteria.game.BuildConfig
import com.arteria.game.ui.account.AccountSessionInfo
import com.arteria.game.ui.theme.ArteriaContentColors
import com.arteria.game.ui.theme.ArteriaPalette
import java.text.DateFormat
import java.util.Date

/** Live profile stats shown in settings — computed in [GameScreen]. */
data class SettingsGameSnapshot(
    val totalLevel: Int,
    val bankItemTypes: Int,
    val bankTotalStacks: Int,
    val achievementsUnlocked: Int,
    val achievementsTotal: Int,
)

@Composable
fun SettingsHero(
    accountSession: AccountSessionInfo,
    gameSnapshot: SettingsGameSnapshot?,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = ArteriaPalette.BgCard.copy(alpha = 0.82f),
        modifier = modifier
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
            gameSnapshot?.let { snap ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoPill("Total lvl", snap.totalLevel.toString(), Modifier.weight(1f))
                    InfoPill("Bank", "${snap.bankItemTypes} types", Modifier.weight(1f))
                }
                InfoPill(
                    label = "Chronicle",
                    value = "${snap.achievementsUnlocked}/${snap.achievementsTotal}",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            InfoPill(
                label = "Last played",
                value = formatLastPlayed(accountSession.lastPlayedAtEpochMs),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun SettingsSectionDivider(label: String, modifier: Modifier = Modifier) {
    Text(
        text = label.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = ArteriaContentColors.muted(),
        modifier = modifier.padding(start = 4.dp, top = 4.dp, bottom = 2.dp),
    )
}

@Composable
fun SettingsCard(title: String, content: @Composable () -> Unit) {
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
fun SettingsCollapsibleCard(
    title: String,
    subtitle: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit,
) {
    val shape = RoundedCornerShape(8.dp)
    Surface(
        shape = shape,
        color = ArteriaContentColors.cardSurface().copy(alpha = 0.94f),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .border(1.dp, ArteriaContentColors.border(), shape),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggle),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.labelSmall, color = ArteriaPalette.Gold)
                    Text(subtitle, style = MaterialTheme.typography.bodySmall, color = ArteriaContentColors.muted())
                }
                Text(
                    text = if (expanded) "−" else "+",
                    style = MaterialTheme.typography.titleMedium,
                    color = ArteriaContentColors.secondary(),
                )
            }
            if (expanded) {
                Column(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
fun SettingsSwitchRow(
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
fun SettingsNavRow(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, color = ArteriaContentColors.primary())
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = ArteriaContentColors.muted())
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = ArteriaContentColors.muted(),
        )
    }
}

@Composable
fun ThemeChip(label: String, selected: Boolean, onClick: () -> Unit) {
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
fun SettingsOutlinedAction(
    label: String,
    onClick: () -> Unit,
    contentColor: androidx.compose.ui.graphics.Color = ArteriaPalette.AccentPrimary,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = contentColor),
    ) {
        Text(label)
    }
}

@Composable
fun TestSoundButton(soundEnabled: Boolean) {
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

fun formatLastPlayed(epochMs: Long): String {
    if (epochMs <= 0L) return "—"
    return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(epochMs))
}
