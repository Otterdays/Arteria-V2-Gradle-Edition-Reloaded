package com.arteria.game.core.model

import com.arteria.game.core.skill.SkillId

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val category: AchievementCategory,
    val icon: String,
    val condition: AchievementCondition,
)

enum class AchievementCategory {
    MILESTONE,
    GATHERING,
    CRAFTING,
    BANKING,
    COMBAT,
    EXPLORATION,
}

sealed class AchievementCondition {
    data class SkillLevel(val skillId: SkillId, val level: Int) : AchievementCondition()
    data class TotalLevel(val level: Int) : AchievementCondition()
    data class ItemCollected(val itemId: String, val amount: Int) : AchievementCondition()
    data class BankItems(val count: Int) : AchievementCondition()
    data class SkillActions(val skillId: SkillId, val actions: Int) : AchievementCondition()
}

data class AchievementProgress(
    val achievementId: String,
    val isUnlocked: Boolean,
    val currentProgress: Int,
    val requiredProgress: Int,
    val unlockedAt: Long = 0L,
)
