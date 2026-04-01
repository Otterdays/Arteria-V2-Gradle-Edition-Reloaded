package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

object SmithingData {
    val BRONZE_BAR   = ItemDef("bronze_bar",    "Bronze Bar",    "Smelted copper and tin alloy")
    val IRON_BAR     = ItemDef("iron_bar",      "Iron Bar",      "Refined iron, ready for forging")
    val STEEL_BAR    = ItemDef("steel_bar",     "Steel Bar",     "Iron hardened with coal")
    val GOLD_BAR     = ItemDef("gold_bar",      "Gold Bar",      "Gleaming refined gold")
    val MITHRIL_BAR  = ItemDef("mithril_bar",   "Mithril Bar",   "Lightweight mythical alloy")
    val ADAMANT_BAR  = ItemDef("adamant_bar",   "Adamantite Bar","Dark green hardened bar")
    val RUNITE_BAR   = ItemDef("runite_bar",    "Runite Bar",    "The hardest known metal")

    val items: Map<String, ItemDef> = listOf(
        BRONZE_BAR, IRON_BAR, STEEL_BAR, GOLD_BAR,
        MITHRIL_BAR, ADAMANT_BAR, RUNITE_BAR,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction(
            id = "smelt_bronze", skillId = SkillId.SMITHING, name = "Bronze Bar",
            levelRequired = 1, xpPerAction = 6.2, actionTimeMs = 2400L,
            resourceId = "bronze_bar",
            inputItems = mapOf("copper_ore" to 1, "tin_ore" to 1),
        ),
        SkillAction(
            id = "smelt_iron", skillId = SkillId.SMITHING, name = "Iron Bar",
            levelRequired = 15, xpPerAction = 12.5, actionTimeMs = 3000L,
            resourceId = "iron_bar",
            inputItems = mapOf("iron_ore" to 1),
        ),
        SkillAction(
            id = "smelt_steel", skillId = SkillId.SMITHING, name = "Steel Bar",
            levelRequired = 30, xpPerAction = 17.5, actionTimeMs = 3600L,
            resourceId = "steel_bar",
            inputItems = mapOf("iron_ore" to 1, "coal" to 2),
        ),
        SkillAction(
            id = "smelt_gold", skillId = SkillId.SMITHING, name = "Gold Bar",
            levelRequired = 40, xpPerAction = 22.5, actionTimeMs = 4200L,
            resourceId = "gold_bar",
            inputItems = mapOf("gold_ore" to 1),
        ),
        SkillAction(
            id = "smelt_mithril", skillId = SkillId.SMITHING, name = "Mithril Bar",
            levelRequired = 50, xpPerAction = 30.0, actionTimeMs = 5400L,
            resourceId = "mithril_bar",
            inputItems = mapOf("mithril_ore" to 1, "coal" to 4),
        ),
        SkillAction(
            id = "smelt_adamant", skillId = SkillId.SMITHING, name = "Adamantite Bar",
            levelRequired = 70, xpPerAction = 37.5, actionTimeMs = 6600L,
            resourceId = "adamant_bar",
            inputItems = mapOf("adamantite_ore" to 1, "coal" to 6),
        ),
        SkillAction(
            id = "smelt_runite", skillId = SkillId.SMITHING, name = "Runite Bar",
            levelRequired = 85, xpPerAction = 50.0, actionTimeMs = 8400L,
            resourceId = "runite_bar",
            inputItems = mapOf("runite_ore" to 1, "coal" to 8),
        ),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
