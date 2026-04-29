package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

// NOTE: Runecrafting consumes scavenged void shards and cut gems.
object RunecraftingData {
    val AIR_RUNE = ItemDef("air_rune", "Air Rune", "A light rune used for wind spells")
    val WATER_RUNE = ItemDef("water_rune", "Water Rune", "A fluid rune used for water spells")
    val EARTH_RUNE = ItemDef("earth_rune", "Earth Rune", "A stable rune used for earth spells")
    val FIRE_RUNE = ItemDef("fire_rune", "Fire Rune", "A volatile rune used for fire spells")
    val CHAOS_RUNE = ItemDef("chaos_rune", "Chaos Rune", "An unstable rune for chaotic magic")
    val COSMIC_RUNE = ItemDef("cosmic_rune", "Cosmic Rune", "A high-tier rune for planar magic")
    val AETHER_RUNE = ItemDef("aether_rune", "Aether Rune", "A rune stitched with aether flow")
    val VOID_RUNE = ItemDef("void_rune", "Void Rune", "A rare rune forged from deep void")

    val items: Map<String, ItemDef> = listOf(
        AIR_RUNE,
        WATER_RUNE,
        EARTH_RUNE,
        FIRE_RUNE,
        CHAOS_RUNE,
        COSMIC_RUNE,
        AETHER_RUNE,
        VOID_RUNE,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction(
            id = "craft_air_rune",
            skillId = SkillId.RUNECRAFTING,
            name = "Air Altar Sigil",
            levelRequired = 1,
            xpPerAction = 22.0,
            actionTimeMs = 2600L,
            resourceId = "air_rune",
            resourceAmount = 6,
            inputItems = mapOf("void_shard" to 1),
        ),
        SkillAction(
            id = "craft_water_rune",
            skillId = SkillId.RUNECRAFTING,
            name = "Water Altar Sigil",
            levelRequired = 12,
            xpPerAction = 35.0,
            actionTimeMs = 3200L,
            resourceId = "water_rune",
            resourceAmount = 6,
            inputItems = mapOf("void_shard" to 1),
        ),
        SkillAction(
            id = "craft_earth_rune",
            skillId = SkillId.RUNECRAFTING,
            name = "Earth Altar Sigil",
            levelRequired = 25,
            xpPerAction = 52.0,
            actionTimeMs = 3800L,
            resourceId = "earth_rune",
            resourceAmount = 6,
            inputItems = mapOf("void_shard" to 1),
        ),
        SkillAction(
            id = "craft_fire_rune",
            skillId = SkillId.RUNECRAFTING,
            name = "Fire Altar Sigil",
            levelRequired = 40,
            xpPerAction = 76.0,
            actionTimeMs = 4600L,
            resourceId = "fire_rune",
            resourceAmount = 7,
            inputItems = mapOf("void_shard" to 1),
        ),
        SkillAction(
            id = "craft_chaos_rune",
            skillId = SkillId.RUNECRAFTING,
            name = "Chaos Sigil",
            levelRequired = 55,
            xpPerAction = 110.0,
            actionTimeMs = 5400L,
            resourceId = "chaos_rune",
            resourceAmount = 5,
            inputItems = mapOf("void_shard" to 2),
        ),
        SkillAction(
            id = "craft_cosmic_rune",
            skillId = SkillId.RUNECRAFTING,
            name = "Cosmic Sigil",
            levelRequired = 70,
            xpPerAction = 155.0,
            actionTimeMs = 6400L,
            resourceId = "cosmic_rune",
            resourceAmount = 4,
            inputItems = mapOf("void_shard" to 2, "cut_sapphire" to 1),
        ),
        SkillAction(
            id = "craft_aether_rune",
            skillId = SkillId.RUNECRAFTING,
            name = "Aether Loom",
            levelRequired = 82,
            xpPerAction = 225.0,
            actionTimeMs = 7600L,
            resourceId = "aether_rune",
            resourceAmount = 4,
            inputItems = mapOf("void_shard" to 2, "cut_emerald" to 1),
        ),
        SkillAction(
            id = "craft_void_rune",
            skillId = SkillId.RUNECRAFTING,
            name = "Void Nexus",
            levelRequired = 92,
            xpPerAction = 340.0,
            actionTimeMs = 9200L,
            resourceId = "void_rune",
            resourceAmount = 3,
            inputItems = mapOf("void_shard" to 3, "cut_ruby" to 1),
        ),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
