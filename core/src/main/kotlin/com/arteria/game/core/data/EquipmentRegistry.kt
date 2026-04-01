package com.arteria.game.core.data

import com.arteria.game.core.model.Equipment
import com.arteria.game.core.model.EquipmentSlots
import com.arteria.game.core.skill.SkillId

object EquipmentRegistry {

    val all: List<Equipment> = listOf(
        // Tools
        Equipment(
            id = "bronze_pickaxe",
            name = "Bronze Pickaxe",
            slot = EquipmentSlots.TOOL.id,
            description = "A sturdy bronze pickaxe for mining",
            icon = "⛏️",
            skillBoosts = mapOf(SkillId.MINING to 0.05),
            levelRequired = 1,
            value = 10,
        ),
        Equipment(
            id = "iron_pickaxe",
            name = "Iron Pickaxe",
            slot = EquipmentSlots.TOOL.id,
            description = "An iron pickaxe, better than bronze",
            icon = "⛏️",
            skillBoosts = mapOf(SkillId.MINING to 0.10),
            levelRequired = 10,
            value = 50,
        ),
        Equipment(
            id = "steel_pickaxe",
            name = "Steel Pickaxe",
            slot = EquipmentSlots.TOOL.id,
            description = "A steel pickaxe for serious miners",
            icon = "⛏️",
            skillBoosts = mapOf(SkillId.MINING to 0.15),
            levelRequired = 20,
            value = 150,
        ),
        Equipment(
            id = "bronze_axe",
            name = "Bronze Axe",
            slot = EquipmentSlots.TOOL.id,
            description = "A bronze axe for logging",
            icon = "🪓",
            skillBoosts = mapOf(SkillId.LOGGING to 0.05),
            levelRequired = 1,
            value = 10,
        ),
        Equipment(
            id = "iron_axe",
            name = "Iron Axe",
            slot = EquipmentSlots.TOOL.id,
            description = "An iron axe for efficient logging",
            icon = "🪓",
            skillBoosts = mapOf(SkillId.LOGGING to 0.10),
            levelRequired = 10,
            value = 50,
        ),
        Equipment(
            id = "bronze_harpoon",
            name = "Bronze Harpoon",
            slot = EquipmentSlots.TOOL.id,
            description = "A bronze-tipped harpoon for fishing",
            icon = "🎣",
            skillBoosts = mapOf(SkillId.FISHING to 0.05),
            levelRequired = 1,
            value = 10,
        ),
        Equipment(
            id = "iron_harpoon",
            name = "Iron Harpoon",
            slot = EquipmentSlots.TOOL.id,
            description = "An iron harpoon for better catches",
            icon = "🎣",
            skillBoosts = mapOf(SkillId.FISHING to 0.10),
            levelRequired = 10,
            value = 50,
        ),
        Equipment(
            id = "herbalist_gloves",
            name = "Herbalist Gloves",
            slot = EquipmentSlots.TOOL.id,
            description = "Gloves that enhance harvesting",
            icon = "🧤",
            skillBoosts = mapOf(SkillId.HARVESTING to 0.10),
            levelRequired = 5,
            value = 30,
        ),
        Equipment(
            id = "scavenger_bag",
            name = "Scavenger Bag",
            slot = EquipmentSlots.TOOL.id,
            description = "A bag with extra pockets for salvage",
            icon = "🎒",
            skillBoosts = mapOf(SkillId.SCAVENGING to 0.10),
            levelRequired = 5,
            value = 30,
        ),

        // Weapons
        Equipment(
            id = "bronze_sword",
            name = "Bronze Sword",
            slot = EquipmentSlots.WEAPON.id,
            description = "A basic bronze sword",
            icon = "⚔️",
            skillBoosts = mapOf(SkillId.ATTACK to 0.05, SkillId.STRENGTH to 0.03),
            levelRequired = 1,
            value = 15,
        ),
        Equipment(
            id = "iron_sword",
            name = "Iron Sword",
            slot = EquipmentSlots.WEAPON.id,
            description = "A sharp iron sword",
            icon = "⚔️",
            skillBoosts = mapOf(SkillId.ATTACK to 0.10, SkillId.STRENGTH to 0.05),
            levelRequired = 10,
            value = 75,
        ),
        Equipment(
            id = "wooden_staff",
            name = "Wooden Staff",
            slot = EquipmentSlots.WEAPON.id,
            description = "A simple wooden staff for spellcasting",
            icon = "🪄",
            skillBoosts = mapOf(SkillId.SORCERY to 0.05),
            levelRequired = 1,
            value = 15,
        ),
        Equipment(
            id = "oak_staff",
            name = "Oak Staff",
            slot = EquipmentSlots.WEAPON.id,
            description = "An oak staff with better focus",
            icon = "🪄",
            skillBoosts = mapOf(SkillId.SORCERY to 0.10),
            levelRequired = 10,
            value = 75,
        ),

        // Armor
        Equipment(
            id = "leather_armor",
            name = "Leather Armor",
            slot = EquipmentSlots.ARMOR.id,
            description = "Basic leather armor",
            icon = "🛡️",
            skillBoosts = mapOf(SkillId.DEFENCE to 0.05),
            levelRequired = 1,
            value = 20,
        ),
        Equipment(
            id = "iron_armor",
            name = "Iron Armor",
            slot = EquipmentSlots.ARMOR.id,
            description = "Sturdy iron plate armor",
            icon = "🛡️",
            skillBoosts = mapOf(SkillId.DEFENCE to 0.10),
            levelRequired = 10,
            value = 100,
        ),
        Equipment(
            id = "robe_of_wisdom",
            name = "Robe of Wisdom",
            slot = EquipmentSlots.ARMOR.id,
            description = "A robe that enhances magical study",
            icon = "👘",
            skillBoosts = mapOf(SkillId.WIZARDRY to 0.10),
            levelRequired = 10,
            value = 100,
        ),

        // Accessories
        Equipment(
            id = "amulet_of_accuracy",
            name = "Amulet of Accuracy",
            slot = EquipmentSlots.ACCESSORY.id,
            description = "Slightly improves all skill XP",
            icon = "📿",
            skillBoosts = emptyMap(),
            globalXpMultiplier = 1.02,
            levelRequired = 5,
            value = 100,
        ),
        Equipment(
            id = "ring_of_fortune",
            name = "Ring of Fortune",
            slot = EquipmentSlots.ACCESSORY.id,
            description = "Increases chance of rare drops",
            icon = "💍",
            skillBoosts = emptyMap(),
            globalXpMultiplier = 1.05,
            levelRequired = 20,
            value = 500,
        ),
        Equipment(
            id = "explorer_compass",
            name = "Explorer's Compass",
            slot = EquipmentSlots.ACCESSORY.id,
            description = "Guides you to hidden resources",
            icon = "🧭",
            skillBoosts = mapOf(SkillId.EXPLORATION to 0.15),
            levelRequired = 10,
            value = 200,
        ),
    )

    fun getById(id: String): Equipment? = all.find { it.id == id }

    fun getBySlot(slotId: String): List<Equipment> = all.filter { it.slot == slotId }

    fun getAvailableForLevel(level: Int): List<Equipment> = all.filter { it.levelRequired <= level }
}
