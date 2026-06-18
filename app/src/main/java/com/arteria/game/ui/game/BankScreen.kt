package com.arteria.game.ui.game

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.arteria.game.core.data.BankCategoryRules
import com.arteria.game.core.data.BankItemCategory
import com.arteria.game.core.data.ItemUsageIndex
import com.arteria.game.core.data.SkillDataRegistry
import com.arteria.game.core.model.SkillState
import com.arteria.game.core.skill.SkillId
import com.arteria.game.ui.theme.ArteriaPalette
import java.text.NumberFormat

enum class BankSortMode {
    NAME_ASC,
    NAME_DESC,
    QUANTITY_DESC,
    QUANTITY_ASC,
}

private enum class BankViewMode { GROUPED, FLAT }

@Composable
fun BankScreen(
    bank: Map<String, Int>,
    skills: Map<SkillId, SkillState> = emptyMap(),
    modifier: Modifier = Modifier,
) {
    var searchQuery by remember { mutableStateOf("") }
    var sortMode by remember { mutableStateOf(BankSortMode.QUANTITY_DESC) }
    var viewMode by remember { mutableStateOf(BankViewMode.GROUPED) }
    var showSortMenu by remember { mutableStateOf(false) }
    var withdrawTarget by remember { mutableStateOf<String?>(null) }
    var detailItemId by remember { mutableStateOf<String?>(null) }
    var collapsedCategories by remember { mutableStateOf(setOf<BankItemCategory>()) }

    val nf = NumberFormat.getIntegerInstance()
    val skillLevels = remember(skills) { ItemUsageIndex.skillLevelsFromXp(skills) }
    val craftableInputs = remember(bank, skillLevels) {
        ItemUsageIndex.craftableInputItemIds(bank, skillLevels)
    }

    val filteredAndSorted = remember(bank, searchQuery, sortMode) {
        val nonEmpty = bank.filter { (_, qty) -> qty > 0 }.entries.toList()
        val filtered = if (searchQuery.isBlank()) {
            nonEmpty
        } else {
            nonEmpty.filter { (itemId) ->
                SkillDataRegistry.itemName(itemId).contains(searchQuery, ignoreCase = true)
            }
        }
        filtered.sortedWith { a, b ->
            when (sortMode) {
                BankSortMode.NAME_ASC ->
                    SkillDataRegistry.itemName(a.key).compareTo(SkillDataRegistry.itemName(b.key))
                BankSortMode.NAME_DESC ->
                    SkillDataRegistry.itemName(b.key).compareTo(SkillDataRegistry.itemName(a.key))
                BankSortMode.QUANTITY_DESC -> b.value.compareTo(a.value)
                BankSortMode.QUANTITY_ASC -> a.value.compareTo(b.value)
            }
        }
    }

    val grouped = remember(filteredAndSorted) {
        BankCategoryRules.groupItems(filteredAndSorted.map { it.key })
            .mapValues { (_, ids) ->
                filteredAndSorted.filter { it.key in ids }
            }
            .filterValues { it.isNotEmpty() }
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
            Text("BANK", style = MaterialTheme.typography.labelSmall, color = ArteriaPalette.Gold)
            Text(
                "${filteredAndSorted.size} / ${bank.values.count { it > 0 }} items",
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaPalette.TextMuted,
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 8.dp)) {
            BankViewChip("Grouped", viewMode == BankViewMode.GROUPED) { viewMode = BankViewMode.GROUPED }
            BankViewChip("Flat", viewMode == BankViewMode.FLAT) { viewMode = BankViewMode.FLAT }
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search items...", color = ArteriaPalette.TextMuted) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = ArteriaPalette.TextMuted)
                },
                trailingIcon = {
                    Row {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, "Clear search", tint = ArteriaPalette.TextMuted)
                            }
                        }
                        IconButton(onClick = { showSortMenu = true }) {
                            Text(
                                when (sortMode) {
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
            SortDropdown(showSortMenu, { showSortMenu = false }) { sortMode = it; showSortMenu = false }
        }

        Spacer(Modifier.height(8.dp))

        if (filteredAndSorted.isEmpty()) {
            BankEmptyState(searchQuery)
        } else when (viewMode) {
            BankViewMode.FLAT -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filteredAndSorted.chunked(3), key = { row -> row.first().key }) { row ->
                        BankItemRow(row, nf, craftableInputs) { detailItemId = it }
                    }
                }
            }
            BankViewMode.GROUPED -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    grouped.forEach { (category, entries) ->
                        item(key = "header_${category.name}") {
                            BankCategoryHeader(
                                category = category,
                                count = entries.size,
                                collapsed = category in collapsedCategories,
                                onToggle = {
                                    collapsedCategories = if (category in collapsedCategories) {
                                        collapsedCategories - category
                                    } else {
                                        collapsedCategories + category
                                    }
                                },
                            )
                        }
                        if (category !in collapsedCategories) {
                            items(entries.chunked(3), key = { row -> "${category.name}_${row.first().key}" }) { row ->
                                BankItemRow(row, nf, craftableInputs) { detailItemId = it }
                            }
                        }
                    }
                }
            }
        }
    }

    detailItemId?.let { itemId ->
        BankItemDetailDialog(
            itemId = itemId,
            quantity = bank[itemId] ?: 0,
            isCraftableInput = itemId in craftableInputs,
            onDismiss = { detailItemId = null },
            onWithdraw = {
                detailItemId = null
                withdrawTarget = itemId
            },
        )
    }

    withdrawTarget?.let { itemId ->
        WithdrawDialog(
            itemId = itemId,
            quantity = bank[itemId] ?: 0,
            onDismiss = { withdrawTarget = null },
            onWithdraw = { withdrawTarget = null },
        )
    }
}

@Composable
private fun BankViewChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Surface(
        shape = shape,
        color = if (selected) ArteriaPalette.AccentPrimary.copy(alpha = 0.2f) else ArteriaPalette.BgCard,
        modifier = Modifier
            .border(
                1.dp,
                if (selected) ArteriaPalette.AccentPrimary else ArteriaPalette.Border,
                shape,
            )
            .clickable(onClick = onClick),
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) ArteriaPalette.AccentPrimary else ArteriaPalette.TextSecondary,
        )
    }
}

@Composable
private fun BankCategoryHeader(
    category: BankItemCategory,
    count: Int,
    collapsed: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            category.displayName.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = ArteriaPalette.Gold,
        )
        Text(
            "${if (collapsed) "+" else "−"}  $count",
            style = MaterialTheme.typography.bodySmall,
            color = ArteriaPalette.TextMuted,
        )
    }
}

@Composable
private fun BankItemRow(
    row: List<Map.Entry<String, Int>>,
    nf: NumberFormat,
    craftableInputs: Set<String>,
    onOpenDetail: (String) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        row.forEach { (itemId, qty) ->
            BankSlotCard(
                itemId = itemId,
                quantity = qty,
                nf = nf,
                isCraftableInput = itemId in craftableInputs,
                onOpenDetail = { onOpenDetail(itemId) },
                modifier = Modifier.weight(1f),
            )
        }
        repeat(3 - row.size) {
            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun BankEmptyState(searchQuery: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                if (searchQuery.isNotEmpty()) "No items match \"$searchQuery\"" else "Bank is empty",
                style = MaterialTheme.typography.titleMedium,
                color = ArteriaPalette.TextSecondary,
            )
            Text(
                if (searchQuery.isNotEmpty()) "Try a different search"
                else "Start training a skill to collect resources",
                style = MaterialTheme.typography.bodyMedium,
                color = ArteriaPalette.TextMuted,
            )
        }
    }
}

@Composable
private fun SortDropdown(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onSelect: (BankSortMode) -> Unit,
) {
    DropdownMenu(expanded = expanded, onDismissRequest = onDismiss, containerColor = ArteriaPalette.BgCard) {
        listOf(
            BankSortMode.NAME_ASC to "Name A→Z",
            BankSortMode.NAME_DESC to "Name Z→A",
            BankSortMode.QUANTITY_DESC to "Quantity ↓",
            BankSortMode.QUANTITY_ASC to "Quantity ↑",
        ).forEach { (mode, label) ->
            DropdownMenuItem(
                text = { Text(label, color = ArteriaPalette.TextPrimary) },
                onClick = { onSelect(mode) },
            )
        }
    }
}

@Composable
private fun BankSlotCard(
    itemId: String,
    quantity: Int,
    nf: NumberFormat,
    isCraftableInput: Boolean,
    onOpenDetail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val name = SkillDataRegistry.itemName(itemId)
    val cardShape = RoundedCornerShape(10.dp)
    val borderColor = if (isCraftableInput) {
        ArteriaPalette.Gold.copy(alpha = 0.55f)
    } else {
        ArteriaPalette.Border
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, cardShape)
            .clickable(onClick = onOpenDetail),
        shape = cardShape,
        color = if (isCraftableInput) {
            ArteriaPalette.BgCardHover.copy(alpha = 0.35f)
        } else {
            ArteriaPalette.BgCard
        },
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (isCraftableInput) {
                Text(
                    "Craft now",
                    style = MaterialTheme.typography.labelSmall,
                    color = ArteriaPalette.Gold,
                )
            }
            Text(
                nf.format(quantity),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = ArteriaPalette.TextPrimary,
                textAlign = TextAlign.Center,
            )
            Text(
                name,
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaPalette.TextSecondary,
                textAlign = TextAlign.Center,
                maxLines = 2,
            )
        }
    }
}

@Composable
private fun BankItemDetailDialog(
    itemId: String,
    quantity: Int,
    isCraftableInput: Boolean,
    onDismiss: () -> Unit,
    onWithdraw: () -> Unit,
) {
    val nf = NumberFormat.getIntegerInstance()
    val name = SkillDataRegistry.itemName(itemId)
    val def = SkillDataRegistry.itemRegistry[itemId]
    val usage = remember(itemId) { ItemUsageIndex.info(itemId) }

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(16.dp), color = ArteriaPalette.BgCard) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(name, style = MaterialTheme.typography.titleLarge, color = ArteriaPalette.TextPrimary)
                Text(
                    "Owned: ${nf.format(quantity)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ArteriaPalette.TextSecondary,
                )
                def?.description?.let { desc ->
                    Spacer(Modifier.height(8.dp))
                    Text(desc, style = MaterialTheme.typography.bodySmall, color = ArteriaPalette.TextMuted)
                }
                if (isCraftableInput) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "You can start a recipe that uses this item right now.",
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaPalette.Gold,
                    )
                }
                if (usage.producedBy.isNotEmpty()) {
                    Spacer(Modifier.height(12.dp))
                    Text("Produced by", style = MaterialTheme.typography.labelSmall, color = ArteriaPalette.Gold)
                    usage.producedBy.take(6).forEach { ref ->
                        Text(
                            "• ${ref.skillId.displayName} — ${ref.actionName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = ArteriaPalette.TextSecondary,
                        )
                    }
                }
                if (usage.consumedBy.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text("Used in", style = MaterialTheme.typography.labelSmall, color = ArteriaPalette.Gold)
                    usage.consumedBy.take(6).forEach { ref ->
                        Text(
                            "• ${ref.skillId.displayName} — ${ref.actionName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = ArteriaPalette.TextSecondary,
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Close")
                    }
                    OutlinedButton(onClick = onWithdraw, modifier = Modifier.weight(1f)) {
                        Text("Withdraw")
                    }
                }
            }
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
        Surface(shape = RoundedCornerShape(16.dp), color = ArteriaPalette.BgCard) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Withdraw $name", style = MaterialTheme.typography.titleMedium, color = ArteriaPalette.TextPrimary)
                Text(
                    "Available: ${nf.format(quantity)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.TextMuted,
                )
                Spacer(Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(1, 5, 10, quantity).distinct().forEach { amount ->
                        OutlinedButton(
                            onClick = { onWithdraw(amount) },
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(
                                if (amount == quantity && quantity !in listOf(1, 5, 10)) "All"
                                else "$amount",
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    androidx.compose.material3.TextButton(onClick = onDismiss) { Text("Cancel") }
                }
            }
        }
    }
}
