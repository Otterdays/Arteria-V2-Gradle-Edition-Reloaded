package com.arteria.game.ui.game

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arteria.game.core.data.SkillDataRegistry
import com.arteria.game.ui.theme.ArteriaPalette
import java.text.NumberFormat

enum class BankSortMode {
    NAME_ASC,
    NAME_DESC,
    QUANTITY_DESC,
    QUANTITY_ASC,
}

@Composable
fun BankScreen(
    bank: Map<String, Int>,
    modifier: Modifier = Modifier,
) {
    var searchQuery by remember { mutableStateOf("") }
    var sortMode by remember { mutableStateOf(BankSortMode.QUANTITY_DESC) }
    var showSortMenu by remember { mutableStateOf(false) }
    var withdrawTarget by remember { mutableStateOf<String?>(null) }

    val nf = NumberFormat.getIntegerInstance()

    val filteredAndSorted = remember(bank, searchQuery, sortMode) {
        val nonEmpty = bank.filter { (_, qty) -> qty > 0 }.entries.toList()
        val filtered = if (searchQuery.isBlank()) {
            nonEmpty
        } else {
            nonEmpty.filter { (itemId) ->
                SkillDataRegistry.itemName(itemId)
                    .contains(searchQuery, ignoreCase = true)
            }
        }
        filtered.sortedWith { a, b ->
            when (sortMode) {
                BankSortMode.NAME_ASC ->
                    SkillDataRegistry.itemName(a.key)
                        .compareTo(SkillDataRegistry.itemName(b.key))
                BankSortMode.NAME_DESC ->
                    SkillDataRegistry.itemName(b.key)
                        .compareTo(SkillDataRegistry.itemName(a.key))
                BankSortMode.QUANTITY_DESC -> b.value.compareTo(a.value)
                BankSortMode.QUANTITY_ASC -> a.value.compareTo(b.value)
            }
        }
    }

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
                text = "${filteredAndSorted.size} / ${bank.values.count { it > 0 }} items",
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaPalette.TextMuted,
            )
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Search items...", color = ArteriaPalette.TextMuted)
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = ArteriaPalette.TextMuted,
                    )
                },
                trailingIcon = {
                    Row {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Clear search",
                                    tint = ArteriaPalette.TextMuted,
                                )
                            }
                        }
                        IconButton(onClick = { showSortMenu = true }) {
                            Text(
                                text = when (sortMode) {
                                    BankSortMode.NAME_ASC -> "A→Z"
                                    BankSortMode.NAME_DESC -> "Z→A"
                                    BankSortMode.QUANTITY_DESC -> "Qty↓"
                                    BankSortMode.QUANTITY_ASC -> "Qty↑"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = ArteriaPalette.AccentPrimary,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = ArteriaPalette.BgCard,
                    unfocusedContainerColor = ArteriaPalette.BgCard,
                    focusedIndicatorColor = ArteriaPalette.AccentPrimary,
                    unfocusedIndicatorColor = ArteriaPalette.Border,
                    focusedTextColor = ArteriaPalette.TextPrimary,
                    unfocusedTextColor = ArteriaPalette.TextPrimary,
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {}),
            )

            DropdownMenu(
                expanded = showSortMenu,
                onDismissRequest = { showSortMenu = false },
                containerColor = ArteriaPalette.BgCard,
            ) {
                DropdownMenuItem(
                    text = { Text("Name A→Z", color = ArteriaPalette.TextPrimary) },
                    onClick = {
                        sortMode = BankSortMode.NAME_ASC
                        showSortMenu = false
                    },
                )
                DropdownMenuItem(
                    text = { Text("Name Z→A", color = ArteriaPalette.TextPrimary) },
                    onClick = {
                        sortMode = BankSortMode.NAME_DESC
                        showSortMenu = false
                    },
                )
                DropdownMenuItem(
                    text = { Text("Quantity ↓", color = ArteriaPalette.TextPrimary) },
                    onClick = {
                        sortMode = BankSortMode.QUANTITY_DESC
                        showSortMenu = false
                    },
                )
                DropdownMenuItem(
                    text = { Text("Quantity ↑", color = ArteriaPalette.TextPrimary) },
                    onClick = {
                        sortMode = BankSortMode.QUANTITY_ASC
                        showSortMenu = false
                    },
                )
            }
        }

        if (filteredAndSorted.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (searchQuery.isNotEmpty()) "No items match \"$searchQuery\""
                        else "Bank is empty",
                        style = MaterialTheme.typography.titleMedium,
                        color = ArteriaPalette.TextSecondary,
                    )
                    Text(
                        text = if (searchQuery.isNotEmpty()) "Try a different search"
                        else "Start training a skill to collect resources",
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
                items(filteredAndSorted, key = { it.key }) { (itemId, qty) ->
                    BankSlotCard(
                        itemId = itemId,
                        quantity = qty,
                        nf = nf,
                        onWithdraw = { withdrawTarget = itemId },
                    )
                }
            }
        }
    }

    withdrawTarget?.let { itemId ->
        WithdrawDialog(
            itemId = itemId,
            quantity = bank[itemId] ?: 0,
            onDismiss = { withdrawTarget = null },
            onWithdraw = { amount ->
                // TODO: wire to bank withdrawal logic
                withdrawTarget = null
            },
        )
    }
}

@Composable
private fun BankSlotCard(
    itemId: String,
    quantity: Int,
    nf: NumberFormat,
    onWithdraw: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val name = SkillDataRegistry.itemName(itemId)
    val cardShape = RoundedCornerShape(10.dp)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, ArteriaPalette.Border, cardShape)
            .clickable(onClick = onWithdraw),
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

@Composable
private fun WithdrawDialog(
    itemId: String,
    quantity: Int,
    onDismiss: () -> Unit,
    onWithdraw: (Int) -> Unit,
) {
    val nf = NumberFormat.getIntegerInstance()
    val name = SkillDataRegistry.itemName(itemId)

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = ArteriaPalette.BgCard,
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
            ) {
                Text(
                    text = "Withdraw $name",
                    style = MaterialTheme.typography.titleMedium,
                    color = ArteriaPalette.TextPrimary,
                )
                Text(
                    text = "Available: ${nf.format(quantity)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.TextMuted,
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    listOf(1, 5, 10, quantity).distinct().forEach { amount ->
                        OutlinedButton(
                            onClick = { onWithdraw(amount) },
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(
                                if (amount == quantity && quantity != 10 && quantity != 5 && quantity != 1) "All"
                                else "$amount",
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    androidx.compose.material3.TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}
