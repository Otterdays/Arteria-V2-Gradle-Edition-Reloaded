package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

// NOTE: Fletching converts logs and bars into bows and arrows.
object FletchingData {
    val ARROW_SHAFTS = ItemDef("arrow_shafts", "Arrow Shafts", "Prepared wooden shafts for arrows")
    val BRONZE_ARROWS = ItemDef("bronze_arrows", "Bronze Arrows", "Basic arrows for novice rangers")
    val IRON_ARROWS = ItemDef("iron_arrows", "Iron Arrows", "Reliable arrows with iron heads")
    val STEEL_ARROWS = ItemDef("steel_arrows", "Steel Arrows", "Balanced arrows for serious hunts")
    val SHORTBOW = ItemDef("shortbow", "Shortbow", "A simple short-range bow")
    val OAK_LONGBOW = ItemDef("oak_longbow", "Oak Longbow", "A sturdy longbow made of oak")
    val WILLOW_LONGBOW = ItemDef("willow_longbow", "Willow Longbow", "A flexible longbow for rapid fire")
    val MAPLE_LONGBOW = ItemDef("maple_longbow", "Maple Longbow", "A dense longbow with heavy draw weight")

    val items: Map<String, ItemDef> = listOf(
        ARROW_SHAFTS,
        BRONZE_ARROWS,
        IRON_ARROWS,
        STEEL_ARROWS,
        SHORTBOW,
        OAK_LONGBOW,
        WILLOW_LONGBOW,
        MAPLE_LONGBOW,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction(
            id = "carve_arrow_shafts",
            skillId = SkillId.FLETCHING,
            name = "Carve Arrow Shafts",
            levelRequired = 1,
            xpPerAction = 12.0,
            actionTimeMs = 2200L,
            resourceId = "arrow_shafts",
            resourceAmount = 12,
            inputItems = mapOf("normal_logs" to 1),
        ),
        SkillAction(
            id = "fletch_bronze_arrows",
            skillId = SkillId.FLETCHING,
            name = "Bronze Arrows",
            levelRequired = 8,
            xpPerAction = 24.0,
            actionTimeMs = 2600L,
            resourceId = "bronze_arrows",
            resourceAmount = 10,
            inputItems = mapOf("arrow_shafts" to 10, "bronze_bar" to 1),
        ),
        SkillAction(
            id = "fletch_iron_arrows",
            skillId = SkillId.FLETCHING,
            name = "Iron Arrows",
            levelRequired = 22,
            xpPerAction = 40.0,
            actionTimeMs = 3200L,
            resourceId = "iron_arrows",
            resourceAmount = 10,
            inputItems = mapOf("arrow_shafts" to 10, "iron_bar" to 1),
        ),
        SkillAction(
            id = "fletch_steel_arrows",
            skillId = SkillId.FLETCHING,
            name = "Steel Arrows",
            levelRequired = 36,
            xpPerAction = 62.0,
            actionTimeMs = 3800L,
            resourceId = "steel_arrows",
            resourceAmount = 10,
            inputItems = mapOf("arrow_shafts" to 10, "steel_bar" to 1),
        ),
        SkillAction(
            id = "carve_shortbow",
            skillId = SkillId.FLETCHING,
            name = "Carve Shortbow",
            levelRequired = 45,
            xpPerAction = 88.0,
            actionTimeMs = 4600L,
            resourceId = "shortbow",
            inputItems = mapOf("oak_logs" to 1),
        ),
        SkillAction(
            id = "carve_oak_longbow",
            skillId = SkillId.FLETCHING,
            name = "Oak Longbow",
            levelRequired = 56,
            xpPerAction = 118.0,
            actionTimeMs = 5600L,
            resourceId = "oak_longbow",
            inputItems = mapOf("oak_logs" to 2),
        ),
        SkillAction(
            id = "carve_willow_longbow",
            skillId = SkillId.FLETCHING,
            name = "Willow Longbow",
            levelRequired = 70,
            xpPerAction = 165.0,
            actionTimeMs = 6600L,
            resourceId = "willow_longbow",
            inputItems = mapOf("willow_logs" to 2),
        ),
        SkillAction(
            id = "carve_maple_longbow",
            skillId = SkillId.FLETCHING,
            name = "Maple Longbow",
            levelRequired = 84,
            xpPerAction = 235.0,
            actionTimeMs = 7800L,
            resourceId = "maple_longbow",
            inputItems = mapOf("maple_logs" to 2),
        ),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
