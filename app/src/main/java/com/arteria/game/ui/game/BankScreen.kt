package com.arteria.game.ui.game

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arteria.game.core.data.SkillDataRegistry
import com.arteria.game.ui.theme.ArteriaPalette
import java.text.NumberFormat

@Composable
fun BankScreen(
    bank: Map<String, Int>,
    modifier: Modifier = Modifier,
) {
    val nonEmpty = bank.filter { (_, qty) -> qty > 0 }.entries.toList()

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
                text = "BANK",
                style = MaterialTheme.typography.labelSmall,
                color = ArteriaPalette.Gold,
            )
            Text(
                text = "${nonEmpty.size} items",
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaPalette.TextMuted,
            )
        }

        if (nonEmpty.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Bank is empty",
                        style = MaterialTheme.typography.titleMedium,
                        color = ArteriaPalette.TextSecondary,
                    )
                    Text(
                        text = "Start training a skill to collect resources",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ArteriaPalette.TextMuted,
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(nonEmpty, key = { it.key }) { (itemId, qty) ->
                    BankSlotCard(itemId = itemId, quantity = qty)
                }
            }
        }
    }
}

@Composable
private fun BankSlotCard(
    itemId: String,
    quantity: Int,
    modifier: Modifier = Modifier,
) {
    val name = SkillDataRegistry.itemName(itemId)
    val nf = NumberFormat.getIntegerInstance()
    val cardShape = RoundedCornerShape(10.dp)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, ArteriaPalette.Border, cardShape),
        shape = cardShape,
        color = ArteriaPalette.BgCard,
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = nf.format(quantity),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = ArteriaPalette.TextPrimary,
                textAlign = TextAlign.Center,
            )
            Text(
                text = name,
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaPalette.TextSecondary,
                textAlign = TextAlign.Center,
                maxLines = 2,
            )
        }
    }
}

