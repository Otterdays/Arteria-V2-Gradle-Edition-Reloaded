package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

object CookingData {
    val COOKED_SHRIMP     = ItemDef("cooked_shrimp",     "Cooked Shrimp",     "Tiny but tasty; heals a little")
    val COOKED_SARDINE    = ItemDef("cooked_sardine",    "Cooked Sardine",    "A decent light meal")
    val COOKED_TROUT      = ItemDef("cooked_trout",      "Cooked Trout",      "Freshwater trout, nicely charred")
    val COOKED_SALMON     = ItemDef("cooked_salmon",     "Cooked Salmon",     "Rich pink flesh, hearty meal")
    val COOKED_TUNA       = ItemDef("cooked_tuna",       "Cooked Tuna",       "Dense, restorative fish")
    val COOKED_LOBSTER    = ItemDef("cooked_lobster",    "Cooked Lobster",    "Delicacy of the deep — restores plenty")
    val COOKED_SWORDFISH  = ItemDef("cooked_swordfish",  "Cooked Swordfish",  "Tough meat, powerful sustenance")
    val COOKED_SHARK      = ItemDef("cooked_shark",      "Cooked Shark",      "The finest food in the land")

    val items: Map<String, ItemDef> = listOf(
        COOKED_SHRIMP, COOKED_SARDINE, COOKED_TROUT, COOKED_SALMON,
        COOKED_TUNA, COOKED_LOBSTER, COOKED_SWORDFISH, COOKED_SHARK,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction(
            id = "cook_shrimp", skillId = SkillId.COOKING, name = "Cook Shrimp",
            levelRequired = 1, xpPerAction = 30.0, actionTimeMs = 2400L,
            resourceId = "cooked_shrimp",
            inputItems = mapOf("shrimp" to 1),
        ),
        SkillAction(
            id = "cook_sardine", skillId = SkillId.COOKING, name = "Cook Sardine",
            levelRequired = 1, xpPerAction = 40.0, actionTimeMs = 2400L,
            resourceId = "cooked_sardine",
            inputItems = mapOf("sardine" to 1),
        ),
        SkillAction(
            id = "cook_trout", skillId = SkillId.COOKING, name = "Cook Trout",
            levelRequired = 15, xpPerAction = 70.0, actionTimeMs = 3000L,
            resourceId = "cooked_trout",
            inputItems = mapOf("trout" to 1),
        ),
        SkillAction(
            id = "cook_salmon", skillId = SkillId.COOKING, name = "Cook Salmon",
            levelRequired = 25, xpPerAction = 90.0, actionTimeMs = 3600L,
            resourceId = "cooked_salmon",
            inputItems = mapOf("salmon" to 1),
        ),
        SkillAction(
            id = "cook_tuna", skillId = SkillId.COOKING, name = "Cook Tuna",
            levelRequired = 30, xpPerAction = 100.0, actionTimeMs = 4200L,
            resourceId = "cooked_tuna",
            inputItems = mapOf("tuna" to 1),
        ),
        SkillAction(
            id = "cook_lobster", skillId = SkillId.COOKING, name = "Cook Lobster",
            levelRequired = 40, xpPerAction = 120.0, actionTimeMs = 4800L,
            resourceId = "cooked_lobster",
            inputItems = mapOf("lobster" to 1),
        ),
        SkillAction(
            id = "cook_swordfish", skillId = SkillId.COOKING, name = "Cook Swordfish",
            levelRequired = 45, xpPerAction = 140.0, actionTimeMs = 5400L,
            resourceId = "cooked_swordfish",
            inputItems = mapOf("swordfish" to 1),
        ),
        SkillAction(
            id = "cook_shark", skillId = SkillId.COOKING, name = "Cook Shark",
            levelRequired = 80, xpPerAction = 210.0, actionTimeMs = 7200L,
            resourceId = "cooked_shark",
            inputItems = mapOf("shark" to 1),
        ),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
