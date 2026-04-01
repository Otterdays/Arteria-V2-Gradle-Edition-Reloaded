package com.arteria.game.core.model

import com.arteria.game.core.skill.SkillId

data class EquipmentSlot(
    val id: String,
    val displayName: String,
    val description: String,
)

data class Equipment(
    val id: String,
    val name: String,
    val slot: String,
    val description: String,
    val icon: String,
    val skillBoosts: Map<SkillId, Double>,
    val globalXpMultiplier: Double = 1.0,
    val levelRequired: Int = 1,
    val value: Int = 0,
)

data class EquippedGear(
    val weapon: String? = null,
    val tool: String? = null,
    val armor: String? = null,
    val accessory: String? = null,
)

object EquipmentSlots {
    val WEAPON = EquipmentSlot("weapon", "Weapon", "Increases combat effectiveness")
    val TOOL = EquipmentSlot("tool", "Tool", "Boosts gathering and crafting speed")
    val ARMOR = EquipmentSlot("armor", "Armor", "Provides defensive bonuses")
    val ACCESSORY = EquipmentSlot("accessory", "Accessory", "Special passive bonuses")

    val all = listOf(WEAPON, TOOL, ARMOR, ACCESSORY)
}
