package com.arteria.game.ui.account

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arteria.game.BuildConfig
import com.arteria.game.ui.components.ChangelogScreen
import com.arteria.game.ui.components.DockingAccountCard
import com.arteria.game.ui.components.DockingBackground
import com.arteria.game.ui.components.DockingLoreFooter
import com.arteria.game.ui.components.DockingNewAccountSlot
import com.arteria.game.ui.components.DockingTitleBlock
import com.arteria.game.ui.components.dockingGradientForIndex
import com.arteria.game.ui.theme.ArteriaContentColors
import com.arteria.game.ui.theme.ArteriaPalette
import com.arteria.game.ui.theme.LocalReduceMotion
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class AccountSlot(
    val id: String,
    val displayName: String,
    val gameMode: GameMode = GameMode.STANDARD,
    val lastPlayedAtEpochMs: Long = 0L,
    val isActive: Boolean = false,
) {
    val gameModeLabel: String
        get() = "${gameMode.displayName} | ${lastPlayedLabel()}"

    fun lastPlayedLabel(): String {
        if (lastPlayedAtEpochMs <= 0L) return "Never played"
        val formatter = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
        return "Last played ${formatter.format(Date(lastPlayedAtEpochMs))}"
    }
}

enum class GameMode(
    val displayName: String,
    val description: String,
    val gains: String,
    val losses: String,
    val comingSoon: Boolean,
    val avatarIcon: String,
) {
    STANDARD(
        displayName = "Standard",
        description = "The classic Arteria experience.",
        gains = "Normal XP rates · Full offline progress · All features unlocked",
        losses = "None",
        comingSoon = false,
        avatarIcon = "🚀",
    ),
    IRONCLAD(
        displayName = "Ironclad",
        description = "Self-sufficient. No trading, no bank sharing between profiles.",
        gains = "+25% XP · Exclusive Ironclad achievements · Bragging rights",
        losses = "No trading · No bank sharing · No companion gifting · Hardcore permadeath risk",
        comingSoon = true,
        avatarIcon = "🛡️",
    ),
    VOIDTOUCHED(
        displayName = "Void-Touched",
        description = "The void whispers. Higher stakes, greater rewards.",
        gains = "+50% XP · Unique Void-Touched cosmetics · Double rare drop chance",
        losses = "-50% offline progress · Random void events can destroy items · No prestige safety net",
        comingSoon = true,
        avatarIcon = "🌀",
    );

    companion object {
        fun fromLabel(value: String): GameMode =
            entries.firstOrNull { it.displayName.equals(value, ignoreCase = true) } ?: STANDARD
    }
}

@Composable
fun AccountSelectionScreen(
    accounts: List<AccountSlot>,
    onCreateNewClick: () -> Unit,
    selectedId: String?,
    errorMessage: String?,
    onSelectAccount: (String) -> Unit,
    onContinueWithAccount: () -> Unit,
    onSettingsClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    var showChangelog by remember { mutableStateOf(false) }

    BackHandler(enabled = showChangelog) { showChangelog = false }

    if (showChangelog) {
        ChangelogScreen(onBack = { showChangelog = false }, modifier = modifier)
        return
    }

    Box(modifier = modifier.fillMaxSize()) {
        DockingBackground(Modifier.fillMaxSize())
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DockingTitleBlock(
                    title = "The Docking Station",
                    subtitle = "Select an account to continue, or forge a new profile.",
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = "v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                    style = MaterialTheme.typography.labelSmall,
                    color = ArteriaPalette.TextMuted,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
            Spacer(Modifier.height(8.dp))
            if (!errorMessage.isNullOrBlank()) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.AccentHover,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                )
            }

            if (accounts.isEmpty()) {
                EmptyStateSection(onCreateNewClick = onCreateNewClick)
                Spacer(Modifier.weight(1f))
            } else {
                val selectedAccount = accounts.firstOrNull { it.id == selectedId }
                    ?: accounts.firstOrNull { it.isActive }
                    ?: accounts.first()
                ContinueHero(
                    account = selectedAccount,
                    onContinue = onContinueWithAccount,
                )
                Spacer(Modifier.height(8.dp))
                CurrentReleaseStrip(onClick = { showChangelog = true })
                Spacer(Modifier.height(8.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 12.dp),
                    modifier = Modifier.weight(1f),
                ) {
                    itemsIndexed(accounts, key = { _, a -> a.id }) { index, account ->
                        val selected = account.id == selectedId
                        DockingAccountCard(
                            displayName = account.displayName,
                            metaLine = account.gameModeLabel,
                            selected = selected,
                            gradient = dockingGradientForIndex(index),
                            onClick = { onSelectAccount(account.id) },
                            entryIndex = index,
                            showTimelineConnectorBelow = true,
                            avatarIcon = account.gameMode.avatarIcon,
                        )
                    }
                    item {
                        val reduceMotion = LocalReduceMotion.current
                        val termPulse = rememberInfiniteTransition(label = "term")
                        val termAlpha = if (reduceMotion) {
                            0.42f
                        } else {
                            termPulse.animateFloat(
                                initialValue = 0.28f,
                                targetValue = 0.55f,
                                animationSpec = infiniteRepeatable(
                                    tween(2400, easing = LinearEasing),
                                    RepeatMode.Reverse,
                                ),
                                label = "term_a",
                            ).value
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 2.dp),
                            verticalAlignment = Alignment.Top,
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .width(32.dp)
                                    .padding(top = 16.dp),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(
                                            color = ArteriaPalette.LuminarEnd.copy(alpha = termAlpha),
                                            shape = CircleShape,
                                        )
                                        .border(
                                            width = 0.5.dp,
                                            color = ArteriaPalette.LuminarEnd.copy(
                                                alpha = termAlpha * 0.6f,
                                            ),
                                            shape = CircleShape,
                                        ),
                                )
                            }
                            Spacer(Modifier.width(6.dp))
                            DockingNewAccountSlot(
                                onClick = onCreateNewClick,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(
                    onClick = { showChangelog = true },
                ) {
                    Text(
                        text = "Version history",
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaContentColors.muted(),
                    )
                }
                if (onSettingsClick != null) {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings",
                            tint = ArteriaContentColors.muted(),
                        )
                    }
                }
            }

            DockingLoreFooter(
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        }
    }
}

@Composable
private fun ContinueHero(
    account: AccountSlot,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = ArteriaPalette.BgCard.copy(alpha = 0.82f),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            ArteriaPalette.AccentPrimary.copy(alpha = 0.45f),
        ),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "CONTINUE",
                style = MaterialTheme.typography.labelSmall,
                color = ArteriaPalette.Gold,
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = account.gameMode.avatarIcon,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = account.displayName,
                        style = MaterialTheme.typography.titleLarge,
                        color = ArteriaPalette.TextPrimary,
                        maxLines = 1,
                    )
                    Text(
                        text = "${account.gameMode.displayName} | ${account.lastPlayedLabel()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaPalette.TextMuted,
                    )
                }
                Button(
                    onClick = onContinue,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ArteriaPalette.AccentPrimary,
                        contentColor = Color.White,
                    ),
                ) {
                    Text("Enter")
                }
            }
        }
    }
}

@Composable
private fun CurrentReleaseStrip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = ArteriaPalette.TextSecondary,
            containerColor = ArteriaPalette.BgCard.copy(alpha = 0.45f),
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            ArteriaPalette.VoidAccent.copy(alpha = 0.35f),
        ),
    ) {
        Text(
            text = "v${BuildConfig.VERSION_NAME} | Summoning unlocked | What's New",
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun EmptyStateSection(
    onCreateNewClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    color = ArteriaPalette.BgCard.copy(alpha = 0.5f),
                    shape = CircleShape,
                )
                .border(
                    width = 1.5.dp,
                    color = ArteriaPalette.AccentPrimary.copy(alpha = 0.3f),
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "🌌",
                style = MaterialTheme.typography.displaySmall,
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Your journey begins here",
            style = MaterialTheme.typography.titleMedium,
            color = ArteriaPalette.TextPrimary,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Create a profile to start training skills,\nbuilding your bank, and exploring the cosmos.",
            style = MaterialTheme.typography.bodyMedium,
            color = ArteriaPalette.TextMuted,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = onCreateNewClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = ArteriaPalette.AccentPrimary,
                contentColor = Color.White,
            ),
        ) {
            Text("Create your first account")
        }
    }
}

@Composable
private fun EnterTimelineButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val reduceMotion = LocalReduceMotion.current
    val infiniteTransition = rememberInfiniteTransition(label = "enter_btn")
    val pulseScale = if (enabled && !reduceMotion) {
        infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.015f,
            animationSpec = infiniteRepeatable(
                tween(1200, easing = LinearEasing),
                RepeatMode.Reverse,
            ),
            label = "enter_pulse",
        ).value
    } else {
        1f
    }

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .scale(pulseScale),
        colors = ButtonDefaults.buttonColors(
            containerColor = ArteriaPalette.AccentPrimary,
            contentColor = Color.White,
            disabledContainerColor = ArteriaPalette.BgCard,
            disabledContentColor = ArteriaContentColors.muted(),
        ),
    ) {
        Text("Enter timeline")
    }
}
