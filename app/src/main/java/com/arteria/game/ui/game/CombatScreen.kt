package com.arteria.game.ui.game

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arteria.game.ui.theme.ArteriaPalette

@Composable
fun CombatScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = ArteriaPalette.BgCard.copy(alpha = 0.94f),
            modifier = Modifier
                .padding(32.dp)
                .border(1.dp, ArteriaPalette.Border, RoundedCornerShape(12.dp)),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "The Encounter",
                    style = MaterialTheme.typography.headlineMedium,
                    color = ArteriaPalette.TextPrimary,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Combat system arriving soon",
                    style = MaterialTheme.typography.bodyLarge,
                    color = ArteriaPalette.TextSecondary,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Train your Attack, Strength, and Defence\nin the Skills tab to prepare",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ArteriaPalette.TextMuted,
                )
            }
        }
    }
}

