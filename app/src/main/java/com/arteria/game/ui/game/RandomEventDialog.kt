package com.arteria.game.ui.game

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.arteria.game.core.model.ActiveRandomEvent
import com.arteria.game.core.model.EventEffect
import com.arteria.game.ui.theme.ArteriaPalette

@Composable
fun RandomEventDialog(
    activeEvent: ActiveRandomEvent,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val event = activeEvent.event

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = ArteriaPalette.BgCard,
            modifier = modifier
                .fillMaxWidth()
                .border(1.dp, ArteriaPalette.Gold.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "✦ RANDOM EVENT ✦",
                    style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                    color = ArteriaPalette.Gold,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = event.title,
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                    color = ArteriaPalette.TextPrimary,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = event.message,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                    color = ArteriaPalette.TextSecondary,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(8.dp))

                val effect = event.effect
                val effectText = when (effect) {
                    is EventEffect.XpBonus -> "+${((effect.multiplier - 1) * 100).toInt()}% XP boost!"
                    is EventEffect.ResourceBonus -> "Found ${effect.amount} ${effect.itemId}!"
                    is EventEffect.ResourceLoss -> "Lost ${effect.amount} ${effect.itemId}..."
                    is EventEffect.TemporaryBuff -> effect.description
                    EventEffect.Nothing -> "No lasting effect."
                }

                Text(
                    text = effectText,
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                    color = when (effect) {
                        is EventEffect.XpBonus, is EventEffect.ResourceBonus, is EventEffect.TemporaryBuff ->
                            ArteriaPalette.BalancedEnd
                        is EventEffect.ResourceLoss -> ArteriaPalette.CombatAccent
                        EventEffect.Nothing -> ArteriaPalette.TextMuted
                    },
                    fontWeight = FontWeight.Medium,
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ArteriaPalette.AccentPrimary,
                        ),
                    ) {
                        Text("Continue")
                    }
                }
            }
        }
    }
}
