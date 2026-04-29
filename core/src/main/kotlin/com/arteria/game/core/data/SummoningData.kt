package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

// NOTE: Summoning follows the pouch loop: combat charms + spirit shards + simple catalysts.
object SummoningData {
    val GOLD_CHARM = ItemDef("gold_charm", "Gold Charm", "A warm charm for novice familiar pouches")
    val GREEN_CHARM = ItemDef("green_charm", "Green Charm", "A living charm suited to skilling familiars")
    val CRIMSON_CHARM = ItemDef("crimson_charm", "Crimson Charm", "A fierce charm used for combat familiars")
    val BLUE_CHARM = ItemDef("blue_charm", "Blue Charm", "A rare charm that binds elder spirits")
    val SPIRIT_SHARD = ItemDef("spirit_shard", "Spirit Shard", "A small anchor for familiar essence")

    val SPIRIT_SPRITE_POUCH = ItemDef(
        "spirit_sprite_pouch",
        "Spirit Sprite Pouch",
        "A beginner pouch that calls a tiny mining sprite",
    )
    val FOREST_IMP_POUCH = ItemDef("forest_imp_pouch", "Forest Imp Pouch", "A pouch that binds a helpful forest imp")
    val TIDE_PUP_POUCH = ItemDef("tide_pup_pouch", "Tide Pup Pouch", "A damp pouch that splashes when shaken")
    val EMBER_FOX_POUCH = ItemDef("ember_fox_pouch", "Ember Fox Pouch", "A heated pouch for a smithing familiar")
    val HERB_DRYAD_POUCH = ItemDef("herb_dryad_pouch", "Herb Dryad Pouch", "A living pouch stitched with herb scent")
    val VOID_WISP_POUCH = ItemDef("void_wisp_pouch", "Void Wisp Pouch", "A dim pouch humming between worlds")
    val AETHER_DRAGON_POUCH = ItemDef(
        "aether_dragon_pouch",
        "Aether Dragon Pouch",
        "A high-tier pouch with a miniature dragon soul",
    )
    val PRIMORDIAL_TITAN_POUCH = ItemDef(
        "primordial_titan_pouch",
        "Primordial Titan Pouch",
        "A legendary binding for an ancient ally",
    )

    val items: Map<String, ItemDef> = listOf(
        GOLD_CHARM,
        GREEN_CHARM,
        CRIMSON_CHARM,
        BLUE_CHARM,
        SPIRIT_SHARD,
        SPIRIT_SPRITE_POUCH,
        FOREST_IMP_POUCH,
        TIDE_PUP_POUCH,
        EMBER_FOX_POUCH,
        HERB_DRYAD_POUCH,
        VOID_WISP_POUCH,
        AETHER_DRAGON_POUCH,
        PRIMORDIAL_TITAN_POUCH,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction(
            id = "summon_spirit_sprite_pouch",
            skillId = SkillId.SUMMONING,
            name = "Infuse Spirit Sprite Pouch",
            levelRequired = 1,
            xpPerAction = 24.0,
            actionTimeMs = 2600L,
            resourceId = "spirit_sprite_pouch",
            inputItems = mapOf("gold_charm" to 1, "spirit_shard" to 4, "rat_tail" to 1),
        ),
        SkillAction(
            id = "summon_forest_imp_pouch",
            skillId = SkillId.SUMMONING,
            name = "Infuse Forest Imp Pouch",
            levelRequired = 8,
            xpPerAction = 38.0,
            actionTimeMs = 3000L,
            resourceId = "forest_imp_pouch",
            inputItems = mapOf("gold_charm" to 1, "spirit_shard" to 7, "oak_logs" to 1),
        ),
        SkillAction(
            id = "summon_tide_pup_pouch",
            skillId = SkillId.SUMMONING,
            name = "Infuse Tide Pup Pouch",
            levelRequired = 18,
            xpPerAction = 62.0,
            actionTimeMs = 3800L,
            resourceId = "tide_pup_pouch",
            inputItems = mapOf("green_charm" to 1, "spirit_shard" to 12, "trout" to 1),
        ),
        SkillAction(
            id = "summon_ember_fox_pouch",
            skillId = SkillId.SUMMONING,
            name = "Infuse Ember Fox Pouch",
            levelRequired = 32,
            xpPerAction = 96.0,
            actionTimeMs = 4800L,
            resourceId = "ember_fox_pouch",
            inputItems = mapOf("crimson_charm" to 1, "spirit_shard" to 20, "iron_bar" to 1),
        ),
        SkillAction(
            id = "summon_herb_dryad_pouch",
            skillId = SkillId.SUMMONING,
            name = "Infuse Herb Dryad Pouch",
            levelRequired = 46,
            xpPerAction = 140.0,
            actionTimeMs = 5800L,
            resourceId = "herb_dryad_pouch",
            inputItems = mapOf("green_charm" to 2, "spirit_shard" to 30, "sage_leaves" to 1),
        ),
        SkillAction(
            id = "summon_void_wisp_pouch",
            skillId = SkillId.SUMMONING,
            name = "Infuse Void Wisp Pouch",
            levelRequired = 60,
            xpPerAction = 210.0,
            actionTimeMs = 7200L,
            resourceId = "void_wisp_pouch",
            inputItems = mapOf("crimson_charm" to 2, "spirit_shard" to 45, "void_shard" to 2),
        ),
        SkillAction(
            id = "summon_aether_dragon_pouch",
            skillId = SkillId.SUMMONING,
            name = "Infuse Aether Dragon Pouch",
            levelRequired = 78,
            xpPerAction = 330.0,
            actionTimeMs = 9000L,
            resourceId = "aether_dragon_pouch",
            inputItems = mapOf("blue_charm" to 1, "spirit_shard" to 75, "aether_rune" to 2),
        ),
        SkillAction(
            id = "summon_primordial_titan_pouch",
            skillId = SkillId.SUMMONING,
            name = "Infuse Primordial Titan Pouch",
            levelRequired = 92,
            xpPerAction = 520.0,
            actionTimeMs = 12000L,
            resourceId = "primordial_titan_pouch",
            inputItems = mapOf("blue_charm" to 2, "spirit_shard" to 120, "void_rune" to 3),
        ),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
