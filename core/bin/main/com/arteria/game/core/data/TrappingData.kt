package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

// NOTE: Trapping converts field bait/materials into meat and pelts.
object TrappingData {
    val RABBIT_PELT = ItemDef("rabbit_pelt", "Rabbit Pelt", "Soft pelt from small woodland game")
    val RAW_RABBIT = ItemDef("raw_rabbit", "Raw Rabbit", "Lean game meat from snare catches")
    val FOX_PELT = ItemDef("fox_pelt", "Fox Pelt", "A sleek pelt prized by artisans")
    val RAW_GAME_BIRD = ItemDef("raw_game_bird", "Raw Game Bird", "Freshly trapped wild fowl")
    val BOAR_HIDE = ItemDef("boar_hide", "Boar Hide", "Tough hide from aggressive forest boars")
    val RAW_BOAR = ItemDef("raw_boar", "Raw Boar", "Dense meat from heavy game")
    val SHADOW_PELT = ItemDef("shadow_pelt", "Shadow Pelt", "A dark pelt that absorbs moonlight")
    val MYTHIC_HIDE = ItemDef("mythic_hide", "Mythic Hide", "Legendary hide from rare apex beasts")

    val items: Map<String, ItemDef> = listOf(
        RABBIT_PELT,
        RAW_RABBIT,
        FOX_PELT,
        RAW_GAME_BIRD,
        BOAR_HIDE,
        RAW_BOAR,
        SHADOW_PELT,
        MYTHIC_HIDE,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction(
            id = "set_rabbit_snare",
            skillId = SkillId.TRAPPING,
            name = "Rabbit Snare",
            levelRequired = 1,
            xpPerAction = 18.0,
            actionTimeMs = 3000L,
            resourceId = "raw_rabbit",
            inputItems = mapOf("rough_thread" to 1),
        ),
        SkillAction(
            id = "set_pelt_snare",
            skillId = SkillId.TRAPPING,
            name = "Pelt Snare",
            levelRequired = 12,
            xpPerAction = 32.0,
            actionTimeMs = 3600L,
            resourceId = "rabbit_pelt",
            inputItems = mapOf("rough_thread" to 1),
        ),
        SkillAction(
            id = "set_fox_trap",
            skillId = SkillId.TRAPPING,
            name = "Fox Trap",
            levelRequired = 26,
            xpPerAction = 52.0,
            actionTimeMs = 4600L,
            resourceId = "fox_pelt",
            inputItems = mapOf("woven_cloth" to 1, "wire_bundle" to 1),
        ),
        SkillAction(
            id = "set_bird_net",
            skillId = SkillId.TRAPPING,
            name = "Bird Net",
            levelRequired = 40,
            xpPerAction = 80.0,
            actionTimeMs = 5600L,
            resourceId = "raw_game_bird",
            inputItems = mapOf("woven_cloth" to 1, "normal_logs" to 1),
        ),
        SkillAction(
            id = "set_boar_pit",
            skillId = SkillId.TRAPPING,
            name = "Boar Pit",
            levelRequired = 55,
            xpPerAction = 118.0,
            actionTimeMs = 6800L,
            resourceId = "raw_boar",
            inputItems = mapOf("oak_plank" to 1, "iron_bar" to 1),
        ),
        SkillAction(
            id = "set_boar_hide_trap",
            skillId = SkillId.TRAPPING,
            name = "Boar Hide Trap",
            levelRequired = 68,
            xpPerAction = 170.0,
            actionTimeMs = 8200L,
            resourceId = "boar_hide",
            inputItems = mapOf("oak_plank" to 1, "steel_bar" to 1),
        ),
        SkillAction(
            id = "set_shadow_lure",
            skillId = SkillId.TRAPPING,
            name = "Shadow Lure",
            levelRequired = 84,
            xpPerAction = 250.0,
            actionTimeMs = 9800L,
            resourceId = "shadow_pelt",
            inputItems = mapOf("void_shard" to 1, "nightshade_tincture" to 1),
        ),
        SkillAction(
            id = "set_mythic_hunt",
            skillId = SkillId.TRAPPING,
            name = "Mythic Hunt Rig",
            levelRequired = 96,
            xpPerAction = 380.0,
            actionTimeMs = 11800L,
            resourceId = "mythic_hide",
            inputItems = mapOf("shadow_pelt" to 1, "chrono_regulator" to 1),
        ),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
