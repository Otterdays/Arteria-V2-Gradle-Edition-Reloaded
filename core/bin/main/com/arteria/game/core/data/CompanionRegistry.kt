package com.arteria.game.core.data

import com.arteria.game.core.model.Companion
import com.arteria.game.core.model.CompanionBonus
import com.arteria.game.core.model.CompanionRarity
import com.arteria.game.core.skill.SkillId

object CompanionRegistry {

    val all: List<Companion> = listOf(
        // Common
        Companion(
            id = "spirit_sprite",
            name = "Spirit Sprite",
            description = "A tiny glowing spirit that boosts mining speed",
            icon = "✨",
            rarity = CompanionRarity.COMMON,
            passiveBonus = CompanionBonus.XpBoost(SkillId.MINING, 1.05),
            levelRequired = 1,
        ),
        Companion(
            id = "forest_imp",
            name = "Forest Imp",
            description = "A mischievous imp that helps with logging",
            icon = "🧚",
            rarity = CompanionRarity.COMMON,
            passiveBonus = CompanionBonus.XpBoost(SkillId.LOGGING, 1.05),
            levelRequired = 1,
        ),
        Companion(
            id = "tide_pup",
            name = "Tide Pup",
            description = "A small water creature that attracts fish",
            icon = "🐟",
            rarity = CompanionRarity.COMMON,
            passiveBonus = CompanionBonus.XpBoost(SkillId.FISHING, 1.05),
            levelRequired = 5,
        ),

        // Uncommon
        Companion(
            id = "ember_fox",
            name = "Ember Fox",
            description = "A fox wreathed in flames, boosts smithing",
            icon = "🦊",
            rarity = CompanionRarity.UNCOMMON,
            passiveBonus = CompanionBonus.XpBoost(SkillId.SMITHING, 1.08),
            levelRequired = 10,
        ),
        Companion(
            id = "herb_dryad",
            name = "Herb Dryad",
            description = "A plant spirit that enhances herblore",
            icon = "🌱",
            rarity = CompanionRarity.UNCOMMON,
            passiveBonus = CompanionBonus.XpBoost(SkillId.HERBLORE, 1.08),
            levelRequired = 10,
        ),
        Companion(
            id = "cook_spirit",
            name = "Kitchen Spirit",
            description = "A helpful ghost that prevents burning food",
            icon = "👻",
            rarity = CompanionRarity.UNCOMMON,
            passiveBonus = CompanionBonus.ResourceBoost("cooked_fish", 0.1),
            levelRequired = 10,
        ),

        // Rare
        Companion(
            id = "void_wisp",
            name = "Void Wisp",
            description = "A creature from between dimensions, boosts offline efficiency",
            icon = "🌑",
            rarity = CompanionRarity.RARE,
            passiveBonus = CompanionBonus.OfflineEfficiency(1.10),
            levelRequired = 20,
        ),
        Companion(
            id = "chrono_beetle",
            name = "Chrono Beetle",
            description = "A time-bending insect that slows offline decay",
            icon = "🪲",
            rarity = CompanionRarity.RARE,
            passiveBonus = CompanionBonus.OfflineEfficiency(1.15),
            levelRequired = 25,
        ),

        // Epic
        Companion(
            id = "aether_dragon",
            name = "Aether Dragon",
            description = "A miniature dragon that boosts all XP gains",
            icon = "🐉",
            rarity = CompanionRarity.EPIC,
            passiveBonus = CompanionBonus.XpBoost(null, 1.10),
            levelRequired = 30,
        ),
        Companion(
            id = "celestial_owl",
            name = "Celestial Owl",
            description = "An owl of the stars, grants extra bank capacity",
            icon = "🦉",
            rarity = CompanionRarity.EPIC,
            passiveBonus = CompanionBonus.BankCapacity(10),
            levelRequired = 35,
        ),

        // Legendary
        Companion(
            id = "primordial_titan",
            name = "Primordial Titan",
            description = "An ancient being that boosts all skills significantly",
            icon = "👑",
            rarity = CompanionRarity.LEGENDARY,
            passiveBonus = CompanionBonus.XpBoost(null, 1.20),
            levelRequired = 50,
        ),
    )

    fun getById(id: String): Companion? = all.find { it.id == id }

    fun getByRarity(rarity: CompanionRarity): List<Companion> = all.filter { it.rarity == rarity }

    fun getAvailableForLevel(level: Int): List<Companion> = all.filter { it.levelRequired <= level }

    private val totalWeight = all.sumOf { companion ->
        when (companion.rarity) {
            CompanionRarity.COMMON -> 10
            CompanionRarity.UNCOMMON -> 6
            CompanionRarity.RARE -> 3
            CompanionRarity.EPIC -> 1
            CompanionRarity.LEGENDARY -> 1
        }
    }

    fun getRandom(): Companion {
        var roll = (0 until totalWeight).random()
        for (companion in all) {
            val weight = when (companion.rarity) {
                CompanionRarity.COMMON -> 10
                CompanionRarity.UNCOMMON -> 6
                CompanionRarity.RARE -> 3
                CompanionRarity.EPIC -> 1
                CompanionRarity.LEGENDARY -> 1
            }
            roll -= weight
            if (roll < 0) return companion
        }
        return all.last()
    }
}
