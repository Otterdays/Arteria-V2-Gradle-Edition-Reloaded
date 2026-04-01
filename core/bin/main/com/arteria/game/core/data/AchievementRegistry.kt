package com.arteria.game.core.data

import com.arteria.game.core.model.Achievement
import com.arteria.game.core.model.AchievementCategory
import com.arteria.game.core.model.AchievementCondition
import com.arteria.game.core.skill.SkillId

object AchievementRegistry {

    val all: List<Achievement> = listOf(
        // Milestones
        Achievement(
            id = "total_level_25",
            title = "Getting Started",
            description = "Reach a total level of 25",
            category = AchievementCategory.MILESTONE,
            icon = "⭐",
            condition = AchievementCondition.TotalLevel(25),
        ),
        Achievement(
            id = "total_level_50",
            title = "Seasoned Adventurer",
            description = "Reach a total level of 50",
            category = AchievementCategory.MILESTONE,
            icon = "🌟",
            condition = AchievementCondition.TotalLevel(50),
        ),
        Achievement(
            id = "total_level_100",
            title = "Veteran",
            description = "Reach a total level of 100",
            category = AchievementCategory.MILESTONE,
            icon = "💫",
            condition = AchievementCondition.TotalLevel(100),
        ),
        Achievement(
            id = "first_99",
            title = "Mastered",
            description = "Reach level 99 in any skill",
            category = AchievementCategory.MILESTONE,
            icon = "🏆",
            condition = AchievementCondition.SkillLevel(SkillId.MINING, 99),
        ),

        // Gathering
        Achievement(
            id = "mining_10",
            title = "Rookie Miner",
            description = "Reach level 10 in Mining",
            category = AchievementCategory.GATHERING,
            icon = "⛏️",
            condition = AchievementCondition.SkillLevel(SkillId.MINING, 10),
        ),
        Achievement(
            id = "mining_30",
            title = "Skilled Miner",
            description = "Reach level 30 in Mining",
            category = AchievementCategory.GATHERING,
            icon = "⛏️",
            condition = AchievementCondition.SkillLevel(SkillId.MINING, 30),
        ),
        Achievement(
            id = "logging_10",
            title = "Rookie Logger",
            description = "Reach level 10 in Logging",
            category = AchievementCategory.GATHERING,
            icon = "🪓",
            condition = AchievementCondition.SkillLevel(SkillId.LOGGING, 10),
        ),
        Achievement(
            id = "fishing_10",
            title = "Rookie Fisher",
            description = "Reach level 10 in Fishing",
            category = AchievementCategory.GATHERING,
            icon = "🎣",
            condition = AchievementCondition.SkillLevel(SkillId.FISHING, 10),
        ),
        Achievement(
            id = "harvesting_10",
            title = "Rookie Harvester",
            description = "Reach level 10 in Harvesting",
            category = AchievementCategory.GATHERING,
            icon = "🌿",
            condition = AchievementCondition.SkillLevel(SkillId.HARVESTING, 10),
        ),
        Achievement(
            id = "scavenging_10",
            title = "Rookie Scavenger",
            description = "Reach level 10 in Scavenging",
            category = AchievementCategory.GATHERING,
            icon = "🔍",
            condition = AchievementCondition.SkillLevel(SkillId.SCAVENGING, 10),
        ),

        // Crafting
        Achievement(
            id = "smithing_10",
            title = "Apprentice Smith",
            description = "Reach level 10 in Smithing",
            category = AchievementCategory.CRAFTING,
            icon = "🔨",
            condition = AchievementCondition.SkillLevel(SkillId.SMITHING, 10),
        ),
        Achievement(
            id = "cooking_10",
            title = "Kitchen Novice",
            description = "Reach level 10 in Cooking",
            category = AchievementCategory.CRAFTING,
            icon = "🍳",
            condition = AchievementCondition.SkillLevel(SkillId.COOKING, 10),
        ),
        Achievement(
            id = "herblore_10",
            title = "Apprentice Alchemist",
            description = "Reach level 10 in Herblore",
            category = AchievementCategory.CRAFTING,
            icon = "🧪",
            condition = AchievementCondition.SkillLevel(SkillId.HERBLORE, 10),
        ),

        // Banking
        Achievement(
            id = "bank_100",
            title = "Hoarder",
            description = "Have 100 items in your bank",
            category = AchievementCategory.BANKING,
            icon = "🏦",
            condition = AchievementCondition.BankItems(100),
        ),
        Achievement(
            id = "bank_500",
            title = "Collector",
            description = "Have 500 items in your bank",
            category = AchievementCategory.BANKING,
            icon = "💰",
            condition = AchievementCondition.BankItems(500),
        ),
        Achievement(
            id = "bank_1000",
            title = "Tycoon",
            description = "Have 1000 items in your bank",
            category = AchievementCategory.BANKING,
            icon = "👑",
            condition = AchievementCondition.BankItems(1000),
        ),
        Achievement(
            id = "iron_ore_100",
            title = "Iron Stockpile",
            description = "Collect 100 iron ore",
            category = AchievementCategory.GATHERING,
            icon = "🪨",
            condition = AchievementCondition.ItemCollected("iron_ore", 100),
        ),
        Achievement(
            id = "logs_100",
            title = "Woodpile",
            description = "Collect 100 logs",
            category = AchievementCategory.GATHERING,
            icon = "🪵",
            condition = AchievementCondition.ItemCollected("logs", 100),
        ),
    )

    fun getById(id: String): Achievement? = all.find { it.id == id }

    fun getRequiredProgress(condition: AchievementCondition): Int = when (condition) {
        is AchievementCondition.SkillLevel -> condition.level
        is AchievementCondition.TotalLevel -> condition.level
        is AchievementCondition.ItemCollected -> condition.amount
        is AchievementCondition.BankItems -> condition.count
        is AchievementCondition.SkillActions -> condition.actions
    }
}
