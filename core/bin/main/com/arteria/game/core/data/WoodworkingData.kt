package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

// NOTE: Woodworking uses logs from Logging as inputs (bank consumption via inputItems).

object WoodworkingData {
    val PLANK          = ItemDef("plank",           "Plank",           "A simple flat piece of sawn timber")
    val OAK_PLANK      = ItemDef("oak_plank",       "Oak Plank",       "A sturdy oak plank for furniture")
    val WILLOW_PLANK   = ItemDef("willow_plank",    "Willow Plank",    "A supple willow plank, slightly flexible")
    val MAPLE_PLANK    = ItemDef("maple_plank",     "Maple Plank",     "Dense hardwood plank, good for weapons")
    val YEW_SHIELD     = ItemDef("yew_shield",      "Yew Shield",      "A curved shield shaped from yew wood")
    val MAGIC_STAFF    = ItemDef("magic_staff",     "Magic Staff",     "A staff carved from a magic log")
    val ELDER_FRAME    = ItemDef("elder_frame",     "Elder Frame",     "A mystical hardwood construction frame")
    val VOID_CARVING   = ItemDef("void_carving",    "Void Carving",    "An eerie sculpture in elder wood")

    val items: Map<String, ItemDef> = listOf(
        PLANK, OAK_PLANK, WILLOW_PLANK, MAPLE_PLANK,
        YEW_SHIELD, MAGIC_STAFF, ELDER_FRAME, VOID_CARVING,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction(
            id = "saw_plank", skillId = SkillId.WOODWORKING, name = "Saw Plank",
            levelRequired = 1, xpPerAction = 15.0, actionTimeMs = 3000L,
            resourceId = "plank",
            inputItems = mapOf("normal_logs" to 1),
        ),
        SkillAction(
            id = "saw_oak_plank", skillId = SkillId.WOODWORKING, name = "Oak Plank",
            levelRequired = 15, xpPerAction = 25.0, actionTimeMs = 3600L,
            resourceId = "oak_plank",
            inputItems = mapOf("oak_logs" to 1),
        ),
        SkillAction(
            id = "saw_willow_plank", skillId = SkillId.WOODWORKING, name = "Willow Plank",
            levelRequired = 30, xpPerAction = 45.0, actionTimeMs = 4800L,
            resourceId = "willow_plank",
            inputItems = mapOf("willow_logs" to 1),
        ),
        SkillAction(
            id = "saw_maple_plank", skillId = SkillId.WOODWORKING, name = "Maple Plank",
            levelRequired = 45, xpPerAction = 70.0, actionTimeMs = 6000L,
            resourceId = "maple_plank",
            inputItems = mapOf("maple_logs" to 1),
        ),
        SkillAction(
            id = "carve_yew_shield", skillId = SkillId.WOODWORKING, name = "Yew Shield",
            levelRequired = 60, xpPerAction = 115.0, actionTimeMs = 7200L,
            resourceId = "yew_shield",
            inputItems = mapOf("yew_logs" to 2),
        ),
        SkillAction(
            id = "carve_magic_staff", skillId = SkillId.WOODWORKING, name = "Magic Staff",
            levelRequired = 75, xpPerAction = 180.0, actionTimeMs = 9000L,
            resourceId = "magic_staff",
            inputItems = mapOf("magic_logs" to 2),
        ),
        SkillAction(
            id = "craft_elder_frame", skillId = SkillId.WOODWORKING, name = "Elder Frame",
            levelRequired = 85, xpPerAction = 300.0, actionTimeMs = 12000L,
            resourceId = "elder_frame",
            inputItems = mapOf("elder_logs" to 2, "magic_logs" to 1),
        ),
        SkillAction(
            id = "carve_void_carving", skillId = SkillId.WOODWORKING, name = "Void Carving",
            levelRequired = 95, xpPerAction = 500.0, actionTimeMs = 15600L,
            resourceId = "void_carving",
            inputItems = mapOf("elder_logs" to 3, "magic_logs" to 2),
        ),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
