package com.arteria.game.core.data

import com.arteria.game.core.model.Achievement
import com.arteria.game.core.model.AchievementCategory
import com.arteria.game.core.model.AchievementCondition
import com.arteria.game.core.model.AchievementRarity
import com.arteria.game.core.skill.SkillId

object AchievementRegistry {

    val all: List<Achievement> = listOf(

        // ── Milestones ────────────────────────────────────────────────────────
        Achievement(
            id = "total_level_10",
            title = "First Steps",
            description = "Reach a total level of 10",
            category = AchievementCategory.MILESTONE,
            icon = "⭐",
            condition = AchievementCondition.TotalLevel(10),
            rarity = AchievementRarity.COMMON,
        ),
        Achievement(
            id = "total_level_50",
            title = "Getting Started",
            description = "Reach a total level of 50",
            category = AchievementCategory.MILESTONE,
            icon = "🌟",
            condition = AchievementCondition.TotalLevel(50),
            rarity = AchievementRarity.COMMON,
        ),
        Achievement(
            id = "total_level_100",
            title = "Seasoned Adventurer",
            description = "Reach a total level of 100",
            category = AchievementCategory.MILESTONE,
            icon = "💫",
            condition = AchievementCondition.TotalLevel(100),
            rarity = AchievementRarity.UNCOMMON,
        ),
        Achievement(
            id = "total_level_250",
            title = "Veteran",
            description = "Reach a total level of 250",
            category = AchievementCategory.MILESTONE,
            icon = "🔥",
            condition = AchievementCondition.TotalLevel(250),
            rarity = AchievementRarity.RARE,
        ),
        Achievement(
            id = "total_level_500",
            title = "Master of Many",
            description = "Reach a total level of 500",
            category = AchievementCategory.MILESTONE,
            icon = "🌌",
            condition = AchievementCondition.TotalLevel(500),
            rarity = AchievementRarity.EPIC,
        ),
        Achievement(
            id = "first_50",
            title = "Halfway There",
            description = "Reach level 50 in any skill",
            category = AchievementCategory.MILESTONE,
            icon = "🎯",
            condition = AchievementCondition.AnySkillLevel(50),
            rarity = AchievementRarity.UNCOMMON,
        ),
        Achievement(
            id = "first_99",
            title = "Mastered",
            description = "Reach level 99 in any skill",
            category = AchievementCategory.MILESTONE,
            icon = "🏆",
            condition = AchievementCondition.AnySkillLevel(99),
            rarity = AchievementRarity.LEGENDARY,
        ),

        // ── Gathering — Mining ────────────────────────────────────────────────
        Achievement(
            id = "mining_10",
            title = "Rookie Miner",
            description = "Reach level 10 in Mining",
            category = AchievementCategory.GATHERING,
            icon = "⛏️",
            condition = AchievementCondition.SkillLevel(SkillId.MINING, 10),
            rarity = AchievementRarity.COMMON,
        ),
        Achievement(
            id = "mining_30",
            title = "Skilled Miner",
            description = "Reach level 30 in Mining",
            category = AchievementCategory.GATHERING,
            icon = "⛏️",
            condition = AchievementCondition.SkillLevel(SkillId.MINING, 30),
            rarity = AchievementRarity.UNCOMMON,
        ),
        Achievement(
            id = "mining_60",
            title = "Expert Miner",
            description = "Reach level 60 in Mining",
            category = AchievementCategory.GATHERING,
            icon = "⛏️",
            condition = AchievementCondition.SkillLevel(SkillId.MINING, 60),
            rarity = AchievementRarity.RARE,
        ),
        Achievement(
            id = "iron_ore_100",
            title = "Iron Stockpile",
            description = "Collect 100 iron ore",
            category = AchievementCategory.GATHERING,
            icon = "🪨",
            condition = AchievementCondition.ItemCollected("iron_ore", 100),
            rarity = AchievementRarity.COMMON,
        ),
        Achievement(
            id = "iron_ore_1000",
            title = "Iron Hoard",
            description = "Collect 1,000 iron ore",
            category = AchievementCategory.GATHERING,
            icon = "🪨",
            condition = AchievementCondition.ItemCollected("iron_ore", 1000),
            rarity = AchievementRarity.UNCOMMON,
        ),

        // ── Gathering — Logging ───────────────────────────────────────────────
        Achievement(
            id = "logging_10",
            title = "Rookie Logger",
            description = "Reach level 10 in Logging",
            category = AchievementCategory.GATHERING,
            icon = "🪓",
            condition = AchievementCondition.SkillLevel(SkillId.LOGGING, 10),
            rarity = AchievementRarity.COMMON,
        ),
        Achievement(
            id = "logging_30",
            title = "Skilled Logger",
            description = "Reach level 30 in Logging",
            category = AchievementCategory.GATHERING,
            icon = "🪓",
            condition = AchievementCondition.SkillLevel(SkillId.LOGGING, 30),
            rarity = AchievementRarity.UNCOMMON,
        ),
        Achievement(
            id = "logs_100",
            title = "Woodpile",
            description = "Collect 100 logs",
            category = AchievementCategory.GATHERING,
            icon = "🪵",
            condition = AchievementCondition.ItemCollected("logs", 100),
            rarity = AchievementRarity.COMMON,
        ),
        Achievement(
            id = "logs_1000",
            title = "Lumber Baron",
            description = "Collect 1,000 logs",
            category = AchievementCategory.GATHERING,
            icon = "🌲",
            condition = AchievementCondition.ItemCollected("logs", 1000),
            rarity = AchievementRarity.UNCOMMON,
        ),

        // ── Gathering — Fishing ───────────────────────────────────────────────
        Achievement(
            id = "fishing_10",
            title = "Rookie Fisher",
            description = "Reach level 10 in Fishing",
            category = AchievementCategory.GATHERING,
            icon = "🎣",
            condition = AchievementCondition.SkillLevel(SkillId.FISHING, 10),
            rarity = AchievementRarity.COMMON,
        ),
        Achievement(
            id = "fishing_30",
            title = "Skilled Fisher",
            description = "Reach level 30 in Fishing",
            category = AchievementCategory.GATHERING,
            icon = "🎣",
            condition = AchievementCondition.SkillLevel(SkillId.FISHING, 30),
            rarity = AchievementRarity.UNCOMMON,
        ),
        Achievement(
            id = "raw_fish_100",
            title = "The Catch",
            description = "Collect 100 raw fish",
            category = AchievementCategory.GATHERING,
            icon = "🐟",
            condition = AchievementCondition.ItemCollected("raw_shrimp", 100),
            rarity = AchievementRarity.COMMON,
        ),

        // ── Gathering — Harvesting ────────────────────────────────────────────
        Achievement(
            id = "harvesting_10",
            title = "Rookie Harvester",
            description = "Reach level 10 in Harvesting",
            category = AchievementCategory.GATHERING,
            icon = "🌿",
            condition = AchievementCondition.SkillLevel(SkillId.HARVESTING, 10),
            rarity = AchievementRarity.COMMON,
        ),
        Achievement(
            id = "harvesting_30",
            title = "Skilled Harvester",
            description = "Reach level 30 in Harvesting",
            category = AchievementCategory.GATHERING,
            icon = "🌿",
            condition = AchievementCondition.SkillLevel(SkillId.HARVESTING, 30),
            rarity = AchievementRarity.UNCOMMON,
        ),

        // ── Gathering — Scavenging ────────────────────────────────────────────
        Achievement(
            id = "scavenging_10",
            title = "Rookie Scavenger",
            description = "Reach level 10 in Scavenging",
            category = AchievementCategory.GATHERING,
            icon = "🔍",
            condition = AchievementCondition.SkillLevel(SkillId.SCAVENGING, 10),
            rarity = AchievementRarity.COMMON,
        ),

        // ── Crafting — Smithing ───────────────────────────────────────────────
        Achievement(
            id = "smithing_10",
            title = "Apprentice Smith",
            description = "Reach level 10 in Smithing",
            category = AchievementCategory.CRAFTING,
            icon = "🔨",
            condition = AchievementCondition.SkillLevel(SkillId.SMITHING, 10),
            rarity = AchievementRarity.COMMON,
        ),
        Achievement(
            id = "smithing_30",
            title = "Journeyman Smith",
            description = "Reach level 30 in Smithing",
            category = AchievementCategory.CRAFTING,
            icon = "🔨",
            condition = AchievementCondition.SkillLevel(SkillId.SMITHING, 30),
            rarity = AchievementRarity.UNCOMMON,
        ),
        Achievement(
            id = "iron_bar_50",
            title = "Iron Forged",
            description = "Smelt 50 iron bars",
            category = AchievementCategory.CRAFTING,
            icon = "⚙️",
            condition = AchievementCondition.ItemCollected("iron_bar", 50),
            rarity = AchievementRarity.COMMON,
        ),

        // ── Crafting — Cooking ────────────────────────────────────────────────
        Achievement(
            id = "cooking_10",
            title = "Kitchen Novice",
            description = "Reach level 10 in Cooking",
            category = AchievementCategory.CRAFTING,
            icon = "🍳",
            condition = AchievementCondition.SkillLevel(SkillId.COOKING, 10),
            rarity = AchievementRarity.COMMON,
        ),
        Achievement(
            id = "cooking_30",
            title = "Line Cook",
            description = "Reach level 30 in Cooking",
            category = AchievementCategory.CRAFTING,
            icon = "🍳",
            condition = AchievementCondition.SkillLevel(SkillId.COOKING, 30),
            rarity = AchievementRarity.UNCOMMON,
        ),
        Achievement(
            id = "cooked_shrimp_50",
            title = "Shrimp Platter",
            description = "Cook 50 shrimp",
            category = AchievementCategory.CRAFTING,
            icon = "🍤",
            condition = AchievementCondition.ItemCollected("cooked_shrimp", 50),
            rarity = AchievementRarity.COMMON,
        ),

        // ── Crafting — Herblore ───────────────────────────────────────────────
        Achievement(
            id = "herblore_10",
            title = "Apprentice Alchemist",
            description = "Reach level 10 in Herblore",
            category = AchievementCategory.CRAFTING,
            icon = "🧪",
            condition = AchievementCondition.SkillLevel(SkillId.HERBLORE, 10),
            rarity = AchievementRarity.COMMON,
        ),
        Achievement(
            id = "herblore_30",
            title = "Journeyman Alchemist",
            description = "Reach level 30 in Herblore",
            category = AchievementCategory.CRAFTING,
            icon = "🧪",
            condition = AchievementCondition.SkillLevel(SkillId.HERBLORE, 30),
            rarity = AchievementRarity.UNCOMMON,
        ),

        // ── Banking ────────────────────────────────────────────────────────────
        Achievement(
            id = "bank_50",
            title = "Getting Stocked",
            description = "Have 50 items in your bank",
            category = AchievementCategory.BANKING,
            icon = "📦",
            condition = AchievementCondition.BankItems(50),
            rarity = AchievementRarity.COMMON,
        ),
        Achievement(
            id = "bank_250",
            title = "Hoarder",
            description = "Have 250 items in your bank",
            category = AchievementCategory.BANKING,
            icon = "🏦",
            condition = AchievementCondition.BankItems(250),
            rarity = AchievementRarity.COMMON,
        ),
        Achievement(
            id = "bank_500",
            title = "Collector",
            description = "Have 500 items in your bank",
            category = AchievementCategory.BANKING,
            icon = "💰",
            condition = AchievementCondition.BankItems(500),
            rarity = AchievementRarity.UNCOMMON,
        ),
        Achievement(
            id = "bank_1000",
            title = "Tycoon",
            description = "Have 1,000 items in your bank",
            category = AchievementCategory.BANKING,
            icon = "👑",
            condition = AchievementCondition.BankItems(1000),
            rarity = AchievementRarity.RARE,
        ),
        Achievement(
            id = "bank_5000",
            title = "The Vault",
            description = "Have 5,000 items in your bank",
            category = AchievementCategory.BANKING,
            icon = "🏰",
            condition = AchievementCondition.BankItems(5000),
            rarity = AchievementRarity.EPIC,
        ),

        // ── Exploration / other ────────────────────────────────────────────────
        Achievement(
            id = "skilled_multi",
            title = "Skilled Multi",
            description = "Train 5 different skills to level 10",
            category = AchievementCategory.EXPLORATION,
            icon = "🗺️",
            condition = AchievementCondition.TotalLevel(50),  // proxy: 5 skills × 10
            rarity = AchievementRarity.UNCOMMON,
        ),
    )

    fun getById(id: String): Achievement? = all.find { it.id == id }

    fun getRequiredProgress(condition: AchievementCondition): Int = when (condition) {
        is AchievementCondition.SkillLevel    -> condition.level
        is AchievementCondition.AnySkillLevel -> condition.level
        is AchievementCondition.TotalLevel    -> condition.level
        is AchievementCondition.ItemCollected -> condition.amount
        is AchievementCondition.BankItems     -> condition.count
        is AchievementCondition.SkillActions  -> condition.actions
    }
}
