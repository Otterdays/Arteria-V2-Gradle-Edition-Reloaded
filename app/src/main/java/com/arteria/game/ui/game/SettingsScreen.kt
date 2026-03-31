package com.arteria.game.ui.game

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.arteria.game.ui.components.ChangelogScreen
import com.arteria.game.ui.theme.ArteriaPalette

@Composable
fun SettingsScreen(
    accountDisplayName: String,
    onBack: () -> Unit,
    onBackToAccounts: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showChangelog by remember { mutableStateOf(false) }

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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgBrush)
            .statusBarsPadding()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Top bar with back arrow
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

        // Profile card
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
                    text = accountDisplayName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = ArteriaPalette.TextPrimary,
                )
                Text(
                    text = "Game mode · Standard",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ArteriaPalette.AccentHover,
                )
            }
        }

        // Game info card
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
                    text = "Version 1.1 · Kotlin + Compose",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ArteriaPalette.TextSecondary,
                )
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
}
