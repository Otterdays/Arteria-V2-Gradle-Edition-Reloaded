package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

object LoggingData {
    val NORMAL_LOGS = ItemDef("normal_logs", "Normal Logs", "Freshly chopped timber")
    val OAK_LOGS = ItemDef("oak_logs", "Oak Logs", "Sturdy oak wood")
    val WILLOW_LOGS = ItemDef("willow_logs", "Willow Logs", "Supple willow branches")
    val MAPLE_LOGS = ItemDef("maple_logs", "Maple Logs", "Dense hardwood maple")
    val YEW_LOGS = ItemDef("yew_logs", "Yew Logs", "Ancient yew timber")
    val MAGIC_LOGS = ItemDef("magic_logs", "Magic Logs", "Logs pulsing with arcane energy")
    val REDWOOD_LOGS = ItemDef("redwood_logs", "Redwood Logs", "Massive ancient redwood timber")
    val ELDER_LOGS = ItemDef("elder_logs", "Elder Logs", "Legendary elder wood, near-mythical")

    val items: Map<String, ItemDef> = listOf(
        NORMAL_LOGS, OAK_LOGS, WILLOW_LOGS, MAPLE_LOGS,
        YEW_LOGS, MAGIC_LOGS, REDWOOD_LOGS, ELDER_LOGS,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction("chop_normal", SkillId.LOGGING, "Normal Tree",  1,  25.0,  3000L, "normal_logs"),
        SkillAction("chop_oak",    SkillId.LOGGING, "Oak Tree",    15,  37.5,  4000L, "oak_logs"),
        SkillAction("chop_willow", SkillId.LOGGING, "Willow Tree", 30,  67.5,  5400L, "willow_logs"),
        SkillAction("chop_maple",  SkillId.LOGGING, "Maple Tree",  45, 100.0,  6000L, "maple_logs"),
        SkillAction("chop_yew",    SkillId.LOGGING, "Yew Tree",    60, 175.0,  7200L, "yew_logs"),
        SkillAction("chop_magic",  SkillId.LOGGING, "Magic Tree",  75, 250.0,  9000L, "magic_logs"),
        SkillAction("chop_redwood",SkillId.LOGGING, "Redwood Tree",85, 380.0, 10800L, "redwood_logs"),
        SkillAction("chop_elder",  SkillId.LOGGING, "Elder Tree",  95, 500.0, 14400L, "elder_logs"),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
