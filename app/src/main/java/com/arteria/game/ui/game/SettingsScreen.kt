package com.arteria.game.ui.game

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arteria.game.ui.theme.ArteriaPalette

@Composable
fun SettingsScreen(
    accountDisplayName: String,
    onBackToAccounts: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "SETTINGS",
            style = MaterialTheme.typography.labelSmall,
            color = ArteriaPalette.Gold,
            modifier = Modifier.padding(start = 4.dp),
        )

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

