package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

object FarmingData {
    val CARROT        = ItemDef("carrot",         "Carrot",          "A crisp orange root vegetable")
    val POTATO        = ItemDef("potato",          "Potato",          "A starchy tuber — versatile and filling")
    val TOMATO        = ItemDef("tomato",           "Tomato",          "A bright red fruit used in many recipes")
    val CORN          = ItemDef("corn",             "Corn",            "Golden ears of sweet corn")
    val PUMPKIN       = ItemDef("pumpkin",          "Pumpkin",         "A hardy squash with thick flesh")
    val WATERMELON    = ItemDef("watermelon",       "Watermelon",      "Enormous and refreshing summer fruit")
    val DRAGONFLOWER  = ItemDef("dragonflower",     "Dragonflower",    "A rare blossom with magical resonance")
    val STARFRUIT     = ItemDef("starfruit",        "Starfruit",       "A stellar fruit that gleams in moonlight")

    val items: Map<String, ItemDef> = listOf(
        CARROT, POTATO, TOMATO, CORN, PUMPKIN, WATERMELON, DRAGONFLOWER, STARFRUIT,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction("farm_carrot",      SkillId.FARMING, "Carrot Patch",        1,  18.0,  3000L, "carrot"),
        SkillAction("farm_potato",      SkillId.FARMING, "Potato Field",        5,  25.0,  3600L, "potato"),
        SkillAction("farm_tomato",      SkillId.FARMING, "Tomato Vine",        15,  40.0,  4200L, "tomato"),
        SkillAction("farm_corn",        SkillId.FARMING, "Corn Row",           25,  55.0,  5400L, "corn"),
        SkillAction("farm_pumpkin",     SkillId.FARMING, "Pumpkin Plot",       40,  85.0,  6600L, "pumpkin"),
        SkillAction("farm_watermelon",  SkillId.FARMING, "Watermelon Vine",    55, 120.0,  8400L, "watermelon"),
        SkillAction("farm_dragonflower",SkillId.FARMING, "Dragon Garden",      70, 200.0, 10800L, "dragonflower"),
        SkillAction("farm_starfruit",   SkillId.FARMING, "Starfall Orchard",   90, 350.0, 14400L, "starfruit"),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
