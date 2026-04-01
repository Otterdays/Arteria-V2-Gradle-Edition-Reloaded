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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arteria.game.core.data.EquipmentRegistry
import com.arteria.game.core.model.EquippedGear
import com.arteria.game.core.model.Equipment
import com.arteria.game.core.model.EquipmentSlots
import com.arteria.game.core.skill.SkillId
import com.arteria.game.core.skill.XPTable
import com.arteria.game.ui.theme.ArteriaPalette
import java.text.NumberFormat

@Composable
fun EquipmentScreen(
    equippedGear: EquippedGear,
    playerLevel: Int,
    bank: Map<String, Int>,
    onEquip: (String) -> Unit,
    onUnequip: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val nf = NumberFormat.getIntegerInstance()
    var selectedSlot by remember { mutableStateOf<String?>(null) }

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
                text = "EQUIPMENT",
                style = MaterialTheme.typography.labelSmall,
                color = ArteriaPalette.Gold,
            )
            Text(
                text = "Player Level $playerLevel",
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaPalette.TextMuted,
            )
        }

        // Equipped gear slots
        Text(
            text = "EQUIPPED",
            style = MaterialTheme.typography.labelSmall,
            color = ArteriaPalette.AccentPrimary,
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp),
        )

        EquipmentSlots.all.forEach { slot ->
            val equippedId = when (slot.id) {
                EquipmentSlots.WEAPON.id -> equippedGear.weapon
                EquipmentSlots.TOOL.id -> equippedGear.tool
                EquipmentSlots.ARMOR.id -> equippedGear.armor
                EquipmentSlots.ACCESSORY.id -> equippedGear.accessory
                else -> null
            }

            EquippedSlotCard(
                slot = slot,
                equippedId = equippedId,
                onUnequip = { onUnequip(slot.id) },
                onSelect = { selectedSlot = slot.id },
            )
        }

        Spacer(Modifier.height(12.dp))

        // Available equipment for selected slot
        val slotToShow = selectedSlot
        if (slotToShow != null) {
            val slotDef = EquipmentSlots.all.find { it.id == slotToShow }
            if (slotDef != null) {
                Text(
                    text = "${slotDef.displayName.uppercase()} — TAP TO EQUIP",
                    style = MaterialTheme.typography.labelSmall,
                    color = ArteriaPalette.AccentWeb,
                    modifier = Modifier.padding(start = 4.dp, bottom = 6.dp),
                )

                val available = EquipmentRegistry.getBySlot(slotToShow)
                    .filter { it.levelRequired <= playerLevel }

                if (available.isEmpty()) {
                    Text(
                        text = "No equipment available for this slot",
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaPalette.TextMuted,
                        modifier = Modifier.padding(start = 4.dp),
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(available, key = { it.id }) { equipment ->
                            val isEquipped = when (slotToShow) {
                                EquipmentSlots.WEAPON.id -> equippedGear.weapon == equipment.id
                                EquipmentSlots.TOOL.id -> equippedGear.tool == equipment.id
                                EquipmentSlots.ARMOR.id -> equippedGear.armor == equipment.id
                                EquipmentSlots.ACCESSORY.id -> equippedGear.accessory == equipment.id
                                else -> false
                            }
                            EquipmentCard(
                                equipment = equipment,
                                isEquipped = isEquipped,
                                nf = nf,
                                onEquip = { onEquip(equipment.id) },
                            )
                        }
                    }
                }
            }
        } else {
            Text(
                text = "Tap a slot above to browse equipment",
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaPalette.TextMuted,
                modifier = Modifier.padding(start = 4.dp, top = 8.dp),
            )
        }
    }
}

@Composable
private fun EquippedSlotCard(
    slot: com.arteria.game.core.model.EquipmentSlot,
    equippedId: String?,
    onUnequip: () -> Unit,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(10.dp)
    val equipment = equippedId?.let { EquipmentRegistry.getById(it) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        shape = cardShape,
        color = ArteriaPalette.BgCard,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(1.dp, ArteriaPalette.Border, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = equipment?.icon ?: "—",
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = slot.displayName,
                    style = MaterialTheme.typography.labelSmall,
                    color = ArteriaPalette.TextMuted,
                )
                Text(
                    text = equipment?.name ?: "Empty",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (equipment != null) ArteriaPalette.TextPrimary else ArteriaPalette.TextMuted,
                )
                if (equipment != null && equipment.skillBoosts.isNotEmpty()) {
                    Text(
                        text = equipment.skillBoosts.entries.joinToString(", ") { (skill, boost) ->
                            "+${(boost * 100).toInt()}% ${skill.displayName}"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaPalette.BalancedEnd,
                    )
                }
                if (equipment != null && equipment.globalXpMultiplier > 1.0) {
                    Text(
                        text = "+${((equipment.globalXpMultiplier - 1) * 100).toInt()}% All XP",
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaPalette.Gold,
                    )
                }
            }

            if (equipment != null) {
                androidx.compose.material3.TextButton(onClick = onUnequip) {
                    Text("Unequip", color = ArteriaPalette.CombatAccent)
                }
            }
        }
    }
}

@Composable
private fun EquipmentCard(
    equipment: Equipment,
    isEquipped: Boolean,
    nf: NumberFormat,
    onEquip: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(10.dp)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = if (isEquipped) 1.dp else 0.dp,
                color = if (isEquipped) ArteriaPalette.AccentPrimary.copy(alpha = 0.4f) else ArteriaPalette.Border,
                shape = cardShape,
            ),
        shape = cardShape,
        color = if (isEquipped) ArteriaPalette.BgCardHover else ArteriaPalette.BgCard,
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
                    text = equipment.icon,
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = equipment.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = ArteriaPalette.TextPrimary,
                )
                Text(
                    text = equipment.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.TextMuted,
                )

                if (equipment.skillBoosts.isNotEmpty()) {
                    Text(
                        text = equipment.skillBoosts.entries.joinToString(", ") { (skill: SkillId, boost: Double) ->
                            "+${(boost * 100).toInt()}% ${skill.displayName}"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaPalette.BalancedEnd,
                    )
                }
                if (equipment.globalXpMultiplier > 1.0) {
                    Text(
                        text = "+${((equipment.globalXpMultiplier - 1) * 100).toInt()}% All XP",
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaPalette.Gold,
                    )
                }

                Text(
                    text = "Requires Level ${equipment.levelRequired}",
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.TextMuted,
                )
            }

            if (isEquipped) {
                Text(
                    text = "EQUIPPED",
                    style = MaterialTheme.typography.labelSmall,
                    color = ArteriaPalette.AccentPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            } else {
                androidx.compose.material3.TextButton(onClick = onEquip) {
                    Text("Equip")
                }
            }
        }
    }
}
