package com.arteria.game.ui.game

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arteria.game.core.model.PrestigePerk
import com.arteria.game.core.model.PrestigePerks
import com.arteria.game.core.model.PrestigeState
import com.arteria.game.core.skill.XPTable
import com.arteria.game.ui.theme.ArteriaPalette
import java.text.NumberFormat

@Composable
fun PrestigeScreen(
    prestigeState: PrestigeState,
    totalLevel: Int,
    onPrestige: () -> Unit,
    onBuyPerk: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val nf = NumberFormat.getIntegerInstance()
    val pointsOnOffer = calculatePointsOnOffer(totalLevel)
    val canPrestige = totalLevel >= 100

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "ASCENSION",
                style = MaterialTheme.typography.labelSmall,
                color = ArteriaPalette.Gold,
            )
            Text(
                text = "Prestige ${prestigeState.prestigeLevel}",
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaPalette.AccentPrimary,
            )
        }

        // Prestige summary card
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = ArteriaPalette.BgCard,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, ArteriaPalette.Gold.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    text = "Current Prestige Level: ${prestigeState.prestigeLevel}",
                    style = MaterialTheme.typography.titleMedium,
                    color = ArteriaPalette.Gold,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Total Points: ${nf.format(prestigeState.totalPrestigePoints.toInt())}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ArteriaPalette.TextPrimary,
                )
                Text(
                    text = "Total Level: $totalLevel",
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.TextMuted,
                )

                Spacer(Modifier.height(12.dp))

                if (canPrestige) {
                    Text(
                        text = "Points on offer: ${nf.format(pointsOnOffer.toInt())}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ArteriaPalette.BalancedEnd,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = onPrestige,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ArteriaPalette.Gold,
                        ),
                    ) {
                        Text("Ascend — Reset for Points")
                    }
                    Text(
                        text = "Warning: This resets your skills and bank",
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaPalette.CombatAccent,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                } else {
                    Text(
                        text = "Reach total level 100 to ascend",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ArteriaPalette.TextMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    val progress = (totalLevel / 100f).coerceIn(0f, 1f)
                    Spacer(Modifier.height(4.dp))
                    androidx.compose.material3.LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        color = ArteriaPalette.Gold,
                        trackColor = ArteriaPalette.Border.copy(alpha = 0.4f),
                    )
                    Text(
                        text = "$totalLevel / 100",
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaPalette.TextMuted,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "PERKS",
            style = MaterialTheme.typography.labelSmall,
            color = ArteriaPalette.AccentWeb,
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp),
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(PrestigePerks.all, key = { it.id }) { perk ->
                val currentLevel = prestigeState.unlockedPerks.count { it == perk.id }
                PerkCard(
                    perk = perk,
                    currentLevel = currentLevel,
                    availablePoints = prestigeState.totalPrestigePoints.toInt(),
                    onBuy = { onBuyPerk(perk.id) },
                )
            }
        }
    }
}

@Composable
private fun PerkCard(
    perk: PrestigePerk,
    currentLevel: Int,
    availablePoints: Int,
    onBuy: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(10.dp)
    val isMaxed = currentLevel >= perk.maxLevel
    val canAfford = availablePoints >= perk.cost

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = if (currentLevel > 0) 1.dp else 0.dp,
                color = if (currentLevel > 0) ArteriaPalette.AccentPrimary.copy(alpha = 0.3f) else ArteriaPalette.Border,
                shape = cardShape,
            ),
        shape = cardShape,
        color = ArteriaPalette.BgCard,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .border(1.dp, ArteriaPalette.Border.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = perk.icon,
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = perk.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = ArteriaPalette.TextPrimary,
                )
                Text(
                    text = perk.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.TextMuted,
                )
                Text(
                    text = "${perk.effectPerLevel} (Lv $currentLevel/${perk.maxLevel})",
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.BalancedEnd,
                )
                Text(
                    text = "Cost: ${perk.cost} point${if (perk.cost > 1) "s" else ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (canAfford) ArteriaPalette.Gold else ArteriaPalette.TextMuted,
                )
            }

            if (isMaxed) {
                Text(
                    text = "MAX",
                    style = MaterialTheme.typography.labelSmall,
                    color = ArteriaPalette.Gold,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            } else {
                Button(
                    onClick = onBuy,
                    enabled = canAfford,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ArteriaPalette.AccentPrimary,
                        disabledContainerColor = ArteriaPalette.Border,
                    ),
                ) {
                    Text("Buy")
                }
            }
        }
    }
}

private fun calculatePointsOnOffer(totalLevel: Int): Double {
    if (totalLevel < 100) return 0.0
    return (totalLevel - 100) * 0.5 + 10.0
}
