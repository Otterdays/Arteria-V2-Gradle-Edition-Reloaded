package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

object HarvestingData {
    val FLAX_BUNDLE = ItemDef("flax_bundle", "Flax Bundle", "Soft fibers for twine and cloth")
    val MARIGOLD_PETALS = ItemDef("marigold_petals", "Marigold Petals", "Bright petals with mild restorative oils")
    val SAGE_LEAVES = ItemDef("sage_leaves", "Sage Leaves", "Aromatic leaves prized by cooks and herbalists")
    val GINSENG_ROOT = ItemDef("ginseng_root", "Ginseng Root", "Twisted root said to restore vigor")
    val NIGHTSHADE_BERRIES = ItemDef(
        "nightshade_berries",
        "Nightshade Berries",
        "Toxic berries — handle with care",
    )
    val MANDRAKE_ROOT = ItemDef(
        "mandrake_root",
        "Mandrake Root",
        "Roots that mutter when pulled from the soil",
    )
    val PHANTOM_SPORES = ItemDef(
        "phantom_spores",
        "Phantom Spores",
        "Luminescent spores from deep groves",
    )
    val STAR_HERB = ItemDef(
        "star_herb",
        "Star-Touched Herb",
        "Rare botanicals that drank starlight",
    )

    val items: Map<String, ItemDef> = listOf(
        FLAX_BUNDLE,
        MARIGOLD_PETALS,
        SAGE_LEAVES,
        GINSENG_ROOT,
        NIGHTSHADE_BERRIES,
        MANDRAKE_ROOT,
        PHANTOM_SPORES,
        STAR_HERB,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction("harvest_flax", SkillId.HARVESTING, "Flax Patch", 1, 25.0, 3000L, "flax_bundle"),
        SkillAction(
            "harvest_marigold",
            SkillId.HARVESTING,
            "Marigold Field",
            15,
            37.5,
            4000L,
            "marigold_petals",
        ),
        SkillAction("harvest_sage", SkillId.HARVESTING, "Sage Grove", 30, 67.5, 5400L, "sage_leaves"),
        SkillAction(
            "harvest_ginseng",
            SkillId.HARVESTING,
            "Ginseng Hollow",
            45,
            100.0,
            6000L,
            "ginseng_root",
        ),
        SkillAction(
            "harvest_nightshade",
            SkillId.HARVESTING,
            "Nightshade Thicket",
            60,
            175.0,
            7200L,
            "nightshade_berries",
        ),
        SkillAction(
            "harvest_mandrake",
            SkillId.HARVESTING,
            "Mandrake Garden",
            75,
            250.0,
            9000L,
            "mandrake_root",
        ),
        SkillAction(
            "harvest_phantom",
            SkillId.HARVESTING,
            "Phantom Grove",
            85,
            380.0,
            10800L,
            "phantom_spores",
        ),
        SkillAction(
            "harvest_star_herb",
            SkillId.HARVESTING,
            "Starfall Glade",
            95,
            500.0,
            14400L,
            "star_herb",
        ),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
