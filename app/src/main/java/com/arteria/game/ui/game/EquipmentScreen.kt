package com.arteria.game.ui.game

import androidx.compose.foundation.background
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arteria.game.core.data.EquipmentRegistry
import com.arteria.game.core.model.EquippedGear
import com.arteria.game.core.model.allEquippedIdsForGameplay
import com.arteria.game.core.model.Equipment
import com.arteria.game.core.model.EquipmentSlot
import com.arteria.game.core.model.EquipmentSlots
import com.arteria.game.ui.theme.ArteriaPalette

// [TRACE: DOCS/SCRATCHPAD.md — expanded equipment silhouette + persistence v6]

@Composable
fun EquipmentScreen(
    equippedGear: EquippedGear,
    playerLevel: Int,
    bank: Map<String, Int>,
    onEquip: (String) -> Unit,
    onUnequip: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val slots = EquipmentSlots.paperDollOrder.map { slot ->
        SlotView(
            slotId = slot.id,
            slotName = slot.displayName,
            equippedId = equippedGear.equippedInUiSlot(slot),
        )
    }
    val equipped = slots.mapNotNull { it.equippedId?.let(EquipmentRegistry::getById) }
    val aggregate = aggregateCombatStats(equipped)

    val groupedOwned = EquipmentRegistry.all
        .filter { (bank[it.id] ?: 0) > 0 }
        .groupBy { it.slot }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ArteriaPalette.BgApp)
            .padding(12.dp),
    ) {
        Text(
            text = "LOADOUT",
            style = MaterialTheme.typography.headlineMedium,
            color = ArteriaPalette.Gold,
        )
        Text(
            text = "Seven-slot silhouette · player level $playerLevel · twin ring pockets.",
            style = MaterialTheme.typography.bodySmall,
            color = ArteriaPalette.TextMuted,
        )
        Spacer(Modifier.height(10.dp))

        PaperDollLite(
            slots = slots,
            onUnequip = onUnequip,
        )

        Spacer(Modifier.height(10.dp))
        AggregateStatsCard(aggregate = aggregate)
        Spacer(Modifier.height(10.dp))

        Text(
            text = "OWNED GEAR",
            style = MaterialTheme.typography.labelSmall,
            color = ArteriaPalette.AccentPrimary,
        )
        Spacer(Modifier.height(6.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            EquipmentSlots.allItemKinds.forEach { slot ->
                val owned = groupedOwned[slot.id].orEmpty().sortedBy { it.levelRequired }
                item(key = "header_${slot.id}") {
                    val headerTitle = when (slot.id) {
                        EquipmentSlots.RING.id -> "RINGS — dual sockets"
                        else -> slot.displayName.uppercase()
                    }
                    Text(
                        text = headerTitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = ArteriaPalette.AccentWeb,
                        modifier = Modifier.padding(start = 2.dp, top = 4.dp),
                    )
                }
                if (owned.isEmpty()) {
                    item(key = "empty_${slot.id}") {
                        Text(
                            text = "No ${slot.displayName.lowercase()} owned yet.",
                            style = MaterialTheme.typography.bodySmall,
                            color = ArteriaPalette.TextMuted,
                            modifier = Modifier.padding(start = 4.dp, bottom = 2.dp),
                        )
                    }
                } else {
                    items(owned, key = { it.id }) { gear ->
                        val qty = bank[gear.id] ?: 0
                        val isEquipped = gear.id in equippedGear.allEquippedIdsForGameplay()
                        val canEquip = playerLevel >= gear.levelRequired
                        GearCard(
                            gear = gear,
                            quantity = qty,
                            isEquipped = isEquipped,
                            canEquip = canEquip,
                            onEquip = { onEquip(gear.id) },
                        )
                    }
                }
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

private data class SlotView(
    val slotId: String,
    val slotName: String,
    val equippedId: String?,
)

private data class AggregateStats(
    val accuracy: Int,
    val maxHit: Int,
    val meleeDefence: Int,
    val attackSpeedBonusMs: Long,
)

@Composable
private fun PaperDollLite(
    slots: List<SlotView>,
    onUnequip: (String) -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = ArteriaPalette.BgCard,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "SILHOUETTE (PAPER DOLL)",
                style = MaterialTheme.typography.labelSmall,
                color = ArteriaPalette.Gold,
            )
            slots.chunked(2).forEach { rowSlots ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    rowSlots.forEach { slot ->
                        val gear = slot.equippedId?.let { EquipmentRegistry.getById(it) }
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            color = ArteriaPalette.BgInput,
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Text(
                                    text = slot.slotName,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ArteriaPalette.TextMuted,
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(26.dp)
                                            .border(
                                                1.dp,
                                                ArteriaPalette.Border,
                                                RoundedCornerShape(6.dp),
                                            ),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(
                                            text = gear?.icon ?: "—",
                                            style = MaterialTheme.typography.bodySmall,
                                        )
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = gear?.name ?: "Empty",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (gear != null) {
                                            ArteriaPalette.TextPrimary
                                        } else {
                                            ArteriaPalette.TextMuted
                                        },
                                        maxLines = 1,
                                    )
                                }
                                if (gear != null) {
                                    TextButton(
                                        onClick = { onUnequip(slot.slotId) },
                                        modifier = Modifier.align(Alignment.End),
                                    ) {
                                        Text("Unequip", color = ArteriaPalette.CombatAccent)
                                    }
                                }
                            }
                        }
                    }
                    if (rowSlots.size == 1) {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun AggregateStatsCard(aggregate: AggregateStats) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = ArteriaPalette.BgCard,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = "LOADOUT COMBAT STATS",
                style = MaterialTheme.typography.labelSmall,
                color = ArteriaPalette.AccentPrimary,
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                StatChip("Accuracy", "+${aggregate.accuracy}")
                StatChip("Max Hit", "+${aggregate.maxHit}")
                StatChip("Melee Def", "+${aggregate.meleeDefence}")
            }
            StatChip(
                label = "Attack Speed",
                value = if (aggregate.attackSpeedBonusMs > 0L) {
                    "-${aggregate.attackSpeedBonusMs} ms"
                } else {
                    "+0 ms"
                },
                centered = false,
            )
        }
    }
}

@Composable
private fun StatChip(label: String, value: String, centered: Boolean = true) {
    Surface(
        color = ArteriaPalette.BgInput,
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalAlignment = if (centered) Alignment.CenterHorizontally else Alignment.Start,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = ArteriaPalette.TextMuted,
                textAlign = if (centered) TextAlign.Center else TextAlign.Start,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = ArteriaPalette.TextPrimary,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun GearCard(
    gear: Equipment,
    quantity: Int,
    isEquipped: Boolean,
    canEquip: Boolean,
    onEquip: () -> Unit,
) {
    val shape = RoundedCornerShape(10.dp)
    Surface(
        shape = shape,
        color = if (isEquipped) ArteriaPalette.BgCardHover else ArteriaPalette.BgCard,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isEquipped) 1.dp else 0.dp,
                color = if (isEquipped) {
                    ArteriaPalette.AccentPrimary.copy(alpha = 0.5f)
                } else {
                    ArteriaPalette.Border
                },
                shape = shape,
            ),
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .border(1.dp, ArteriaPalette.Border, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = gear.icon, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = gear.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = ArteriaPalette.TextPrimary,
                )
                Text(
                    text = gear.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.TextMuted,
                )
                Text(
                    text = "Owned: $quantity · Req Lv ${gear.levelRequired}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (canEquip) ArteriaPalette.TextSecondary else ArteriaPalette.CombatAccent,
                )
                val skillLine = gear.skillBoosts.entries.joinToString(", ") { (skill, boost) ->
                    "+${(boost * 100).toInt()}% ${skill.displayName}"
                }
                if (skillLine.isNotBlank()) {
                    Text(
                        text = skillLine,
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaPalette.BalancedEnd,
                    )
                }
                if (gear.globalXpMultiplier > 1.0) {
                    Text(
                        text = "+${((gear.globalXpMultiplier - 1.0) * 100).toInt()}% All XP",
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaPalette.Gold,
                    )
                }
                val cs = gear.combatStats
                if (cs.accuracy != 0 || cs.maxHit != 0 || cs.meleeDefence != 0 || cs.attackSpeedBonusMs != 0L) {
                    Text(
                        text = "Combat: +${cs.accuracy} acc, +${cs.maxHit} hit, +${cs.meleeDefence} def, " +
                            "-${cs.attackSpeedBonusMs}ms swing",
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaPalette.AccentPrimary,
                    )
                }
            }
            if (isEquipped) {
                Text(
                    text = "EQUIPPED",
                    style = MaterialTheme.typography.labelSmall,
                    color = ArteriaPalette.AccentPrimary,
                    fontWeight = FontWeight.Bold,
                )
            } else {
                TextButton(onClick = onEquip, enabled = canEquip && quantity > 0) {
                    Text("Equip")
                }
            }
        }
    }
}

private fun EquippedGear.equippedInUiSlot(slot: EquipmentSlot): String? =
    when (slot.id) {
        EquipmentSlots.WEAPON.id -> weapon
        EquipmentSlots.HEAD.id -> head
        EquipmentSlots.TOOL.id -> tool
        EquipmentSlots.ARMOR.id -> armor
        EquipmentSlots.ACCESSORY.id -> accessory
        EquipmentSlots.RING.id -> ring
        EquipmentSlots.RING2.id -> ring2
        else -> null
    }

private fun aggregateCombatStats(equipped: List<Equipment>): AggregateStats {
    var acc = 0
    var hit = 0
    var def = 0
    var speed = 0L
    equipped.forEach { gear ->
        acc += gear.combatStats.accuracy
        hit += gear.combatStats.maxHit
        def += gear.combatStats.meleeDefence
        speed += gear.combatStats.attackSpeedBonusMs
    }
    return AggregateStats(
        accuracy = acc,
        maxHit = hit,
        meleeDefence = def,
        attackSpeedBonusMs = speed,
    )
}
