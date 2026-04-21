package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

object JewelcraftingData {
    val CUT_SAPPHIRE = ItemDef("cut_sapphire", "Cut Sapphire", "A brilliant blue gemstone")
    val CUT_EMERALD = ItemDef("cut_emerald", "Cut Emerald", "A sparkling green gemstone")
    val CUT_RUBY = ItemDef("cut_ruby", "Cut Ruby", "A glowing red gemstone")
    val CUT_DIAMOND = ItemDef("cut_diamond", "Cut Diamond", "A clear, multi-faceted gemstone")

    val items: Map<String, ItemDef> = listOf(
        CUT_SAPPHIRE, CUT_EMERALD, CUT_RUBY, CUT_DIAMOND
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        // Gem Cutting
        SkillAction(
            id = "cut_sapphire", skillId = SkillId.JEWELCRAFTING, name = "Cut Sapphire",
            levelRequired = 1, xpPerAction = 50.0, actionTimeMs = 2000L,
            resourceId = "cut_sapphire",
            inputItems = mapOf("raw_sapphire" to 1),
        ),
        SkillAction(
            id = "cut_emerald", skillId = SkillId.JEWELCRAFTING, name = "Cut Emerald",
            levelRequired = 20, xpPerAction = 67.0, actionTimeMs = 2000L,
            resourceId = "cut_emerald",
            inputItems = mapOf("raw_emerald" to 1),
        ),
        SkillAction(
            id = "cut_ruby", skillId = SkillId.JEWELCRAFTING, name = "Cut Ruby",
            levelRequired = 34, xpPerAction = 85.0, actionTimeMs = 2000L,
            resourceId = "cut_ruby",
            inputItems = mapOf("raw_ruby" to 1),
        ),
        SkillAction(
            id = "cut_diamond", skillId = SkillId.JEWELCRAFTING, name = "Cut Diamond",
            levelRequired = 43, xpPerAction = 107.0, actionTimeMs = 2000L,
            resourceId = "cut_diamond",
            inputItems = mapOf("raw_diamond" to 1),
        ),

        // Jewelry (Accessories from EquipmentRegistry)
        SkillAction(
            id = "craft_sapphire_ring", skillId = SkillId.JEWELCRAFTING, name = "Sapphire Ring",
            levelRequired = 20, xpPerAction = 40.0, actionTimeMs = 3000L,
            resourceId = "ring_of_fortune", // Using existing ID as proxy
            inputItems = mapOf("gold_bar" to 1, "cut_sapphire" to 1),
        ),
        SkillAction(
            id = "craft_amulet_of_accuracy", skillId = SkillId.JEWELCRAFTING, name = "Amulet of Accuracy",
            levelRequired = 5, xpPerAction = 20.0, actionTimeMs = 3000L,
            resourceId = "amulet_of_accuracy",
            inputItems = mapOf("bronze_bar" to 1, "cut_sapphire" to 1),
        ),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
