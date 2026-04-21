package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

object FiremakingData {
    val ASHES = ItemDef("ashes", "Ashes", "The remains of a fire. Useful in Alchemy.")
    
    val items: Map<String, ItemDef> = mapOf(
        ASHES.id to ASHES
    )

    val actions: List<SkillAction> = listOf(
        SkillAction(
            id = "burn_normal_logs", skillId = SkillId.FIREMAKING, name = "Logs",
            levelRequired = 1, xpPerAction = 40.0, actionTimeMs = 2000L,
            resourceId = "ashes",
            inputItems = mapOf("normal_logs" to 1),
        ),
        SkillAction(
            id = "burn_oak_logs", skillId = SkillId.FIREMAKING, name = "Oak Logs",
            levelRequired = 15, xpPerAction = 60.0, actionTimeMs = 2000L,
            resourceId = "ashes",
            inputItems = mapOf("oak_logs" to 1),
        ),
        SkillAction(
            id = "burn_willow_logs", skillId = SkillId.FIREMAKING, name = "Willow Logs",
            levelRequired = 30, xpPerAction = 90.0, actionTimeMs = 2000L,
            resourceId = "ashes",
            inputItems = mapOf("willow_logs" to 1),
        ),
        SkillAction(
            id = "burn_maple_logs", skillId = SkillId.FIREMAKING, name = "Maple Logs",
            levelRequired = 45, xpPerAction = 135.0, actionTimeMs = 2000L,
            resourceId = "ashes",
            inputItems = mapOf("maple_logs" to 1),
        ),
        SkillAction(
            id = "burn_yew_logs", skillId = SkillId.FIREMAKING, name = "Yew Logs",
            levelRequired = 60, xpPerAction = 202.5, actionTimeMs = 2000L,
            resourceId = "ashes",
            inputItems = mapOf("yew_logs" to 1),
        ),
        SkillAction(
            id = "burn_magic_logs", skillId = SkillId.FIREMAKING, name = "Magic Logs",
            levelRequired = 75, xpPerAction = 303.8, actionTimeMs = 2000L,
            resourceId = "ashes",
            inputItems = mapOf("magic_logs" to 1),
        ),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
