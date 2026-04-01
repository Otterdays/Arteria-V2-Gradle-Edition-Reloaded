package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

// NOTE: Tailoring uses flax_bundle (from Harvesting) as its primary input.
// Higher tiers also consume ginseng_root or mandrake_root for dye/treatment.

object TailoringData {
    val ROUGH_THREAD   = ItemDef("rough_thread",    "Rough Thread",    "Coarse thread spun from raw flax")
    val WOVEN_CLOTH    = ItemDef("woven_cloth",     "Woven Cloth",     "Simple cloth woven from rough thread")
    val LINEN_VEST     = ItemDef("linen_vest",      "Linen Vest",      "A basic linen vest, comfortable and light")
    val TRAVELLERS_CLOAK = ItemDef("travellers_cloak","Traveller's Cloak","A durable cloak for long journeys")
    val HERB_ROBE      = ItemDef("herb_robe",       "Herb Robe",       "A robe treated with herbal extracts")
    val MOON_SHROUD    = ItemDef("moon_shroud",     "Moon Shroud",     "A silk-smooth shroud woven at moonrise")
    val AETHER_WRAP    = ItemDef("aether_wrap",     "Aether Wrap",     "Fabric suffused with aetheric energy")
    val VOID_WEAVE     = ItemDef("void_weave",      "Void Weave",      "Cloth woven from threads of pure void")

    val items: Map<String, ItemDef> = listOf(
        ROUGH_THREAD, WOVEN_CLOTH, LINEN_VEST, TRAVELLERS_CLOAK,
        HERB_ROBE, MOON_SHROUD, AETHER_WRAP, VOID_WEAVE,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction(
            id = "spin_thread", skillId = SkillId.TAILORING, name = "Spin Thread",
            levelRequired = 1, xpPerAction = 13.0, actionTimeMs = 2400L,
            resourceId = "rough_thread",
            inputItems = mapOf("flax_bundle" to 1),
        ),
        SkillAction(
            id = "weave_cloth", skillId = SkillId.TAILORING, name = "Weave Cloth",
            levelRequired = 10, xpPerAction = 22.0, actionTimeMs = 3000L,
            resourceId = "woven_cloth",
            inputItems = mapOf("flax_bundle" to 2),
        ),
        SkillAction(
            id = "sew_linen_vest", skillId = SkillId.TAILORING, name = "Linen Vest",
            levelRequired = 20, xpPerAction = 38.0, actionTimeMs = 4200L,
            resourceId = "linen_vest",
            inputItems = mapOf("flax_bundle" to 3),
        ),
        SkillAction(
            id = "sew_travellers_cloak", skillId = SkillId.TAILORING, name = "Traveller's Cloak",
            levelRequired = 35, xpPerAction = 65.0, actionTimeMs = 5400L,
            resourceId = "travellers_cloak",
            inputItems = mapOf("flax_bundle" to 4),
        ),
        SkillAction(
            id = "sew_herb_robe", skillId = SkillId.TAILORING, name = "Herb Robe",
            levelRequired = 50, xpPerAction = 110.0, actionTimeMs = 6600L,
            resourceId = "herb_robe",
            inputItems = mapOf("flax_bundle" to 4, "sage_leaves" to 2),
        ),
        SkillAction(
            id = "weave_moon_shroud", skillId = SkillId.TAILORING, name = "Moon Shroud",
            levelRequired = 65, xpPerAction = 180.0, actionTimeMs = 8400L,
            resourceId = "moon_shroud",
            inputItems = mapOf("flax_bundle" to 5, "ginseng_root" to 1),
        ),
        SkillAction(
            id = "weave_aether_wrap", skillId = SkillId.TAILORING, name = "Aether Wrap",
            levelRequired = 80, xpPerAction = 280.0, actionTimeMs = 10800L,
            resourceId = "aether_wrap",
            inputItems = mapOf("flax_bundle" to 6, "mandrake_root" to 1),
        ),
        SkillAction(
            id = "weave_void_weave", skillId = SkillId.TAILORING, name = "Void Weave",
            levelRequired = 92, xpPerAction = 450.0, actionTimeMs = 14400L,
            resourceId = "void_weave",
            inputItems = mapOf("flax_bundle" to 8, "phantom_spores" to 1),
        ),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
