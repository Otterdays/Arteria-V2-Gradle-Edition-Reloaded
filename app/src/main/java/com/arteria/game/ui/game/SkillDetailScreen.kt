package com.arteria.game.ui.game

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arteria.game.core.data.SkillDataRegistry
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.model.SkillState
import com.arteria.game.core.skill.SkillId
import com.arteria.game.core.skill.SkillPillar
import com.arteria.game.core.skill.XPTable
import com.arteria.game.ui.theme.ArteriaPalette
import com.arteria.game.ui.components.CyberHUD
import java.text.NumberFormat

// Re-use the pillarColor map from SkillsScreen (internal visibility)
private fun pillarColorFor(pillar: SkillPillar): Color =
    pillarColor[pillar] ?: ArteriaPalette.AccentPrimary

@Composable
fun SkillDetailScreen(
    skillId: SkillId,
    skillState: SkillState,
    bank: Map<String, Int>,
    onBack: () -> Unit,
    onStartTraining: (actionId: String) -> Unit,
    onStopTraining: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptics = LocalHapticFeedback.current
    val level = XPTable.levelForXp(skillState.xp)
    val progress = XPTable.progressToNextLevel(skillState.xp)
    val accent = pillarColorFor(skillId.pillar)
    val actions = SkillDataRegistry.actionsForSkill(skillId)
    val nf = NumberFormat.getIntegerInstance()

    val activeAction = skillState.currentActionId
        ?.let { SkillDataRegistry.actionRegistry[it] }
    val trainingProgress = activeAction?.let {
        (skillState.actionProgressMs.toFloat() / it.actionTimeMs).coerceIn(0f, 1f)
    } ?: 0f

    val onStartWithHaptic: (String) -> Unit = { actionId ->
        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        onStartTraining(actionId)
    }

    val onStopWithHaptic: () -> Unit = {
        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
        onStopTraining()
    }

    CyberHUD(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ArteriaPalette.BgApp)
                .navigationBarsPadding(),
        ) {
            // ── Hero header (replaces TopAppBar) ─────────────────────────────────
            SkillHeroHeader(
                skillId = skillId,
                level = level,
                xpProgress = progress,
                accent = accent,
                isTraining = skillState.isTraining,
                activeActionName = activeAction?.name,
                trainingProgress = trainingProgress,
                onBack = onBack,
            )

        // ── Stats strip ───────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "${nf.format(skillState.xp.toLong())} XP",
                style = MaterialTheme.typography.bodyMedium,
                color = ArteriaPalette.TextSecondary,
            )
            if (level < XPTable.MAX_LEVEL) {
                Text(
                    text = "${nf.format(XPTable.xpToNextLevel(skillState.xp))} to next",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ArteriaPalette.TextMuted,
                )
            }
        }

        // ── Stop training button ──────────────────────────────────────────────
        if (skillState.isTraining) {
            OutlinedButton(
                onClick = onStopWithHaptic,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = ArteriaPalette.CombatAccent,
                    containerColor = ArteriaPalette.BgCard,
                ),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = ArteriaPalette.CombatAccent.copy(alpha = 0.7f),
                ),
            ) {
                Text("Stop Training")
            }
            Spacer(Modifier.height(8.dp))
        }

        // ── Action list ───────────────────────────────────────────────────────
        if (actions.isNotEmpty()) {
            Text(
                text = "AVAILABLE ACTIONS",
                style = MaterialTheme.typography.labelSmall,
                color = ArteriaPalette.Gold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(actions, key = { it.id }) { action ->
                    val isCurrent = skillState.isTraining && skillState.currentActionId == action.id
                    ActionCard(
                        action = action,
                        currentLevel = level,
                        isCurrentAction = isCurrent,
                        actionProgress = if (isCurrent) trainingProgress else null,
                        bank = bank,
                        pillarColor = accent,
                        onTrain = { onStartWithHaptic(action.id) },
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Content coming soon…",
                    style = MaterialTheme.typography.bodyLarge,
                    color = ArteriaPalette.TextMuted,
                )
            }
        }
    }
}
}

// ─── Hero Header ─────────────────────────────────────────────────────────────

@Composable
private fun SkillHeroHeader(
    skillId: SkillId,
    level: Int,
    xpProgress: Float,
    accent: Color,
    isTraining: Boolean,
    activeActionName: String?,
    trainingProgress: Float,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    0f to ArteriaPalette.BgDeepSpaceTop,
                    0.55f to ArteriaPalette.BgDeepSpaceTop,
                    1f to accent.copy(alpha = 0.10f),
                ),
            )
            .statusBarsPadding(),
    ) {
        // Back button
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = ArteriaPalette.TextSecondary,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Left: identity + training row
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = skillId.pillar.displayName.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = accent.copy(alpha = 0.7f),
                )
                Text(
                    text = skillId.displayName,
                    style = MaterialTheme.typography.displaySmall,
                    color = ArteriaPalette.TextPrimary,
                )

                Spacer(Modifier.height(6.dp))

                if (isTraining && activeActionName != null) {
                    // Live training row: dot · action name · progress bar
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(accent, CircleShape),
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = activeActionName,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = accent,
                        )
                        Spacer(Modifier.width(10.dp))
                        val animProgress by animateFloatAsState(
                            targetValue = trainingProgress,
                            animationSpec = tween(500),
                            label = "hero_training_progress",
                        )
                        LinearProgressIndicator(
                            progress = { animProgress },
                            modifier = Modifier
                                .weight(1f)
                                .height(3.dp)
                                .clip(RoundedCornerShape(1.5.dp)),
                            color = accent,
                            trackColor = ArteriaPalette.Border.copy(alpha = 0.4f),
                        )
                    }
                } else {
                    Text(
                        text = skillId.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaPalette.TextMuted,
                        maxLines = 2,
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            // Right: circular level display with XP arc
            LevelCircle(
                level = level,
                xpProgress = xpProgress,
                accent = accent,
                isTraining = isTraining
            )
        }
    }
}

// ─── Level Circle ─────────────────────────────────────────────────────────────

@Composable
private fun LevelCircle(
    level: Int,
    xpProgress: Float,
    accent: Color,
    isTraining: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = xpProgress.coerceIn(0f, 1f),
        animationSpec = tween(700),
        label = "level_circle_arc",
    )

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = if (isTraining) 1f else 1f,
        targetValue = if (isTraining) 1.08f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "level_pulse"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(76.dp),
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val stroke = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            // Track
            drawArc(
                color = accent.copy(alpha = 0.15f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke,
            )
            // XP fill
            if (animatedProgress > 0f) {
                drawArc(
                    color = accent,
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    style = stroke,
                )
            }

            // Training Pulse Ring
            if (isTraining) {
                drawCircle(
                    color = accent.copy(alpha = 0.2f),
                    radius = (size.minDimension / 2) * pulseScale,
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$level",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = accent,
            )
            Text(
                text = "LVL",
                style = MaterialTheme.typography.labelSmall,
                color = ArteriaPalette.TextMuted,
            )
        }
    }
}

// ─── Action Card ─────────────────────────────────────────────────────────────

@Composable
private fun ActionCard(
    action: SkillAction,
    currentLevel: Int,
    isCurrentAction: Boolean,
    actionProgress: Float?,
    bank: Map<String, Int>,
    pillarColor: Color,
    onTrain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val meetsLevel = currentLevel >= action.levelRequired
    val canAfford = action.inputItems.all { (itemId, required) ->
        (bank[itemId] ?: 0) >= required
    }
    val cardShape = RoundedCornerShape(10.dp)
    val nf = NumberFormat.getIntegerInstance()

    val borderColor = when {
        isCurrentAction -> pillarColor.copy(alpha = 0.6f)
        !meetsLevel || !canAfford -> ArteriaPalette.Border.copy(alpha = 0.3f)
        else -> ArteriaPalette.Border
    }

    val animProgress by animateFloatAsState(
        targetValue = actionProgress?.coerceIn(0f, 1f) ?: 0f,
        animationSpec = tween(500),
        label = "action_progress_${action.id}",
    )

    // Outer box: clips the sweep fill behind the card content
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .border(1.dp, borderColor, cardShape),
    ) {
        // Live action progress sweep fill
        if (isCurrentAction && animProgress > 0f) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.horizontalGradient(
                            0f to pillarColor.copy(alpha = 0.10f),
                            animProgress to pillarColor.copy(alpha = 0.10f),
                            (animProgress + 0.01f).coerceAtMost(1f) to Color.Transparent,
                            1f to Color.Transparent,
                        ),
                    ),
            )
        }

        // Card content
        Surface(
            color = when {
                isCurrentAction -> ArteriaPalette.BgCardHover.copy(alpha = 0.85f)
                !meetsLevel || !canAfford -> ArteriaPalette.BgCard.copy(alpha = 0.5f)
                else -> ArteriaPalette.BgCard
            },
            shape = cardShape,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = action.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (meetsLevel && canAfford) {
                            ArteriaPalette.TextPrimary
                        } else {
                            ArteriaPalette.TextMuted
                        },
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Lv ${action.levelRequired}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (meetsLevel) ArteriaPalette.TextSecondary else Color(0xFFE74C3C),
                        )
                        Text(
                            text = "${nf.format(action.xpPerAction.toLong())} XP",
                            style = MaterialTheme.typography.bodySmall,
                            color = ArteriaPalette.TextSecondary,
                        )
                        Text(
                            text = "${action.actionTimeMs / 1000.0}s",
                            style = MaterialTheme.typography.bodySmall,
                            color = ArteriaPalette.TextSecondary,
                        )
                    }
                    if (action.inputItems.isNotEmpty()) {
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = action.inputItems.entries
                                .joinToString(" | ") { (id, qty) ->
                                    val owned = bank[id] ?: 0
                                    "$owned/$qty ${SkillDataRegistry.itemName(id)}"
                                },
                            style = MaterialTheme.typography.bodySmall,
                            color = if (canAfford) {
                                ArteriaPalette.Gold.copy(alpha = 0.8f)
                            } else {
                                ArteriaPalette.CombatAccent.copy(alpha = 0.9f)
                            },
                        )
                    }
                }

                if (meetsLevel) {
                    Button(
                        onClick = onTrain,
                        enabled = !isCurrentAction && canAfford,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = pillarColor,
                            contentColor = Color.White,
                            disabledContainerColor = pillarColor.copy(alpha = 0.6f),
                            disabledContentColor = Color.White,
                        ),
                    ) {
                        Text(
                            when {
                                isCurrentAction -> "Active"
                                !canAfford -> "No stock"
                                else -> "Train"
                            },
                        )
                    }
                } else {
                    Text(
                        text = "Locked",
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaPalette.TextMuted,
                        modifier = Modifier.padding(horizontal = 8.dp),
                    )
                }
            }
        }
    }
}
