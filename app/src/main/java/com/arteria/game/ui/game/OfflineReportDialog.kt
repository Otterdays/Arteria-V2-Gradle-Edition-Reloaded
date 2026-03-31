package com.arteria.game.ui.game

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.arteria.game.core.data.MiningData
import com.arteria.game.core.model.TickResult
import com.arteria.game.ui.theme.ArteriaPalette
import java.text.NumberFormat

@Composable
fun OfflineReportDialog(
    report: TickResult,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val nf = NumberFormat.getIntegerInstance()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = ArteriaPalette.BgCard,
            modifier = modifier.border(
                1.dp,
                ArteriaPalette.AccentPrimary.copy(alpha = 0.3f),
                RoundedCornerShape(16.dp),
            ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "While You Were Away",
                    style = MaterialTheme.typography.headlineMedium,
                    color = ArteriaPalette.Gold,
                )

                Spacer(Modifier.height(16.dp))

                if (report.xpGained.isNotEmpty()) {
                    Text(
                        text = "XP GAINED",
                        style = MaterialTheme.typography.labelSmall,
                        color = ArteriaPalette.TextMuted,
                    )
                    Spacer(Modifier.height(4.dp))
                    for ((skillId, xp) in report.xpGained) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = skillId.displayName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = ArteriaPalette.TextPrimary,
                            )
                            Text(
                                text = "+${nf.format(xp.toLong())} XP",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = ArteriaPalette.BalancedEnd,
                            )
                        }
                    }
                }

                if (report.resourcesGained.isNotEmpty()) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "RESOURCES",
                        style = MaterialTheme.typography.labelSmall,
                        color = ArteriaPalette.TextMuted,
                    )
                    Spacer(Modifier.height(4.dp))
                    for ((itemId, qty) in report.resourcesGained) {
                        val name = MiningData.items[itemId]?.name
                            ?: itemId.replace("_", " ").replaceFirstChar { it.uppercase() }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.bodyMedium,
                                color = ArteriaPalette.TextPrimary,
                            )
                            Text(
                                text = "+${nf.format(qty)}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = ArteriaPalette.Gold,
                            )
                        }
                    }
                }

                if (report.levelUps.isNotEmpty()) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "LEVEL UPS",
                        style = MaterialTheme.typography.labelSmall,
                        color = ArteriaPalette.TextMuted,
                    )
                    Spacer(Modifier.height(4.dp))
                    for (levelUp in report.levelUps) {
                        Text(
                            text = "${levelUp.skillId.displayName}: ${levelUp.oldLevel} → ${levelUp.newLevel}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = ArteriaPalette.AccentWeb,
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ArteriaPalette.AccentPrimary,
                        contentColor = Color.White,
                    ),
                ) {
                    Text("Continue")
                }
            }
        }
    }
}

