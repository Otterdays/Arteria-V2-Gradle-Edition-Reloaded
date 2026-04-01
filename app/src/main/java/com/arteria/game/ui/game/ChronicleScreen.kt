package com.arteria.game.ui.game

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arteria.game.core.data.AchievementRegistry
import com.arteria.game.core.model.AchievementProgress
import com.arteria.game.core.model.AchievementCategory
import com.arteria.game.ui.theme.ArteriaPalette

@Composable
fun ChronicleScreen(
    achievements: List<AchievementProgress>,
    modifier: Modifier = Modifier,
) {
    val unlockedCount = achievements.count { it.isUnlocked }
    val totalCount = achievements.size

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "CHRONICLE",
                style = MaterialTheme.typography.labelSmall,
                color = ArteriaPalette.Gold,
            )
            Text(
                text = "$unlockedCount / $totalCount",
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaPalette.TextMuted,
            )
        }

        if (achievements.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No achievements yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = ArteriaPalette.TextSecondary,
                    )
                    Text(
                        text = "Start training to unlock milestones",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ArteriaPalette.TextMuted,
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(
                    achievements.sortedWith { a, b ->
                        when {
                            a.isUnlocked && !b.isUnlocked -> -1
                            !a.isUnlocked && b.isUnlocked -> 1
                            else -> a.achievementId.compareTo(b.achievementId)
                        }
                    },
                    key = { it.achievementId },
                ) { progress ->
                    val achievement = AchievementRegistry.getById(progress.achievementId)
                    if (achievement != null) {
                        AchievementCard(
                            achievement = achievement,
                            progress = progress,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AchievementCard(
    achievement: com.arteria.game.core.model.Achievement,
    progress: AchievementProgress,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(10.dp)
    val accent = when (achievement.category) {
        AchievementCategory.MILESTONE -> ArteriaPalette.Gold
        AchievementCategory.GATHERING -> ArteriaPalette.BalancedEnd
        AchievementCategory.CRAFTING -> ArteriaPalette.AccentPrimary
        AchievementCategory.BANKING -> ArteriaPalette.GoldDim
        AchievementCategory.COMBAT -> ArteriaPalette.CombatAccent
        AchievementCategory.EXPLORATION -> ArteriaPalette.LuminarEnd
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = if (progress.isUnlocked) 1.dp else 0.dp,
                color = if (progress.isUnlocked) accent.copy(alpha = 0.4f) else ArteriaPalette.Border,
                shape = cardShape,
            ),
        shape = cardShape,
        color = if (progress.isUnlocked) ArteriaPalette.BgCardHover else ArteriaPalette.BgCard.copy(alpha = 0.6f),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .border(1.dp, accent.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = achievement.icon,
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = achievement.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = if (progress.isUnlocked) FontWeight.Bold else FontWeight.Normal,
                    ),
                    color = if (progress.isUnlocked) ArteriaPalette.TextPrimary else ArteriaPalette.TextMuted,
                )
                Text(
                    text = achievement.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (progress.isUnlocked) ArteriaPalette.TextSecondary else ArteriaPalette.TextMuted,
                )

                if (!progress.isUnlocked && progress.requiredProgress > 0) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "${progress.currentProgress} / ${progress.requiredProgress}",
                        style = MaterialTheme.typography.bodySmall,
                        color = accent,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }

            if (progress.isUnlocked) {
                Text(
                    text = "✓",
                    style = MaterialTheme.typography.titleMedium,
                    color = accent,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
