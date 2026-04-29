package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

// NOTE: Divination creates foresight resources from cosmic/support loops.
object DivinationData {
    val FATE_THREAD = ItemDef("fate_thread", "Fate Thread", "A thin strand of probable futures")
    val OMEN_CHARM = ItemDef("omen_charm", "Omen Charm", "A charm tuned to subtle signs")
    val LUCK_SIGIL = ItemDef("luck_sigil", "Luck Sigil", "Improves favorable outcomes")
    val STAR_CHART = ItemDef("star_chart", "Star Chart", "Mapped celestial patterns for predictive reading")
    val PROPHECY_SLATE = ItemDef("prophecy_slate", "Prophecy Slate", "A slate etched with branching outcomes")
    val DREAM_COMPASS = ItemDef("dream_compass", "Dream Compass", "Points toward hidden opportunities")
    val ORACLE_MATRIX = ItemDef("oracle_matrix", "Oracle Matrix", "A lattice of layered predictions")
    val CHRONICLE_FATEWEAVE = ItemDef(
        "chronicle_fateweave",
        "Chronicle Fateweave",
        "An apex divination artifact for timeline control",
    )

    val items: Map<String, ItemDef> = listOf(
        FATE_THREAD,
        OMEN_CHARM,
        LUCK_SIGIL,
        STAR_CHART,
        PROPHECY_SLATE,
        DREAM_COMPASS,
        ORACLE_MATRIX,
        CHRONICLE_FATEWEAVE,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction(
            id = "spin_fate_thread",
            skillId = SkillId.DIVINATION,
            name = "Spin Fate Thread",
            levelRequired = 1,
            xpPerAction = 20.0,
            actionTimeMs = 2800L,
            resourceId = "fate_thread",
            inputItems = mapOf("air_rune" to 6, "starfruit" to 1),
        ),
        SkillAction(
            id = "craft_omen_charm",
            skillId = SkillId.DIVINATION,
            name = "Craft Omen Charm",
            levelRequired = 14,
            xpPerAction = 36.0,
            actionTimeMs = 3600L,
            resourceId = "omen_charm",
            inputItems = mapOf("fate_thread" to 1, "cut_sapphire" to 1),
        ),
        SkillAction(
            id = "inscribe_luck_sigil",
            skillId = SkillId.DIVINATION,
            name = "Inscribe Luck Sigil",
            levelRequired = 28,
            xpPerAction = 58.0,
            actionTimeMs = 4500L,
            resourceId = "luck_sigil",
            inputItems = mapOf("omen_charm" to 1, "cosmic_rune" to 3),
        ),
        SkillAction(
            id = "draw_star_chart",
            skillId = SkillId.DIVINATION,
            name = "Draw Star Chart",
            levelRequired = 42,
            xpPerAction = 88.0,
            actionTimeMs = 5600L,
            resourceId = "star_chart",
            inputItems = mapOf("dragonflower" to 1, "cut_emerald" to 1),
        ),
        SkillAction(
            id = "etch_prophecy_slate",
            skillId = SkillId.DIVINATION,
            name = "Etch Prophecy Slate",
            levelRequired = 58,
            xpPerAction = 132.0,
            actionTimeMs = 6800L,
            resourceId = "prophecy_slate",
            inputItems = mapOf("star_chart" to 1, "chaos_rune" to 4),
        ),
        SkillAction(
            id = "tune_dream_compass",
            skillId = SkillId.DIVINATION,
            name = "Tune Dream Compass",
            levelRequired = 72,
            xpPerAction = 190.0,
            actionTimeMs = 8200L,
            resourceId = "dream_compass",
            inputItems = mapOf("prophecy_slate" to 1, "aether_rune" to 2),
        ),
        SkillAction(
            id = "weave_oracle_matrix",
            skillId = SkillId.DIVINATION,
            name = "Weave Oracle Matrix",
            levelRequired = 86,
            xpPerAction = 275.0,
            actionTimeMs = 9800L,
            resourceId = "oracle_matrix",
            inputItems = mapOf("dream_compass" to 1, "star_essence" to 1, "void_rune" to 2),
        ),
        SkillAction(
            id = "forge_chronicle_fateweave",
            skillId = SkillId.DIVINATION,
            name = "Forge Chronicle Fateweave",
            levelRequired = 96,
            xpPerAction = 400.0,
            actionTimeMs = 11600L,
            resourceId = "chronicle_fateweave",
            inputItems = mapOf("oracle_matrix" to 1, "chrono_regulator" to 1, "cosmic_rune" to 4),
        ),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
