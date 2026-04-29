package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

// NOTE: Construction consumes processed materials and outputs structure components.
object ConstructionData {
    val WOODEN_FRAME = ItemDef("wooden_frame", "Wooden Frame", "A basic frame for simple structures")
    val STORAGE_CRATE = ItemDef("storage_crate", "Storage Crate", "A reinforced crate for stockpiling goods")
    val WORKBENCH_KIT = ItemDef("workbench_kit", "Workbench Kit", "Portable crafting bench components")
    val REINFORCED_DOOR = ItemDef("reinforced_door", "Reinforced Door", "A heavy door plated with metal")
    val ARCANE_SHELF = ItemDef("arcane_shelf", "Arcane Shelf", "Runed shelving for reagents and tomes")
    val WATCHTOWER_KIT = ItemDef("watchtower_kit", "Watchtower Kit", "Packaged supports for elevated defense")
    val GUILD_HALL_BEAM = ItemDef("guild_hall_beam", "Guild Hall Beam", "Massive beam for grand structures")
    val CELESTIAL_OBSERVATORY = ItemDef(
        "celestial_observatory",
        "Celestial Observatory",
        "A high-end structure module for cosmic study",
    )

    val items: Map<String, ItemDef> = listOf(
        WOODEN_FRAME,
        STORAGE_CRATE,
        WORKBENCH_KIT,
        REINFORCED_DOOR,
        ARCANE_SHELF,
        WATCHTOWER_KIT,
        GUILD_HALL_BEAM,
        CELESTIAL_OBSERVATORY,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction(
            id = "build_wooden_frame",
            skillId = SkillId.CONSTRUCTION,
            name = "Build Wooden Frame",
            levelRequired = 1,
            xpPerAction = 20.0,
            actionTimeMs = 3200L,
            resourceId = "wooden_frame",
            inputItems = mapOf("plank" to 2),
        ),
        SkillAction(
            id = "build_storage_crate",
            skillId = SkillId.CONSTRUCTION,
            name = "Build Storage Crate",
            levelRequired = 12,
            xpPerAction = 35.0,
            actionTimeMs = 4200L,
            resourceId = "storage_crate",
            inputItems = mapOf("oak_plank" to 2, "bronze_bar" to 1),
        ),
        SkillAction(
            id = "assemble_workbench_kit",
            skillId = SkillId.CONSTRUCTION,
            name = "Assemble Workbench Kit",
            levelRequired = 24,
            xpPerAction = 56.0,
            actionTimeMs = 5200L,
            resourceId = "workbench_kit",
            inputItems = mapOf("willow_plank" to 2, "iron_bar" to 1),
        ),
        SkillAction(
            id = "forge_reinforced_door",
            skillId = SkillId.CONSTRUCTION,
            name = "Forge Reinforced Door",
            levelRequired = 38,
            xpPerAction = 86.0,
            actionTimeMs = 6400L,
            resourceId = "reinforced_door",
            inputItems = mapOf("maple_plank" to 2, "steel_bar" to 1),
        ),
        SkillAction(
            id = "build_arcane_shelf",
            skillId = SkillId.CONSTRUCTION,
            name = "Build Arcane Shelf",
            levelRequired = 52,
            xpPerAction = 126.0,
            actionTimeMs = 7600L,
            resourceId = "arcane_shelf",
            inputItems = mapOf("yew_shield" to 1, "cosmic_rune" to 4),
        ),
        SkillAction(
            id = "assemble_watchtower_kit",
            skillId = SkillId.CONSTRUCTION,
            name = "Assemble Watchtower Kit",
            levelRequired = 67,
            xpPerAction = 182.0,
            actionTimeMs = 9000L,
            resourceId = "watchtower_kit",
            inputItems = mapOf("magic_staff" to 1, "adamant_bar" to 1),
        ),
        SkillAction(
            id = "craft_guild_hall_beam",
            skillId = SkillId.CONSTRUCTION,
            name = "Craft Guild Hall Beam",
            levelRequired = 82,
            xpPerAction = 265.0,
            actionTimeMs = 10800L,
            resourceId = "guild_hall_beam",
            inputItems = mapOf("elder_frame" to 1, "runite_bar" to 1),
        ),
        SkillAction(
            id = "construct_celestial_observatory",
            skillId = SkillId.CONSTRUCTION,
            name = "Construct Celestial Observatory",
            levelRequired = 94,
            xpPerAction = 390.0,
            actionTimeMs = 13200L,
            resourceId = "celestial_observatory",
            inputItems = mapOf("guild_hall_beam" to 1, "star_essence" to 1, "void_rune" to 4),
        ),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
