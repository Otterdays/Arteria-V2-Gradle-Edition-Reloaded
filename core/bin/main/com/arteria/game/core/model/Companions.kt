package com.arteria.game.core.model

import com.arteria.game.core.skill.SkillId

data class Companion(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val rarity: CompanionRarity,
    val passiveBonus: CompanionBonus,
    val skillRequired: SkillId? = null,
    val levelRequired: Int = 1,
)

enum class CompanionRarity {
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY,
}

sealed class CompanionBonus {
    data class XpBoost(val skillId: SkillId?, val multiplier: Double) : CompanionBonus()
    data class ResourceBoost(val itemId: String, val extraChance: Double) : CompanionBonus()
    data class OfflineEfficiency(val multiplier: Double) : CompanionBonus()
    data class BankCapacity(val extraSlots: Int) : CompanionBonus()
}
