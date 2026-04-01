package com.arteria.game.ui.game

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arteria.game.core.data.SkillDataRegistry
import com.arteria.game.core.model.SkillState
import com.arteria.game.core.skill.SkillId
import com.arteria.game.core.skill.SkillPillar
import com.arteria.game.core.skill.XPTable
import com.arteria.game.ui.theme.ArteriaPalette
import java.text.NumberFormat

internal val pillarColor = mapOf(
    SkillPillar.GATHERING to ArteriaPalette.BalancedEnd,
    SkillPillar.CRAFTING  to ArteriaPalette.Gold,
    SkillPillar.COMBAT    to Color(0xFFE74C3C),
    SkillPillar.SUPPORT   to ArteriaPalette.LuminarEnd,
)

@Composable
fun SkillsScreen(
    skills: Map<SkillId, SkillState>,
    onSkillClick: (SkillId) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
    ) {
        // Top label
        item(span = { GridItemSpan(2) }) {
            Text(
                text = "SKILLS",
                style = MaterialTheme.typography.labelSmall,
                color = ArteriaPalette.Gold,
                modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 4.dp),
            )
        }

        // Render each pillar section with a header then its skill cards
        SkillPillar.entries.forEach { pillar ->
            val pillarSkills = SkillId.byPillar(pillar)
            val color = pillarColor[pillar] ?: ArteriaPalette.AccentPrimary

            item(span = { GridItemSpan(2) }) {
                PillarSectionHeader(
                    pillar = pillar,
                    color = color,
                    skillCount = pillarSkills.size,
                )
            }

            items(pillarSkills, key = { it.name }) { skillId ->
                val state = skills[skillId] ?: SkillState(skillId = skillId)
                val implemented = remember(skillId) { SkillDataRegistry.isSkillImplemented(skillId) }
                SkillCard(
                    skillId = skillId,
                    state = state,
                    pillarColor = color,
                    implemented = implemented,
                    onClick = { onSkillClick(skillId) },
                )
            }
        }

        // Bottom spacing so last row clears the nav bar
        item(span = { GridItemSpan(2) }) { Spacer(Modifier.height(12.dp)) }
    }
}

// ─── Pillar Section Header ────────────────────────────────────────────────────

@Composable
private fun PillarSectionHeader(
    pillar: SkillPillar,
    color: Color,
    skillCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 2.dp)
            .drawBehind {
                // Left accent stripe
                drawRect(
                    color = color,
                    size = Size(3.dp.toPx(), size.height),
                )
            }
            .padding(start = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = pillar.displayName.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = color,
        )
        Text(
            text = "$skillCount skills",
            style = MaterialTheme.typography.bodySmall,
            color = ArteriaPalette.TextMuted,
        )
    }
}

// ─── Skill Card ───────────────────────────────────────────────────────────────

@Composable
private fun SkillCard(
    skillId: SkillId,
    state: SkillState,
    pillarColor: Color,
    implemented: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val level = XPTable.levelForXp(state.xp)
    val progress = XPTable.progressToNextLevel(state.xp)
    val cardShape = RoundedCornerShape(12.dp)

    val borderColor by animateColorAsState(
        targetValue = when {
            state.isTraining -> pillarColor
            !implemented -> ArteriaPalette.Border.copy(alpha = 0.4f)
            else -> ArteriaPalette.Border
        },
        animationSpec = tween(350),
        label = "card_border_${skillId.name}",
    )
    val cardAlpha = if (!implemented) 0.5f else 1f

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .clickable(onClick = onClick)
            .border(1.dp, borderColor, cardShape),
        shape = cardShape,
        color = when {
            state.isTraining -> ArteriaPalette.BgCardHover
            !implemented -> ArteriaPalette.BgCard.copy(alpha = 0.6f)
            else -> ArteriaPalette.BgCard
        },
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = skillId.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        color = ArteriaPalette.TextPrimary.copy(alpha = cardAlpha),
                        maxLines = 1,
                    )
                    when {
                        state.isTraining -> Text(
                            text = "TRAINING",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = pillarColor,
                        )
                        !implemented -> Text(
                            text = "SOON",
                            style = MaterialTheme.typography.labelSmall,
                            color = ArteriaPalette.TextMuted,
                        )
                        else -> Text(
                            text = skillId.pillar.displayName,
                            style = MaterialTheme.typography.bodySmall,
                            color = pillarColor.copy(alpha = 0.7f),
                        )
                    }
                }

                LevelBadge(
                    level = level,
                    progress = progress,
                    color = pillarColor.copy(alpha = cardAlpha),
                    implemented = implemented,
                )
            }

            Spacer(Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(1.5.dp)),
                color = pillarColor.copy(alpha = cardAlpha),
                trackColor = ArteriaPalette.Border.copy(alpha = 0.4f),
            )

            if (state.xp > 0.0) {
                Spacer(Modifier.height(3.dp))
                Text(
                    text = "${NumberFormat.getIntegerInstance().format(state.xp.toLong())} XP",
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.TextMuted.copy(alpha = cardAlpha),
                )
            }
        }
    }
}

// ─── Level Badge with XP Arc ─────────────────────────────────────────────────

@Composable
private fun LevelBadge(
    level: Int,
    progress: Float,
    color: Color,
    implemented: Boolean,
    modifier: Modifier = Modifier,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = if (implemented) progress.coerceIn(0f, 1f) else 0f,
        animationSpec = tween(600),
        label = "badge_arc",
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(44.dp),
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val stroke = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
            // Track ring
            drawArc(
                color = color.copy(alpha = 0.15f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke,
            )
            // XP fill
            if (animatedProgress > 0f) {
                drawArc(
                    color = color.copy(alpha = 0.75f),
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    style = stroke,
                )
            }
        }
        Text(
            text = if (implemented) "$level" else "—",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
            ),
            color = color,
        )
    }
}
