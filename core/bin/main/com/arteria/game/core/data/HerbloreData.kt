package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

/**
 * Brews potions from [HarvestingData] botanicals. Inputs are consumed from the bank per tick (see [TickEngine]).
 */
object HerbloreData {
    val SIMPLE_REMEDY = ItemDef("simple_remedy", "Simple Remedy", "A basic flax-based soothing paste")
    val MARIGOLD_SALVE = ItemDef("marigold_salve", "Marigold Salve", "Petals steeped for minor restoration")
    val SAGE_INFUSION = ItemDef("sage_infusion", "Sage Infusion", "Clear-headed clarity in a bottle")
    val GINSENG_TONIC = ItemDef("ginseng_tonic", "Ginseng Tonic", "Earthy vigor for weary travelers")
    val NIGHTSHADE_TINCTURE = ItemDef(
        "nightshade_tincture",
        "Nightshade Tincture",
        "Potent and risky — experts only",
    )
    val MANDRAKE_DECOCTION = ItemDef(
        "mandrake_decoction",
        "Mandrake Decoction",
        "A brew that remembers the scream of the root",
    )
    val PHANTOM_PHILTER = ItemDef(
        "phantom_philter",
        "Phantom Philter",
        "Spores suspended in a ghostly suspension",
    )
    val STARLIGHT_SERUM = ItemDef(
        "starlight_serum",
        "Starlight Serum",
        "Liquid constellations for the worthy",
    )

    val items: Map<String, ItemDef> = listOf(
        SIMPLE_REMEDY,
        MARIGOLD_SALVE,
        SAGE_INFUSION,
        GINSENG_TONIC,
        NIGHTSHADE_TINCTURE,
        MANDRAKE_DECOCTION,
        PHANTOM_PHILTER,
        STARLIGHT_SERUM,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction(
            id = "brew_simple_remedy",
            skillId = SkillId.HERBLORE,
            name = "Brew Simple Remedy",
            levelRequired = 1,
            xpPerAction = 32.0,
            actionTimeMs = 2600L,
            resourceId = "simple_remedy",
            inputItems = mapOf("flax_bundle" to 1),
        ),
        SkillAction(
            id = "brew_marigold_salve",
            skillId = SkillId.HERBLORE,
            name = "Brew Marigold Salve",
            levelRequired = 15,
            xpPerAction = 48.0,
            actionTimeMs = 3200L,
            resourceId = "marigold_salve",
            inputItems = mapOf("marigold_petals" to 1),
        ),
        SkillAction(
            id = "brew_sage_infusion",
            skillId = SkillId.HERBLORE,
            name = "Brew Sage Infusion",
            levelRequired = 30,
            xpPerAction = 72.0,
            actionTimeMs = 4200L,
            resourceId = "sage_infusion",
            inputItems = mapOf("sage_leaves" to 1),
        ),
        SkillAction(
            id = "brew_ginseng_tonic",
            skillId = SkillId.HERBLORE,
            name = "Brew Ginseng Tonic",
            levelRequired = 45,
            xpPerAction = 105.0,
            actionTimeMs = 5200L,
            resourceId = "ginseng_tonic",
            inputItems = mapOf("ginseng_root" to 1),
        ),
        SkillAction(
            id = "brew_nightshade_tincture",
            skillId = SkillId.HERBLORE,
            name = "Brew Nightshade Tincture",
            levelRequired = 60,
            xpPerAction = 165.0,
            actionTimeMs = 6200L,
            resourceId = "nightshade_tincture",
            inputItems = mapOf("nightshade_berries" to 1),
        ),
        SkillAction(
            id = "brew_mandrake_decoction",
            skillId = SkillId.HERBLORE,
            name = "Brew Mandrake Decoction",
            levelRequired = 75,
            xpPerAction = 235.0,
            actionTimeMs = 7800L,
            resourceId = "mandrake_decoction",
            inputItems = mapOf("mandrake_root" to 1),
        ),
        SkillAction(
            id = "brew_phantom_philter",
            skillId = SkillId.HERBLORE,
            name = "Brew Phantom Philter",
            levelRequired = 85,
            xpPerAction = 350.0,
            actionTimeMs = 9600L,
            resourceId = "phantom_philter",
            inputItems = mapOf("phantom_spores" to 1),
        ),
        SkillAction(
            id = "brew_starlight_serum",
            skillId = SkillId.HERBLORE,
            name = "Brew Starlight Serum",
            levelRequired = 95,
            xpPerAction = 460.0,
            actionTimeMs = 12000L,
            resourceId = "starlight_serum",
            inputItems = mapOf("star_herb" to 1),
        ),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
