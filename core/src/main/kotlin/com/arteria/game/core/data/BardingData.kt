package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

// NOTE: Barding crafts performance items and harmony artifacts.
object BardingData {
    val PRACTICE_LUTE = ItemDef("practice_lute", "Practice Lute", "A simple instrument for beginner melodies")
    val RHYTHM_SCROLL = ItemDef("rhythm_scroll", "Rhythm Scroll", "Written rhythm patterns for ensembles")
    val BATTLE_CHANT = ItemDef("battle_chant", "Battle Chant", "A martial chorus for combat morale")
    val LULLABY_HYMN = ItemDef("lullaby_hymn", "Lullaby Hymn", "A soothing arrangement for recovery")
    val RESONANCE_SONGBOOK = ItemDef(
        "resonance_songbook",
        "Resonance Songbook",
        "A curated set of harmonic progression pieces",
    )
    val ORCHESTRAL_SUITE = ItemDef(
        "orchestral_suite",
        "Orchestral Suite",
        "Advanced composition requiring multiple motifs",
    )
    val CELESTIAL_ANTHEM = ItemDef(
        "celestial_anthem",
        "Celestial Anthem",
        "A composition aligned with stellar motion",
    )
    val ETERNAL_REQUIEM = ItemDef(
        "eternal_requiem",
        "Eternal Requiem",
        "A legendary performance said to bend fate",
    )

    val items: Map<String, ItemDef> = listOf(
        PRACTICE_LUTE,
        RHYTHM_SCROLL,
        BATTLE_CHANT,
        LULLABY_HYMN,
        RESONANCE_SONGBOOK,
        ORCHESTRAL_SUITE,
        CELESTIAL_ANTHEM,
        ETERNAL_REQUIEM,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction(
            id = "craft_practice_lute",
            skillId = SkillId.BARDING,
            name = "Craft Practice Lute",
            levelRequired = 1,
            xpPerAction = 18.0,
            actionTimeMs = 3000L,
            resourceId = "practice_lute",
            inputItems = mapOf("plank" to 1, "rough_thread" to 2),
        ),
        SkillAction(
            id = "compose_rhythm_scroll",
            skillId = SkillId.BARDING,
            name = "Compose Rhythm Scroll",
            levelRequired = 14,
            xpPerAction = 34.0,
            actionTimeMs = 3800L,
            resourceId = "rhythm_scroll",
            inputItems = mapOf("practice_lute" to 1, "sage_infusion" to 1),
        ),
        SkillAction(
            id = "write_battle_chant",
            skillId = SkillId.BARDING,
            name = "Write Battle Chant",
            levelRequired = 28,
            xpPerAction = 54.0,
            actionTimeMs = 4700L,
            resourceId = "battle_chant",
            inputItems = mapOf("rhythm_scroll" to 1, "steel_arrows" to 10),
        ),
        SkillAction(
            id = "compose_lullaby_hymn",
            skillId = SkillId.BARDING,
            name = "Compose Lullaby Hymn",
            levelRequired = 42,
            xpPerAction = 82.0,
            actionTimeMs = 5800L,
            resourceId = "lullaby_hymn",
            inputItems = mapOf("rhythm_scroll" to 1, "simple_remedy" to 1),
        ),
        SkillAction(
            id = "assemble_resonance_songbook",
            skillId = SkillId.BARDING,
            name = "Assemble Resonance Songbook",
            levelRequired = 58,
            xpPerAction = 124.0,
            actionTimeMs = 7000L,
            resourceId = "resonance_songbook",
            inputItems = mapOf("battle_chant" to 1, "lullaby_hymn" to 1),
        ),
        SkillAction(
            id = "arrange_orchestral_suite",
            skillId = SkillId.BARDING,
            name = "Arrange Orchestral Suite",
            levelRequired = 72,
            xpPerAction = 178.0,
            actionTimeMs = 8400L,
            resourceId = "orchestral_suite",
            inputItems = mapOf("resonance_songbook" to 1, "cosmic_rune" to 3),
        ),
        SkillAction(
            id = "compose_celestial_anthem",
            skillId = SkillId.BARDING,
            name = "Compose Celestial Anthem",
            levelRequired = 86,
            xpPerAction = 258.0,
            actionTimeMs = 10000L,
            resourceId = "celestial_anthem",
            inputItems = mapOf("orchestral_suite" to 1, "star_chart" to 1, "aether_rune" to 2),
        ),
        SkillAction(
            id = "perform_eternal_requiem",
            skillId = SkillId.BARDING,
            name = "Perform Eternal Requiem",
            levelRequired = 96,
            xpPerAction = 380.0,
            actionTimeMs = 11800L,
            resourceId = "eternal_requiem",
            inputItems = mapOf("celestial_anthem" to 1, "chronicle_fateweave" to 1, "void_rune" to 3),
        ),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
