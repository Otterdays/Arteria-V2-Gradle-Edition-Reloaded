package com.arteria.game.core.model

data class PrestigeState(
    val prestigeLevel: Int = 0,
    val totalPrestigePoints: Double = 0.0,
    val unlockedPerks: List<String> = emptyList(),
)

data class PrestigePerk(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val cost: Int,
    val maxLevel: Int,
    val effectPerLevel: String,
)

data class PrestigeOffering(
    val profileId: String,
    val currentTotalLevel: Int,
    val prestigePointsOnOffer: Double,
    val estimatedMultiplier: Double,
)

object PrestigePerks {
    val all: List<PrestigePerk> = listOf(
        PrestigePerk(
            id = "xp_boost",
            name = "Eternal Knowledge",
            description = "Permanent XP boost across all skills",
            icon = "📚",
            cost = 1,
            maxLevel = 10,
            effectPerLevel = "+5% All XP",
        ),
        PrestigePerk(
            id = "offline_efficiency",
            name = "Chronomancy Mastery",
            description = "Increase offline tick efficiency",
            icon = "⏳",
            cost = 2,
            maxLevel = 5,
            effectPerLevel = "+10% Offline Duration",
        ),
        PrestigePerk(
            id = "bank_capacity",
            name = "Expanded Vault",
            description = "Increase bank item capacity",
            icon = "🏦",
            cost = 1,
            maxLevel = 5,
            effectPerLevel = "+50 Bank Slots",
        ),
        PrestigePerk(
            id = "resource_bonus",
            name = "Fortune's Favor",
            description = "Chance for bonus resources per action",
            icon = "🍀",
            cost = 3,
            maxLevel = 5,
            effectPerLevel = "+3% Double Resources",
        ),
        PrestigePerk(
            id = "companion_slot",
            name = "Companion Bond",
            description = "Additional active companion slot",
            icon = "🤝",
            cost = 5,
            maxLevel = 3,
            effectPerLevel = "+1 Companion Slot",
        ),
        PrestigePerk(
            id = "starting_gear",
            name = "Heirloom Equipment",
            description = "Start with better equipment after prestige",
            icon = "⚔️",
            cost = 2,
            maxLevel = 3,
            effectPerLevel = "Better Starting Tier",
        ),
    )

    fun getById(id: String): PrestigePerk? = all.find { it.id == id }
}
