package com.arteria.game.core.data

import com.arteria.game.core.model.ItemDef
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.skill.SkillId

// NOTE: Enchanting consumes runes and crafted gear for empowered variants.
object EnchantingData {
    val ENCHANTED_SHORTBOW = ItemDef(
        "enchanted_shortbow",
        "Enchanted Shortbow",
        "A shortbow infused with stable arcane threads",
    )
    val ENCHANTED_OAK_LONGBOW = ItemDef(
        "enchanted_oak_longbow",
        "Enchanted Oak Longbow",
        "Oak longbow with enhanced draw resonance",
    )
    val ENCHANTED_MAGIC_STAFF = ItemDef(
        "enchanted_magic_staff",
        "Enchanted Magic Staff",
        "A staff with amplified spell channeling",
    )
    val WARDING_AMULET = ItemDef(
        "warding_amulet",
        "Warding Amulet",
        "A protective amulet that blunts hostile magic",
    )
    val RING_OF_SWIFTNESS = ItemDef(
        "ring_of_swiftness",
        "Ring of Swiftness",
        "A ring that subtly accelerates action flow",
    )
    val RUNEBOUND_ARMOR = ItemDef(
        "runebound_armor",
        "Runebound Armor",
        "Armor etched with defensive rune circuits",
    )
    val AETHER_SIGIL = ItemDef(
        "aether_sigil",
        "Aether Sigil",
        "A focused sigil for high-tier enchant routines",
    )
    val CELESTIAL_RELIC = ItemDef(
        "celestial_relic",
        "Celestial Relic",
        "A rare relic containing layered enchant matrices",
    )

    val items: Map<String, ItemDef> = listOf(
        ENCHANTED_SHORTBOW,
        ENCHANTED_OAK_LONGBOW,
        ENCHANTED_MAGIC_STAFF,
        WARDING_AMULET,
        RING_OF_SWIFTNESS,
        RUNEBOUND_ARMOR,
        AETHER_SIGIL,
        CELESTIAL_RELIC,
    ).associateBy { it.id }

    val actions: List<SkillAction> = listOf(
        SkillAction(
            id = "enchant_shortbow",
            skillId = SkillId.ENCHANTING,
            name = "Enchant Shortbow",
            levelRequired = 1,
            xpPerAction = 24.0,
            actionTimeMs = 2800L,
            resourceId = "enchanted_shortbow",
            inputItems = mapOf("shortbow" to 1, "air_rune" to 8),
        ),
        SkillAction(
            id = "enchant_oak_longbow",
            skillId = SkillId.ENCHANTING,
            name = "Enchant Oak Longbow",
            levelRequired = 16,
            xpPerAction = 42.0,
            actionTimeMs = 3600L,
            resourceId = "enchanted_oak_longbow",
            inputItems = mapOf("oak_longbow" to 1, "earth_rune" to 8),
        ),
        SkillAction(
            id = "enchant_magic_staff",
            skillId = SkillId.ENCHANTING,
            name = "Enchant Magic Staff",
            levelRequired = 30,
            xpPerAction = 66.0,
            actionTimeMs = 4500L,
            resourceId = "enchanted_magic_staff",
            inputItems = mapOf("magic_staff" to 1, "water_rune" to 10),
        ),
        SkillAction(
            id = "craft_warding_amulet",
            skillId = SkillId.ENCHANTING,
            name = "Craft Warding Amulet",
            levelRequired = 44,
            xpPerAction = 100.0,
            actionTimeMs = 5600L,
            resourceId = "warding_amulet",
            inputItems = mapOf("amulet_of_accuracy" to 1, "cosmic_rune" to 4),
        ),
        SkillAction(
            id = "craft_ring_of_swiftness",
            skillId = SkillId.ENCHANTING,
            name = "Craft Ring of Swiftness",
            levelRequired = 58,
            xpPerAction = 145.0,
            actionTimeMs = 6800L,
            resourceId = "ring_of_swiftness",
            inputItems = mapOf("ring_of_fortune" to 1, "chaos_rune" to 6),
        ),
        SkillAction(
            id = "bind_runebound_armor",
            skillId = SkillId.ENCHANTING,
            name = "Bind Runebound Armor",
            levelRequired = 72,
            xpPerAction = 205.0,
            actionTimeMs = 8200L,
            resourceId = "runebound_armor",
            inputItems = mapOf("iron_armor" to 1, "fire_rune" to 10, "water_rune" to 10),
        ),
        SkillAction(
            id = "inscribe_aether_sigil",
            skillId = SkillId.ENCHANTING,
            name = "Inscribe Aether Sigil",
            levelRequired = 86,
            xpPerAction = 295.0,
            actionTimeMs = 9800L,
            resourceId = "aether_sigil",
            inputItems = mapOf("aether_distillate" to 1, "aether_rune" to 4),
        ),
        SkillAction(
            id = "forge_celestial_relic",
            skillId = SkillId.ENCHANTING,
            name = "Forge Celestial Relic",
            levelRequired = 96,
            xpPerAction = 430.0,
            actionTimeMs = 11600L,
            resourceId = "celestial_relic",
            inputItems = mapOf("aether_sigil" to 1, "void_rune" to 4, "star_essence" to 1),
        ),
    )

    val actionRegistry: Map<String, SkillAction> = actions.associateBy { it.id }
}
