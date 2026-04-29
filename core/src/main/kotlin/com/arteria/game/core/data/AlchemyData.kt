package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

// NOTE: Alchemy upgrades crafted materials into high-value reagents.
object AlchemyData {
    val TRANSMUTE_DUST = ItemDef("transmute_dust", "Transmute Dust", "Fine powder used in basic conversions")
    val FOCUS_ELIXIR = ItemDef("focus_elixir", "Focus Elixir", "Improves concentration during training")
    val VIGOR_ELIXIR = ItemDef("vigor_elixir", "Vigor Elixir", "A potent stimulant for long sessions")
    val CHAOS_CATALYST = ItemDef("chaos_catalyst", "Chaos Catalyst", "Unstable core for volatile recipes")
    val STAR_ESSENCE = ItemDef("star_essence", "Star Essence", "Condensed stellar residue")
    val VOID_CONCENTRATE = ItemDef("void_concentrate", "Void Concentrate", "Distilled void matter")
    val AETHER_DISTILLATE = ItemDef("aether_distillate", "Aether Distillate", "Refined high-frequency aether")
    val PHILOSOPHERS_ICHOR = ItemDef(
        "philosophers_ichor",
        "Philosopher's Ichor",
        "A legendary transmutation medium",
    )

    val items: Map<String, ItemDef> = listOf(
        TRANSMUTE_DUST,
        FOCUS_ELIXIR,
        VIGOR_ELIXIR,
        CHAOS_CATALYST,
        STAR_ESSENCE,
        VOID_CONCENTRATE,
        AETHER_DISTILLATE,
        PHILOSOPHERS_ICHOR,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction(
            id = "mix_transmute_dust",
            skillId = SkillId.ALCHEMY,
            name = "Mix Transmute Dust",
            levelRequired = 1,
            xpPerAction = 24.0,
            actionTimeMs = 2600L,
            resourceId = "transmute_dust",
            inputItems = mapOf("rust_scrap" to 2, "air_rune" to 5),
        ),
        SkillAction(
            id = "brew_focus_elixir",
            skillId = SkillId.ALCHEMY,
            name = "Brew Focus Elixir",
            levelRequired = 14,
            xpPerAction = 40.0,
            actionTimeMs = 3400L,
            resourceId = "focus_elixir",
            inputItems = mapOf("sage_infusion" to 1, "water_rune" to 6),
        ),
        SkillAction(
            id = "brew_vigor_elixir",
            skillId = SkillId.ALCHEMY,
            name = "Brew Vigor Elixir",
            levelRequired = 28,
            xpPerAction = 62.0,
            actionTimeMs = 4300L,
            resourceId = "vigor_elixir",
            inputItems = mapOf("ginseng_tonic" to 1, "earth_rune" to 8),
        ),
        SkillAction(
            id = "distill_chaos_catalyst",
            skillId = SkillId.ALCHEMY,
            name = "Distill Chaos Catalyst",
            levelRequired = 42,
            xpPerAction = 95.0,
            actionTimeMs = 5200L,
            resourceId = "chaos_catalyst",
            inputItems = mapOf("nightshade_tincture" to 1, "chaos_rune" to 4),
        ),
        SkillAction(
            id = "extract_star_essence",
            skillId = SkillId.ALCHEMY,
            name = "Extract Star Essence",
            levelRequired = 58,
            xpPerAction = 140.0,
            actionTimeMs = 6200L,
            resourceId = "star_essence",
            inputItems = mapOf("cut_sapphire" to 1, "cosmic_rune" to 3),
        ),
        SkillAction(
            id = "refine_void_concentrate",
            skillId = SkillId.ALCHEMY,
            name = "Refine Void Concentrate",
            levelRequired = 72,
            xpPerAction = 205.0,
            actionTimeMs = 7600L,
            resourceId = "void_concentrate",
            inputItems = mapOf("void_shard" to 2, "void_rune" to 2),
        ),
        SkillAction(
            id = "distill_aether",
            skillId = SkillId.ALCHEMY,
            name = "Distill Aether",
            levelRequired = 86,
            xpPerAction = 295.0,
            actionTimeMs = 9200L,
            resourceId = "aether_distillate",
            inputItems = mapOf("star_herb" to 1, "aether_rune" to 2),
        ),
        SkillAction(
            id = "synthesize_philosophers_ichor",
            skillId = SkillId.ALCHEMY,
            name = "Synthesize Philosopher's Ichor",
            levelRequired = 96,
            xpPerAction = 430.0,
            actionTimeMs = 11200L,
            resourceId = "philosophers_ichor",
            inputItems = mapOf("aether_distillate" to 1, "void_concentrate" to 1, "chaos_rune" to 6),
        ),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
