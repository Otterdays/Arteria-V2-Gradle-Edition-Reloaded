package com.arteria.game.ui.theme

import androidx.compose.ui.graphics.Color
import com.arteria.game.core.model.AchievementCategory
import com.arteria.game.core.model.AchievementRarity

/** Shared Chronicle / toast styling — palette tokens only (no raw hex). */
object AchievementDecor {
    fun rarityAccent(rarity: AchievementRarity): Color =
        when (rarity) {
            AchievementRarity.COMMON -> ArteriaPalette.TextMuted
            AchievementRarity.UNCOMMON -> ArteriaPalette.BalancedEnd
            AchievementRarity.RARE -> ArteriaPalette.AccentPrimary
            AchievementRarity.EPIC -> ArteriaPalette.AccentWeb
            AchievementRarity.LEGENDARY -> ArteriaPalette.Gold
        }

    fun categoryAccent(cat: AchievementCategory): Color =
        when (cat) {
            AchievementCategory.MILESTONE -> ArteriaPalette.Gold
            AchievementCategory.GATHERING -> ArteriaPalette.BalancedEnd
            AchievementCategory.CRAFTING -> ArteriaPalette.AccentPrimary
            AchievementCategory.BANKING -> ArteriaPalette.GoldDim
            AchievementCategory.COMBAT -> ArteriaPalette.CombatAccent
            AchievementCategory.EXPLORATION -> ArteriaPalette.LuminarEnd
            AchievementCategory.RESONANCE -> ArteriaPalette.VoidAccent
        }

    /** Unlock toast visibility — scales with rarity so legendaries linger slightly longer. */
    fun rarityToastMillis(rarity: AchievementRarity): Long =
        when (rarity) {
            AchievementRarity.COMMON -> 3_400L
            AchievementRarity.UNCOMMON -> 3_800L
            AchievementRarity.RARE -> 4_200L
            AchievementRarity.EPIC -> 4_800L
            AchievementRarity.LEGENDARY -> 5_400L
        }
}
