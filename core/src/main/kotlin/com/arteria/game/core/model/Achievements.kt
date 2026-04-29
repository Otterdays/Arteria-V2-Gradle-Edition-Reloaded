package com.arteria.game.core.model

import com.arteria.game.core.skill.SkillId

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val category: AchievementCategory,
    val icon: String,
    val condition: AchievementCondition,
    val rarity: AchievementRarity = AchievementRarity.COMMON,
)

enum class AchievementCategory(val displayName: String) {
    MILESTONE("Milestone"),
    GATHERING("Gathering"),
    CRAFTING("Crafting"),
    BANKING("Banking"),
    COMBAT("Combat"),
    EXPLORATION("Exploration"),
    RESONANCE("Resonance"),
}

enum class AchievementRarity(val label: String) {
    COMMON("Common"),
    UNCOMMON("Uncommon"),
    RARE("Rare"),
    EPIC("Epic"),
    LEGENDARY("Legendary"),
}

sealed class AchievementCondition {
    data class SkillLevel(val skillId: SkillId, val level: Int) : AchievementCondition()
    data class AnySkillLevel(val level: Int) : AchievementCondition()
    data class TotalLevel(val level: Int) : AchievementCondition()
    data class ItemCollected(val itemId: String, val amount: Int) : AchievementCondition()
    data class BankItems(val count: Int) : AchievementCondition()
    data class SkillActions(val skillId: SkillId, val actions: Int) : AchievementCondition()
    /** Lifetime normal Resonance pulses (see `GameState.totalResonancePulses`). */
    data class ResonancePulseTotal(val pulses: Int) : AchievementCondition()
    /** Lifetime Heavy Pulses (see `GameState.totalHeavyPulses`). */
    data class HeavyPulseTotal(val pulses: Int) : AchievementCondition()
    /** `GameState.peakMomentum` must reach at least this value (0–100 scale). */
    data class PeakMomentumReached(val minPeak: Int) : AchievementCondition()
}

data class AchievementProgress(
    val achievementId: String,
    val isUnlocked: Boolean,
    val currentProgress: Int,
    val requiredProgress: Int,
    val unlockedAt: Long = 0L,
)
