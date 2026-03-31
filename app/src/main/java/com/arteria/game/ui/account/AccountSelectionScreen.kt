package com.arteria.game.ui.account

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arteria.game.ui.components.ChangelogScreen
import com.arteria.game.ui.components.DockingAccountCard
import com.arteria.game.ui.components.DockingBackground
import com.arteria.game.ui.components.DockingLoreFooter
import com.arteria.game.ui.components.DockingNewAccountSlot
import com.arteria.game.ui.components.DockingTitleBlock
import com.arteria.game.ui.components.dockingGradientForIndex
import com.arteria.game.ui.theme.ArteriaPalette

data class AccountSlot(
    val id: String,
    val displayName: String,
)

@Composable
fun AccountSelectionScreen(
    accounts: List<AccountSlot>,
    onCreateNewClick: () -> Unit,
    selectedId: String?,
    errorMessage: String?,
    onSelectAccount: (String) -> Unit,
    onContinueWithAccount: () -> Unit,
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
            DockingTitleBlock(
                title = "The Docking Station",
                subtitle = "Select an account to continue, or forge a new profile.",
            )
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
                Text(
                    text = "No accounts yet.\nTap below to create your first slot.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ArteriaPalette.TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                )
                DockingNewAccountSlot(onClick = onCreateNewClick)
                Spacer(Modifier.weight(1f))
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 12.dp),
                    modifier = Modifier.weight(1f),
                ) {
                    itemsIndexed(accounts, key = { _, a -> a.id }) { index, account ->
                        val selected = account.id == selectedId
                        DockingAccountCard(
                            displayName = account.displayName,
                            metaLine = "Game mode · Standard",
                            selected = selected,
                            gradient = dockingGradientForIndex(index),
                            onClick = { onSelectAccount(account.id) },
                            entryIndex = index,
                            showTimelineConnectorBelow = true,
                        )
                    }
                    // Terminal node + new account slot
                    item {
                        val termPulse = rememberInfiniteTransition(label = "term")
                        val termAlpha = termPulse.animateFloat(
                            initialValue = 0.28f,
                            targetValue = 0.55f,
                            animationSpec = infiniteRepeatable(
                                tween(2400, easing = LinearEasing),
                                RepeatMode.Reverse,
                            ),
                            label = "term_a",
                        ).value

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 2.dp),
                            verticalAlignment = Alignment.Top,
                        ) {
                            // Terminal node — pulsing endpoint
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

            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { onContinueWithAccount() },
                enabled = selectedId != null && accounts.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ArteriaPalette.AccentPrimary,
                    contentColor = Color.White,
                    disabledContainerColor = ArteriaPalette.BgCard,
                    disabledContentColor = ArteriaPalette.TextMuted,
                ),
            ) {
                Text("Enter timeline")
            }

            TextButton(
                onClick = { showChangelog = true },
                modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {
                Text(
                    text = "What's New",
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.TextMuted,
                )
            }

            DockingLoreFooter(
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        }
    }
}
