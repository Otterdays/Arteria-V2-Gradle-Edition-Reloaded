package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

object FishingData {
    val SHRIMP       = ItemDef("shrimp",        "Raw Shrimp",        "Small crustaceans from shallow water")
    val SARDINE      = ItemDef("sardine",        "Raw Sardine",       "Slippery little fish")
    val TROUT        = ItemDef("trout",          "Raw Trout",         "A decent-sized freshwater fish")
    val SALMON       = ItemDef("salmon",         "Raw Salmon",        "A prized freshwater catch")
    val TUNA         = ItemDef("tuna",           "Raw Tuna",          "Heavy salt-water fish")
    val LOBSTER      = ItemDef("lobster",        "Raw Lobster",       "Armoured crustacean from deep water")
    val SWORDFISH    = ItemDef("swordfish",      "Raw Swordfish",     "Dangerous to catch, delicious to eat")
    val SHARK        = ItemDef("shark",          "Raw Shark",         "Apex predator of the deep")

    val items: Map<String, ItemDef> = listOf(
        SHRIMP, SARDINE, TROUT, SALMON, TUNA, LOBSTER, SWORDFISH, SHARK,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction("fish_shrimp",    SkillId.FISHING, "Net Shrimp",      1,  10.0,  2400L, "shrimp"),
        SkillAction("fish_sardine",   SkillId.FISHING, "Bait Sardine",   10,  20.0,  3000L, "sardine"),
        SkillAction("fish_trout",     SkillId.FISHING, "Fly Trout",      20,  50.0,  4200L, "trout"),
        SkillAction("fish_salmon",    SkillId.FISHING, "Fly Salmon",     30,  70.0,  4800L, "salmon"),
        SkillAction("fish_tuna",      SkillId.FISHING, "Harpoon Tuna",   35,  80.0,  5400L, "tuna"),
        SkillAction("fish_lobster",   SkillId.FISHING, "Cage Lobster",   40,  90.0,  6000L, "lobster"),
        SkillAction("fish_swordfish", SkillId.FISHING, "Harpoon Swordfish", 50, 100.0, 7200L, "swordfish"),
        SkillAction("fish_shark",     SkillId.FISHING, "Deep Net Shark", 76, 110.0,  9000L, "shark"),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
