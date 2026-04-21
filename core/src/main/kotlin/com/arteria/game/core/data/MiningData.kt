package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

object MiningData {
    val COPPER_ORE = ItemDef("copper_ore", "Copper Ore", "A chunk of raw copper")
    val TIN_ORE = ItemDef("tin_ore", "Tin Ore", "A chunk of raw tin")
    val IRON_ORE = ItemDef("iron_ore", "Iron Ore", "Dense iron ore, ready for smelting")
    val COAL = ItemDef("coal", "Coal", "Fuel for the forge")
    val GOLD_ORE = ItemDef("gold_ore", "Gold Ore", "Gleaming gold-bearing rock")
    val MITHRIL_ORE = ItemDef("mithril_ore", "Mithril Ore", "Blue-tinged mythical ore")
    val ADAMANTITE_ORE = ItemDef("adamantite_ore", "Adamantite Ore", "Dark green, incredibly dense")
    val RUNITE_ORE = ItemDef("runite_ore", "Runite Ore", "Cyan crystalline ore pulsing with energy")

    val RAW_SAPPHIRE = ItemDef("raw_sapphire", "Raw Sapphire", "A cloudly blue gemstone")
    val RAW_EMERALD = ItemDef("raw_emerald", "Raw Emerald", "A rough green gemstone")
    val RAW_RUBY = ItemDef("raw_ruby", "Raw Ruby", "A dull red gemstone")
    val RAW_DIAMOND = ItemDef("raw_diamond", "Raw Diamond", "A hard, unpolished gemstone")

    val items: Map<String, ItemDef> = listOf(
        COPPER_ORE,
        TIN_ORE,
        IRON_ORE,
        COAL,
        GOLD_ORE,
        MITHRIL_ORE,
        ADAMANTITE_ORE,
        RUNITE_ORE,
        RAW_SAPPHIRE,
        RAW_EMERALD,
        RAW_RUBY,
        RAW_DIAMOND,
    ).associateBy { it.id }

    val MINE_COPPER = SkillAction(
        id = "mine_copper",
        skillId = SkillId.MINING,
        name = "Copper Rock",
        levelRequired = 1,
        xpPerAction = 17.5,
        actionTimeMs = 2400L,
        resourceId = "copper_ore",
    )

    val MINE_TIN = SkillAction(
        id = "mine_tin",
        skillId = SkillId.MINING,
        name = "Tin Rock",
        levelRequired = 1,
        xpPerAction = 17.5,
        actionTimeMs = 2400L,
        resourceId = "tin_ore",
    )

    val MINE_GEMS = SkillAction(
        id = "mine_gems",
        skillId = SkillId.MINING,
        name = "Gem Rock",
        levelRequired = 1,
        xpPerAction = 25.0,
        actionTimeMs = 3000L,
        resourceId = "raw_sapphire", // Basic drop for now
    )

    val MINE_IRON = SkillAction(
        id = "mine_iron",
        skillId = SkillId.MINING,
        name = "Iron Rock",
        levelRequired = 15,
        xpPerAction = 35.0,
        actionTimeMs = 3600L,
        resourceId = "iron_ore",
    )

    val MINE_COAL = SkillAction(
        id = "mine_coal",
        skillId = SkillId.MINING,
        name = "Coal Rock",
        levelRequired = 30,
        xpPerAction = 50.0,
        actionTimeMs = 4200L,
        resourceId = "coal",
    )

    val MINE_GOLD = SkillAction(
        id = "mine_gold",
        skillId = SkillId.MINING,
        name = "Gold Rock",
        levelRequired = 40,
        xpPerAction = 65.0,
        actionTimeMs = 4800L,
        resourceId = "gold_ore",
    )

    val MINE_MITHRIL = SkillAction(
        id = "mine_mithril",
        skillId = SkillId.MINING,
        name = "Mithril Rock",
        levelRequired = 55,
        xpPerAction = 80.0,
        actionTimeMs = 5400L,
        resourceId = "mithril_ore",
    )

    val MINE_ADAMANTITE = SkillAction(
        id = "mine_adamantite",
        skillId = SkillId.MINING,
        name = "Adamantite Rock",
        levelRequired = 70,
        xpPerAction = 95.0,
        actionTimeMs = 6000L,
        resourceId = "adamantite_ore",
    )

    val MINE_RUNITE = SkillAction(
        id = "mine_runite",
        skillId = SkillId.MINING,
        name = "Runite Rock",
        levelRequired = 85,
        xpPerAction = 125.0,
        actionTimeMs = 7200L,
        resourceId = "runite_ore",
    )

    val actions: List<SkillAction> = listOf(
        MINE_COPPER,
        MINE_TIN,
        MINE_GEMS,
        MINE_IRON,
        MINE_COAL,
        MINE_GOLD,
        MINE_MITHRIL,
        MINE_ADAMANTITE,
        MINE_RUNITE,
    )

    /** All mining actions keyed by action id — used by TickEngine. */
    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}

