package com.arteria.game.ui.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arteria.game.core.data.AchievementRegistry
import com.arteria.game.core.model.Achievement
import com.arteria.game.core.model.AchievementCategory
import com.arteria.game.core.model.AchievementProgress
import com.arteria.game.core.model.AchievementRarity
import com.arteria.game.ui.theme.ArteriaPalette
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ─── Palette helpers ────────────────────────────────────────────────────────

private fun categoryAccent(cat: AchievementCategory): Color = when (cat) {
    AchievementCategory.MILESTONE   -> ArteriaPalette.Gold
    AchievementCategory.GATHERING   -> ArteriaPalette.BalancedEnd
    AchievementCategory.CRAFTING    -> ArteriaPalette.AccentPrimary
    AchievementCategory.BANKING     -> ArteriaPalette.GoldDim
    AchievementCategory.COMBAT      -> ArteriaPalette.CombatAccent
    AchievementCategory.EXPLORATION -> ArteriaPalette.LuminarEnd
}

private fun rarityColor(rarity: AchievementRarity): Color = when (rarity) {
    AchievementRarity.COMMON    -> Color(0xFF9CA3AF)
    AchievementRarity.UNCOMMON  -> ArteriaPalette.BalancedEnd
    AchievementRarity.RARE      -> ArteriaPalette.AccentPrimary
    AchievementRarity.EPIC      -> ArteriaPalette.AccentWeb
    AchievementRarity.LEGENDARY -> ArteriaPalette.Gold
}

private val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())

// ─── Screen ─────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChronicleScreen(
    achievements: List<AchievementProgress>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Category filter state — null = show all
    var selectedCategory by remember { mutableStateOf<AchievementCategory?>(null) }

    val unlockedCount = achievements.count { it.isUnlocked }
    val totalCount = achievements.size
    val overallProgress = if (totalCount > 0) unlockedCount.toFloat() / totalCount else 0f
    val animOverall by animateFloatAsState(overallProgress, tween(800), label = "overall_prog")

    // Build ordered display list: unlocked first (by unlock date desc), then locked by category
    val displayList = remember(achievements, selectedCategory) {
        val base = if (selectedCategory == null) {
            achievements
        } else {
            val cat = selectedCategory
            achievements.filter {
                AchievementRegistry.getById(it.achievementId)?.category == cat
            }
        }
        base.sortedWith(
            compareByDescending<AchievementProgress> { it.isUnlocked }
                .thenByDescending { it.unlockedAt }
                .thenBy { it.achievementId }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "CHRONICLE",
                            style = MaterialTheme.typography.labelSmall,
                            color = ArteriaPalette.Gold,
                        )
                        Text(
                            text = "$unlockedCount / $totalCount unlocked",
                            style = MaterialTheme.typography.bodySmall,
                            color = ArteriaPalette.TextMuted,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = ArteriaPalette.TextSecondary,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            // ── Overall progress strip ────────────────────────────────────────
            item(key = "overall_bar") {
                OverallProgressBar(progress = animOverall, unlocked = unlockedCount, total = totalCount)
            }

            // ── Category filter chips ─────────────────────────────────────────
            item(key = "filter_chips") {
                CategoryFilterRow(selected = selectedCategory, onSelect = { selectedCategory = it })
            }

            // ── Empty state ───────────────────────────────────────────────────
            if (displayList.isEmpty()) {
                item(key = "empty") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🗺️", fontSize = 40.sp)
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = "Nothing here yet",
                                style = MaterialTheme.typography.titleMedium,
                                color = ArteriaPalette.TextSecondary,
                                textAlign = TextAlign.Center,
                            )
                            Text(
                                text = "Start training to unlock milestones",
                                style = MaterialTheme.typography.bodySmall,
                                color = ArteriaPalette.TextMuted,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }

            // ── Achievement cards ─────────────────────────────────────────────
            items(displayList, key = { it.achievementId }) { progress ->
                val achievement = AchievementRegistry.getById(progress.achievementId)
                if (achievement != null) {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(tween(300)) + expandVertically(tween(300)),
                    ) {
                        AchievementCard(achievement = achievement, progress = progress)
                    }
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

// ─── Overall progress bar ────────────────────────────────────────────────────

@Composable
private fun OverallProgressBar(
    progress: Float,
    unlocked: Int,
    total: Int,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = ArteriaPalette.BgCard,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(12.dp, 10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Overall Completion",
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.TextSecondary,
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = ArteriaPalette.Gold,
                )
            }
            Spacer(Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = ArteriaPalette.Gold,
                trackColor = ArteriaPalette.Border.copy(alpha = 0.5f),
            )
        }
    }
}

// ─── Category filter row ─────────────────────────────────────────────────────

@Composable
private fun CategoryFilterRow(
    selected: AchievementCategory?,
    onSelect: (AchievementCategory?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        FilterChip(label = "All", color = ArteriaPalette.TextSecondary, isSelected = selected == null) {
            onSelect(null)
        }
        AchievementCategory.entries.forEach { cat ->
            FilterChip(
                label = cat.displayName,
                color = categoryAccent(cat),
                isSelected = selected == cat,
            ) { onSelect(cat) }
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val bgColor by animateColorAsState(
        if (isSelected) color.copy(alpha = 0.2f) else ArteriaPalette.BgCard,
        label = "chip_bg",
    )
    val borderColor by animateColorAsState(
        if (isSelected) color else ArteriaPalette.Border,
        label = "chip_border",
    )
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal),
            color = if (isSelected) color else ArteriaPalette.TextMuted,
        )
    }
}

// ─── Achievement card ────────────────────────────────────────────────────────

@Composable
private fun AchievementCard(
    achievement: Achievement,
    progress: AchievementProgress,
    modifier: Modifier = Modifier,
) {
    val categoryAccent = categoryAccent(achievement.category)
    val rarityColor = rarityColor(achievement.rarity)
    val cardShape = RoundedCornerShape(12.dp)
    val isUnlocked = progress.isUnlocked

    val cardAlpha by animateFloatAsState(if (isUnlocked) 1f else 0.68f, label = "card_alpha")
    val progressFraction by animateFloatAsState(
        targetValue = if (progress.requiredProgress > 0) {
            (progress.currentProgress.toFloat() / progress.requiredProgress).coerceIn(0f, 1f)
        } else 0f,
        animationSpec = tween(600),
        label = "ach_prog",
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .alpha(cardAlpha)
            .border(
                width = if (isUnlocked) 1.dp else 0.5.dp,
                brush = if (isUnlocked) {
                    Brush.horizontalGradient(listOf(categoryAccent.copy(alpha = 0.5f), Color.Transparent))
                } else {
                    Brush.horizontalGradient(listOf(ArteriaPalette.Border, ArteriaPalette.Border))
                },
                shape = cardShape,
            ),
        shape = cardShape,
        color = if (isUnlocked) ArteriaPalette.BgCardHover else ArteriaPalette.BgCard,
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                // Icon badge
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isUnlocked) categoryAccent.copy(alpha = 0.15f)
                            else ArteriaPalette.Border.copy(alpha = 0.3f)
                        )
                        .border(
                            width = 1.dp,
                            color = if (isUnlocked) categoryAccent.copy(alpha = 0.4f)
                            else ArteriaPalette.Border.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(10.dp),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = if (isUnlocked) achievement.icon else "🔒",
                        fontSize = 20.sp,
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            text = achievement.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = if (isUnlocked) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp,
                            ),
                            color = if (isUnlocked) ArteriaPalette.TextPrimary else ArteriaPalette.TextMuted,
                        )
                        // Rarity dot
                        if (isUnlocked || progress.currentProgress > 0) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(rarityColor, CircleShape),
                            )
                        }
                    }
                    Text(
                        text = achievement.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isUnlocked) ArteriaPalette.TextSecondary else ArteriaPalette.TextMuted,
                    )
                }

                // Unlock check or rarity badge
                if (isUnlocked) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "✓",
                            style = MaterialTheme.typography.titleMedium,
                            color = categoryAccent,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                } else {
                    RarityBadge(rarity = achievement.rarity)
                }
            }

            // Progress section (locked with partial progress, or always show bar if unlocked)
            if (progress.requiredProgress > 0 && (isUnlocked || progress.currentProgress > 0)) {
                Spacer(Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    LinearProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = if (isUnlocked) categoryAccent else categoryAccent.copy(alpha = 0.5f),
                        trackColor = ArteriaPalette.Border.copy(alpha = 0.4f),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (isUnlocked) {
                            "Done"
                        } else {
                            "${progress.currentProgress} / ${progress.requiredProgress}"
                        },
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = if (isUnlocked) categoryAccent else ArteriaPalette.TextMuted,
                    )
                }
            }

            // Unlock date
            if (isUnlocked && progress.unlockedAt > 0L) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Unlocked ${dateFormat.format(Date(progress.unlockedAt))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.TextMuted.copy(alpha = 0.7f),
                )
            }
        }
    }
}

// ─── Rarity badge ────────────────────────────────────────────────────────────

@Composable
private fun RarityBadge(rarity: AchievementRarity, modifier: Modifier = Modifier) {
    val color = rarityColor(rarity)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.12f))
            .border(0.5.dp, color.copy(alpha = 0.35f), RoundedCornerShape(4.dp))
            .padding(horizontal = 5.dp, vertical = 2.dp),
    ) {
        Text(
            text = rarity.label,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp),
            color = color,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
