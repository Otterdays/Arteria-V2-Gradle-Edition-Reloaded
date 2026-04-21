package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

object ForgingData {
    // We don't define new items here, we mostly produce items from EquipmentRegistry
    // But we might need placeholders for some refined materials if needed.
    
    val items: Map<String, ItemDef> = emptyMap()

    val actions: List<SkillAction> = listOf(
        // Bronze Tier
        SkillAction(
            id = "forge_bronze_pickaxe", skillId = SkillId.FORGING, name = "Bronze Pickaxe",
            levelRequired = 1, xpPerAction = 15.0, actionTimeMs = 4000L,
            resourceId = "bronze_pickaxe",
            inputItems = mapOf("bronze_bar" to 2),
        ),
        SkillAction(
            id = "forge_bronze_axe", skillId = SkillId.FORGING, name = "Bronze Axe",
            levelRequired = 1, xpPerAction = 15.0, actionTimeMs = 4000L,
            resourceId = "bronze_axe",
            inputItems = mapOf("bronze_bar" to 2),
        ),
        SkillAction(
            id = "forge_bronze_sword", skillId = SkillId.FORGING, name = "Bronze Sword",
            levelRequired = 1, xpPerAction = 20.0, actionTimeMs = 5000L,
            resourceId = "bronze_sword",
            inputItems = mapOf("bronze_bar" to 3),
        ),

        // Iron Tier
        SkillAction(
            id = "forge_iron_pickaxe", skillId = SkillId.FORGING, name = "Iron Pickaxe",
            levelRequired = 10, xpPerAction = 30.0, actionTimeMs = 6000L,
            resourceId = "iron_pickaxe",
            inputItems = mapOf("iron_bar" to 2),
        ),
        SkillAction(
            id = "forge_iron_axe", skillId = SkillId.FORGING, name = "Iron Axe",
            levelRequired = 10, xpPerAction = 30.0, actionTimeMs = 6000L,
            resourceId = "iron_axe",
            inputItems = mapOf("iron_bar" to 2),
        ),
        SkillAction(
            id = "forge_iron_sword", skillId = SkillId.FORGING, name = "Iron Sword",
            levelRequired = 15, xpPerAction = 50.0, actionTimeMs = 8000L,
            resourceId = "iron_sword",
            inputItems = mapOf("iron_bar" to 3),
        ),
        SkillAction(
            id = "forge_iron_armor", skillId = SkillId.FORGING, name = "Iron Armor",
            levelRequired = 20, xpPerAction = 75.0, actionTimeMs = 10000L,
            resourceId = "iron_armor",
            inputItems = mapOf("iron_bar" to 5),
        ),

        // Steel Tier
        SkillAction(
            id = "forge_steel_pickaxe", skillId = SkillId.FORGING, name = "Steel Pickaxe",
            levelRequired = 30, xpPerAction = 100.0, actionTimeMs = 12000L,
            resourceId = "steel_pickaxe",
            inputItems = mapOf("steel_bar" to 3),
        ),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
