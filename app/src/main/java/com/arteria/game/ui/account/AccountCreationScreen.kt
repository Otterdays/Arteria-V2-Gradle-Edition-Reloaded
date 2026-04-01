package com.arteria.game.ui.account

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arteria.game.ui.account.GameMode
import com.arteria.game.ui.components.DockingBackground
import com.arteria.game.ui.theme.ArteriaPalette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountCreationScreen(
    onBack: () -> Unit,
    onCreateAccount: (displayName: String) -> Unit,
    errorMessage: String?,
    onClearError: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var displayName by rememberSaveable { mutableStateOf("") }
    var selectedMode by rememberSaveable { mutableStateOf(GameMode.STANDARD) }
    var showModeDetails by rememberSaveable { mutableStateOf<GameMode?>(null) }
    val cardShape = RoundedCornerShape(12.dp)

    Box(modifier = modifier.fillMaxSize()) {
        DockingBackground(Modifier.fillMaxSize())
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "New account",
                        style = MaterialTheme.typography.titleLarge,
                        color = ArteriaPalette.TextPrimary,
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back", color = ArteriaPalette.AccentPrimary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = ArteriaPalette.TextPrimary,
                ),
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "Name your profile to begin your session.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ArteriaPalette.TextSecondary,
                )
                Surface(
                    shape = cardShape,
                    color = ArteriaPalette.BgCard.copy(alpha = 0.94f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ArteriaPalette.Border, cardShape),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        OutlinedTextField(
                            value = displayName,
                            onValueChange = {
                                displayName = it
                                onClearError()
                            },
                            label = { Text("Account name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = ArteriaPalette.TextPrimary,
                                unfocusedTextColor = ArteriaPalette.TextPrimary,
                                focusedBorderColor = ArteriaPalette.AccentPrimary,
                                unfocusedBorderColor = ArteriaPalette.Border,
                                focusedLabelColor = ArteriaPalette.AccentHover,
                                unfocusedLabelColor = ArteriaPalette.TextMuted,
                                cursorColor = ArteriaPalette.AccentPrimary,
                                focusedContainerColor = ArteriaPalette.BgInput.copy(alpha = 0.6f),
                                unfocusedContainerColor = ArteriaPalette.BgInput.copy(alpha = 0.6f),
                            ),
                        )
                        if (!errorMessage.isNullOrBlank()) {
                            Text(
                                text = errorMessage,
                                style = MaterialTheme.typography.bodySmall,
                                color = ArteriaPalette.AccentHover,
                            )
                        }
                        Text(
                            text = "Game mode",
                            style = MaterialTheme.typography.titleSmall,
                            color = ArteriaPalette.TextPrimary,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            GameMode.entries.forEach { mode ->
                                FilterChip(
                                    selected = selectedMode == mode,
                                    onClick = {
                                        if (!mode.comingSoon) {
                                            selectedMode = mode
                                        } else {
                                            showModeDetails = mode
                                        }
                                    },
                                    label = {
                                        Text(
                                            text = if (mode.comingSoon) "${mode.displayName} 🔒" else mode.displayName,
                                            style = MaterialTheme.typography.labelSmall,
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        containerColor = if (mode.comingSoon) {
                                            ArteriaPalette.BgCard.copy(alpha = 0.3f)
                                        } else {
                                            ArteriaPalette.AccentPrimary.copy(alpha = 0.22f)
                                        },
                                        labelColor = if (mode.comingSoon) {
                                            ArteriaPalette.TextMuted
                                        } else {
                                            ArteriaPalette.AccentHover
                                        },
                                        selectedContainerColor = if (mode.comingSoon) {
                                            ArteriaPalette.BgCard.copy(alpha = 0.3f)
                                        } else {
                                            ArteriaPalette.AccentPrimary.copy(alpha = 0.35f)
                                        },
                                        selectedLabelColor = if (mode.comingSoon) {
                                            ArteriaPalette.TextMuted
                                        } else {
                                            Color.White
                                        },
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = selectedMode == mode && !mode.comingSoon,
                                        borderColor = if (mode.comingSoon) {
                                            ArteriaPalette.Border.copy(alpha = 0.3f)
                                        } else {
                                            ArteriaPalette.AccentPrimary.copy(alpha = 0.55f)
                                        },
                                    ),
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        }

                        showModeDetails?.let { mode ->
                            GameModeDetailCard(
                                mode = mode,
                                onDismiss = { showModeDetails = null },
                            )
                        }
                    }
                }
                Button(
                    onClick = {
                        onCreateAccount(displayName)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ArteriaPalette.AccentPrimary,
                        contentColor = Color.White,
                    ),
                ) {
                    Text("Create account")
                }
            }
        }
    }
}

@Composable
private fun GameModeDetailCard(
    mode: GameMode,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val detailCardShape = RoundedCornerShape(10.dp)
    val accentColor = when (mode) {
        GameMode.IRONCLAD -> ArteriaPalette.Gold
        GameMode.VOIDTOUCHED -> ArteriaPalette.VoidAccent
        else -> ArteriaPalette.TextMuted
    }

    Surface(
        shape = detailCardShape,
        color = ArteriaPalette.BgCard.copy(alpha = 0.95f),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, accentColor.copy(alpha = 0.3f), detailCardShape),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${mode.displayName}",
                    style = MaterialTheme.typography.titleSmall,
                    color = accentColor,
                    fontWeight = FontWeight.Bold,
                )
                TextButton(onClick = onDismiss) {
                    Text("Close", color = ArteriaPalette.TextMuted)
                }
            }

            Text(
                text = mode.description,
                style = MaterialTheme.typography.bodyMedium,
                color = ArteriaPalette.TextSecondary,
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "GAINS",
                    style = MaterialTheme.typography.labelSmall,
                    color = ArteriaPalette.BalancedEnd,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = mode.gains,
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.TextPrimary,
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "LOSSES",
                    style = MaterialTheme.typography.labelSmall,
                    color = ArteriaPalette.CombatAccent,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = mode.losses,
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.TextPrimary,
                )
            }

            Text(
                text = "Coming Soon — Tap Standard to create an account now.",
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaPalette.TextMuted,
            )
        }
    }
}
