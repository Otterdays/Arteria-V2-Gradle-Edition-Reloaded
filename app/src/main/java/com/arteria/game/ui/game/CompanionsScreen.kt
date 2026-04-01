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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arteria.game.core.data.CompanionRegistry
import com.arteria.game.core.model.CompanionBonus
import com.arteria.game.core.model.CompanionRarity
import com.arteria.game.core.model.CompanionState
import com.arteria.game.ui.theme.ArteriaPalette

@Composable
fun CompanionsScreen(
    ownedCompanions: List<CompanionState>,
    playerLevel: Int,
    onSummon: (String) -> Unit,
    onDismiss: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val summonedIds = ownedCompanions.filter { it.isSummoned }.map { it.companionId }.toSet()

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
                text = "COMPANIONS",
                style = MaterialTheme.typography.labelSmall,
                color = ArteriaPalette.Gold,
            )
            Text(
                text = "${summonedIds.size} summoned",
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaPalette.TextMuted,
            )
        }

        if (ownedCompanions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No companions yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = ArteriaPalette.TextSecondary,
                    )
                    Text(
                        text = "Discover companions through gameplay",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ArteriaPalette.TextMuted,
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(
                    ownedCompanions.sortedBy { companion ->
                        CompanionRegistry.getById(companion.companionId)?.rarity?.ordinal ?: 0
                    },
                    key = { it.companionId },
                ) { state ->
                    val companion = CompanionRegistry.getById(state.companionId)
                    if (companion != null) {
                        CompanionCard(
                            companion = companion,
                            state = state,
                            playerLevel = playerLevel,
                            onSummon = { onSummon(companion.id) },
                            onDismiss = { onDismiss(companion.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CompanionCard(
    companion: com.arteria.game.core.model.Companion,
    state: CompanionState,
    playerLevel: Int,
    onSummon: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val rarityColor = when (companion.rarity) {
        CompanionRarity.COMMON -> ArteriaPalette.TextSecondary
        CompanionRarity.UNCOMMON -> ArteriaPalette.BalancedEnd
        CompanionRarity.RARE -> ArteriaPalette.AccentPrimary
        CompanionRarity.EPIC -> ArteriaPalette.LuminarEnd
        CompanionRarity.LEGENDARY -> ArteriaPalette.Gold
    }

    val cardShape = RoundedCornerShape(10.dp)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, rarityColor.copy(alpha = 0.3f), cardShape),
        shape = cardShape,
        color = if (state.isSummoned) ArteriaPalette.BgCardHover else ArteriaPalette.BgCard,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .border(1.dp, rarityColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = companion.icon,
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = companion.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = ArteriaPalette.TextPrimary,
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = companion.rarity.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = rarityColor,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Text(
                    text = companion.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.TextMuted,
                )

                val bonus = companion.passiveBonus
                val bonusText = when (bonus) {
                    is CompanionBonus.XpBoost -> {
                        val sid = bonus.skillId
                        if (sid != null) "+${((bonus.multiplier - 1) * 100).toInt()}% ${sid.displayName} XP"
                        else "+${((bonus.multiplier - 1) * 100).toInt()}% All XP"
                    }
                    is CompanionBonus.ResourceBoost -> "+${(bonus.extraChance * 100).toInt()}% ${bonus.itemId} drops"
                    is CompanionBonus.OfflineEfficiency -> "+${((bonus.multiplier - 1) * 100).toInt()}% offline efficiency"
                    is CompanionBonus.BankCapacity -> "+${bonus.extraSlots} bank slots"
                }

                Text(
                    text = bonusText,
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.BalancedEnd,
                    fontWeight = FontWeight.Medium,
                )
            }

            if (state.isSummoned) {
                androidx.compose.material3.TextButton(onClick = onDismiss) {
                    Text("Dismiss", color = ArteriaPalette.CombatAccent)
                }
            } else {
                androidx.compose.material3.TextButton(onClick = onSummon) {
                    Text("Summon")
                }
            }
        }
    }
}
