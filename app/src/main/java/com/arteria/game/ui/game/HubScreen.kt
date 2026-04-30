package com.arteria.game.ui.game

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.arteria.game.core.data.SkillDataRegistry
import com.arteria.game.core.model.GameState
import com.arteria.game.core.model.SkillState
import com.arteria.game.core.model.TickResult
import com.arteria.game.core.skill.SkillId
import com.arteria.game.core.skill.SkillPillar
import com.arteria.game.core.skill.XPTable
import com.arteria.game.ui.theme.ArteriaPalette
import kotlin.math.roundToInt
import com.arteria.game.ui.theme.LocalReduceMotion
import com.arteria.game.ui.components.CyberHUD
import java.text.NumberFormat

/**
 * Hub / Command Center — the landing tab. Shows at-a-glance game state:
 * welcome-back card (offline gains), active training panel, quick stats,
 * next milestone nudge, smart suggestions, and recent level-ups.
 *
 * [TRACE: claudes_checklist_by_ryan.md — Section 1: Hub Screen]
 */
@Composable
fun HubScreen(
    gameState: GameState,
    offlineReport: TickResult?,
    recentLevelUps: List<Pair<SkillId, Int>>,
    onDismissOffline: () -> Unit,
    onSkillTap: (SkillId) -> Unit,
    onNavigateToSkills: () -> Unit,
    onNavigateToBank: () -> Unit,
    onNavigateToResonance: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val nf = remember { NumberFormat.getIntegerInstance() }
    val skills = gameState.skills
    val bank = gameState.bank

    // 5f. Dismissed nudge keys — resets automatically when key changes (level-up / bank change)
    var dismissedNudges by remember { mutableStateOf(setOf<String>()) }

    // Derived data
    val totalLevel = remember(skills) {
        skills.values.sumOf { XPTable.levelForXp(it.xp) }
    }
    val highestSkill = remember(skills) {
        skills.maxByOrNull { it.value.xp }
    }
    val trainingSKill = remember(skills) {
        skills.entries.firstOrNull { it.value.isTraining }
    }
    val milestone = remember(skills) { findNextMilestone(skills) }
    val suggestion = remember(skills, bank) { findSmartSuggestion(skills, bank) }

    // Stable keys — when game state progresses the key changes, auto-refreshing dismissed nudges
    val milestoneKey = milestone?.let { "ms_${it.skillId.name}_${it.targetLevel}" }
    val suggestionKey = suggestion?.let { sug ->
        when (sug) {
            is Suggestion.CraftOpportunity -> "sug_craft_${sug.itemName}"
            is Suggestion.IdleSkill        -> "sug_idle_${sug.skillId.name}"
        }
    }

    CyberHUD(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
        item { Spacer(Modifier.height(4.dp)) }

        // ── 1b. Welcome-back card (inline offline gains) ─────────────────────
        if (offlineReport != null &&
            (offlineReport.xpGained.isNotEmpty() ||
                offlineReport.resourcesGained.isNotEmpty())
        ) {
            item(key = "offline") {
                OfflineGainsCard(
                    report = offlineReport,
                    nf = nf,
                    onDismiss = onDismissOffline,
                )
            }
        }

        // ── 1c. Active training panel ────────────────────────────────────────
        val training = trainingSKill
        if (training != null) {
            item(key = "training") {
                ActiveTrainingCard(
                    skillId = training.key,
                    state = training.value,
                    nf = nf,
                    onTap = { onSkillTap(training.key) },
                )
            }
        }

        // ── 1d. Quick-stats row ──────────────────────────────────────────────
        item(key = "stats") {
            QuickStatsRow(
                totalLevel = totalLevel,
                totalItems = bank.values.sum(),
                highestSkillId = highestSkill?.key,
                highestLevel = highestSkill?.let {
                    XPTable.levelForXp(it.value.xp)
                },
                implementedCount = skills.keys.count {
                    SkillDataRegistry.isSkillImplemented(it)
                },
            )
        }

        item(key = "resonance") {
            ResonanceHubCard(
                momentum = gameState.momentum,
                onOpen = onNavigateToResonance,
            )
        }

        // ── 1e. Next milestone nudge (5f: dismissible, keyed to level target) ──
        val ms = milestone
        val msKey = milestoneKey
        if (ms != null && msKey != null && msKey !in dismissedNudges) {
            item(key = "milestone") {
                MilestoneCard(
                    milestone = ms,
                    nf = nf,
                    onTap = { onSkillTap(ms.skillId) },
                    onDismiss = { dismissedNudges = dismissedNudges + msKey },
                )
            }
        }

        // ── 1f. Smart suggestion (5f: max 2 nudges total, dismissible) ───────
        val sug = suggestion
        val sugKey = suggestionKey
        val nudgesShown = if (ms != null && msKey != null && msKey !in dismissedNudges) 1 else 0
        if (sug != null && sugKey != null && sugKey !in dismissedNudges && nudgesShown < 2) {
            item(key = "suggestion") {
                SuggestionCard(
                    suggestion = sug,
                    onTap = {
                        when (sug) {
                            is Suggestion.CraftOpportunity -> onNavigateToBank()
                            is Suggestion.IdleSkill        -> onSkillTap(sug.skillId)
                        }
                    },
                    onDismiss = { dismissedNudges = dismissedNudges + sugKey },
                )
            }
        }

        // ── 1g. Recent level-ups ticker ──────────────────────────────────────
        if (recentLevelUps.isNotEmpty()) {
            item(key = "levelups_header") {
                Text(
                    text = "RECENT LEVEL-UPS",
                    style = MaterialTheme.typography.labelSmall,
                    color = ArteriaPalette.Gold,
                    modifier = Modifier.padding(start = 4.dp, top = 6.dp),
                )
            }
            items(
                recentLevelUps.takeLast(5).reversed(),
                key = { "lu_${it.first.name}_${it.second}" },
            ) { (skillId, level) ->
                LevelUpMiniCard(skillId = skillId, level = level)
            }
        }

        // ── No training, no offline — show a gentle prompt ───────────────────
        if (training == null && offlineReport == null && recentLevelUps.isEmpty()) {
            item(key = "empty") {
                EmptyHubPrompt(onNavigateToSkills = onNavigateToSkills)
            }
        }

        item { Spacer(Modifier.height(8.dp)) }
    }
}
}

// ─── 1b. Offline Gains Card ─────────────────────────────────────────────────

@Composable
private fun OfflineGainsCard(
    report: TickResult,
    nf: NumberFormat,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HubCard(
        accent = ArteriaPalette.AccentPrimary,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "While You Were Away",
                style = MaterialTheme.typography.titleMedium,
                color = ArteriaPalette.Gold,
            )
            TextButton(onClick = onDismiss) {
                Text("Dismiss", color = ArteriaPalette.TextMuted)
            }
        }

        if (report.xpGained.isNotEmpty()) {
            for ((skillId, xp) in report.xpGained) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = skillId.displayName,
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaPalette.TextPrimary,
                    )
                    Text(
                        text = "+${nf.format(xp.toLong())} XP",
                        style = MaterialTheme.typography.bodySmall
                            .copy(fontWeight = FontWeight.Bold),
                        color = ArteriaPalette.BalancedEnd,
                    )
                }
            }
        }

        if (report.resourcesGained.isNotEmpty()) {
            Spacer(Modifier.height(4.dp))
            for ((itemId, qty) in report.resourcesGained) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = SkillDataRegistry.itemName(itemId),
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaPalette.TextPrimary,
                    )
                    Text(
                        text = "+${nf.format(qty)}",
                        style = MaterialTheme.typography.bodySmall
                            .copy(fontWeight = FontWeight.Bold),
                        color = ArteriaPalette.Gold,
                    )
                }
            }
        }

        if (report.levelUps.isNotEmpty()) {
            Spacer(Modifier.height(4.dp))
            for (lu in report.levelUps) {
                Text(
                    text = "${lu.skillId.displayName}: ${lu.oldLevel} → ${lu.newLevel}",
                    style = MaterialTheme.typography.bodySmall
                        .copy(fontWeight = FontWeight.Bold),
                    color = ArteriaPalette.AccentWeb,
                )
            }
        }
    }
}

// ─── 1c. Active Training Card ───────────────────────────────────────────────

@Composable
private fun ActiveTrainingCard(
    skillId: SkillId,
    state: SkillState,
    nf: NumberFormat,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val level = XPTable.levelForXp(state.xp)
    val progress = XPTable.progressToNextLevel(state.xp)
    val xpRemaining = XPTable.xpToNextLevel(state.xp)
    val accent = pillarColor[skillId.pillar] ?: ArteriaPalette.AccentPrimary
    val actionName = state.currentActionId
        ?.let { SkillDataRegistry.actionRegistry[it]?.name }
        ?: "Training"

    // Action cycle progress
    val action = state.currentActionId
        ?.let { SkillDataRegistry.actionRegistry[it] }
    val cycleProgress = action?.let {
        (state.actionProgressMs.toFloat() / it.actionTimeMs).coerceIn(0f, 1f)
    } ?: 0f

    // Estimate time to next level
    val xpPerAction = action?.xpPerAction ?: 0.0
    val actionTimeS = (action?.actionTimeMs ?: 1L) / 1000.0
    val xpPerSecond = if (actionTimeS > 0) xpPerAction / actionTimeS else 0.0
    val secondsToLevel = if (xpPerSecond > 0) (xpRemaining / xpPerSecond).toLong() else 0L

    val animCycle by animateFloatAsState(
        targetValue = cycleProgress,
        animationSpec = tween(400),
        label = "hub_cycle",
    )

    // Pulsing dot
    val inf = rememberInfiniteTransition(label = "training_pulse")
    val pulseAlpha by inf.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(800, easing = LinearEasing),
            RepeatMode.Reverse,
        ),
        label = "pulse_dot",
    )

    HubCard(
        accent = accent,
        modifier = modifier.clickable(onClick = onTap),
        isHolo = true, // Extra holo juice for active training
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            // Level ring
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(52.dp),
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    val stroke = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    drawArc(
                        color = accent.copy(alpha = 0.15f),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = stroke,
                    )
                    if (progress > 0f) {
                        drawArc(
                            color = accent,
                            startAngle = -90f,
                            sweepAngle = 360f * progress,
                            useCenter = false,
                            style = stroke,
                        )
                    }
                }
                Text(
                    text = "$level",
                    style = MaterialTheme.typography.titleMedium
                        .copy(fontWeight = FontWeight.Bold),
                    color = accent,
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = skillId.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    color = ArteriaPalette.TextPrimary,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(
                                accent.copy(alpha = pulseAlpha),
                                CircleShape,
                            ),
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = actionName,
                        style = MaterialTheme.typography.bodySmall,
                        color = accent,
                    )
                }
                Spacer(Modifier.height(4.dp))

                // Action cycle bar
                LinearProgressIndicator(
                    progress = { animCycle },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(1.5.dp)),
                    color = accent,
                    trackColor = ArteriaPalette.Border.copy(alpha = 0.4f),
                )

                Spacer(Modifier.height(3.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "${nf.format(xpRemaining)} XP to ${level + 1}",
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaPalette.TextMuted,
                    )
                    if (secondsToLevel > 0) {
                        Text(
                            text = formatDuration(secondsToLevel),
                            style = MaterialTheme.typography.bodySmall,
                            color = ArteriaPalette.TextMuted,
                        )
                    }
                }
            }
        }
    }
}

// ─── Resonance shortcut (CLICKER_DESIGN §2.2) ────────────────────────────────

@Composable
private fun ResonanceHubCard(
    momentum: Double,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HubCard(
        accent = ArteriaPalette.VoidAccent,
        modifier = modifier.clickable(onClick = onOpen),
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(
                text = "RESONANCE",
                style = MaterialTheme.typography.labelSmall,
                color = ArteriaPalette.Gold,
            )
            Text(
                text = "Pulse the orb — haste every other skill",
                style = MaterialTheme.typography.bodyMedium,
                color = ArteriaPalette.TextPrimary,
            )
            Text(
                text = "Momentum ${momentum.roundToInt()}%",
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaPalette.TextMuted,
            )
        }
    }
}

// ─── 1d. Quick Stats Row ────────────────────────────────────────────────────

@Composable
private fun QuickStatsRow(
    totalLevel: Int,
    totalItems: Int,
    highestSkillId: SkillId?,
    highestLevel: Int?,
    implementedCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        StatChip(
            label = "Total Level",
            value = "$totalLevel",
            color = ArteriaPalette.AccentPrimary,
            modifier = Modifier.weight(1f),
        )
        StatChip(
            label = "Bank Items",
            value = "$totalItems",
            color = ArteriaPalette.Gold,
            modifier = Modifier.weight(1f),
        )
        StatChip(
            label = "Top Skill",
            value = if (highestSkillId != null && highestLevel != null) {
                "${highestSkillId.displayName} $highestLevel"
            } else {
                "—"
            },
            color = ArteriaPalette.BalancedEnd,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun StatChip(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = ArteriaPalette.BgCard,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Tiny Canvas icon above the value
            Canvas(modifier = Modifier.size(14.dp).padding(bottom = 2.dp)) {
                val w = size.width
                val h = size.height
                val stroke = Stroke(width = w * 0.12f, cap = StrokeCap.Round)
                when (label) {
                    "Total Level" -> {
                        // Stacked bars icon
                        drawLine(color, Offset(w * 0.15f, h * 0.85f), Offset(w * 0.35f, h * 0.85f), w * 0.12f, cap = StrokeCap.Round)
                        drawLine(color, Offset(w * 0.15f, h * 0.55f), Offset(w * 0.55f, h * 0.55f), w * 0.12f, cap = StrokeCap.Round)
                        drawLine(color, Offset(w * 0.15f, h * 0.25f), Offset(w * 0.85f, h * 0.25f), w * 0.12f, cap = StrokeCap.Round)
                    }
                    "Bank Items" -> {
                        // Small vault circle
                        drawCircle(color, radius = w * 0.35f, center = Offset(w * 0.5f, h * 0.5f), style = stroke)
                        drawCircle(color, radius = w * 0.10f, center = Offset(w * 0.5f, h * 0.5f))
                    }
                    else -> {
                        // Star icon
                        val path = Path()
                        val cx = w * 0.5f; val cy = h * 0.5f
                        for (i in 0 until 5) {
                            val outerAngle = Math.toRadians(-90.0 + i * 72.0)
                            val innerAngle = Math.toRadians(-90.0 + i * 72.0 + 36.0)
                            val outerR = w * 0.45f; val innerR = w * 0.18f
                            if (i == 0) path.moveTo(cx + (outerR * kotlin.math.cos(outerAngle)).toFloat(), cy + (outerR * kotlin.math.sin(outerAngle)).toFloat())
                            else path.lineTo(cx + (outerR * kotlin.math.cos(outerAngle)).toFloat(), cy + (outerR * kotlin.math.sin(outerAngle)).toFloat())
                            path.lineTo(cx + (innerR * kotlin.math.cos(innerAngle)).toFloat(), cy + (innerR * kotlin.math.sin(innerAngle)).toFloat())
                        }
                        path.close()
                        drawPath(path, color)
                    }
                }
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium
                    .copy(fontWeight = FontWeight.Bold),
                color = color,
                maxLines = 1,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaPalette.TextMuted,
                maxLines = 1,
            )
        }
    }
}

// ─── 1e. Milestone Card ─────────────────────────────────────────────────────

@Composable
private fun MilestoneCard(
    milestone: Milestone,
    nf: NumberFormat,
    onTap: () -> Unit,
    onDismiss: () -> Unit,   // 5f
    modifier: Modifier = Modifier,
) {
    val accent = pillarColor[milestone.skillId.pillar] ?: ArteriaPalette.AccentPrimary
    val animProg by animateFloatAsState(
        targetValue = milestone.progress,
        animationSpec = tween(600),
        label = "milestone_prog",
    )

    HubCard(
        accent = ArteriaPalette.AccentWeb,
        modifier = modifier.clickable(onClick = onTap),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "NEXT MILESTONE",
                style = MaterialTheme.typography.labelSmall,
                color = ArteriaPalette.AccentWeb.copy(alpha = 0.7f),
            )
            TextButton(onClick = onDismiss) {
                Text("×", color = ArteriaPalette.TextMuted)
            }
        }
        Text(
            text = milestone.message,
            style = MaterialTheme.typography.bodyMedium,
            color = ArteriaPalette.TextPrimary,
        )
        Spacer(Modifier.height(6.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            LinearProgressIndicator(
                progress = { animProg },
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = accent,
                trackColor = ArteriaPalette.Border.copy(alpha = 0.4f),
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "${(milestone.progress * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall
                    .copy(fontWeight = FontWeight.Bold),
                color = accent,
            )
        }
    }
}

// ─── 1f. Suggestion Card ────────────────────────────────────────────────────

@Composable
private fun SuggestionCard(
    suggestion: Suggestion,
    onTap: () -> Unit,
    onDismiss: () -> Unit,   // 5f
    modifier: Modifier = Modifier,
) {
    HubCard(
        accent = ArteriaPalette.Gold.copy(alpha = 0.6f),
        modifier = modifier.clickable(onClick = onTap),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "SUGGESTION",
                style = MaterialTheme.typography.labelSmall,
                color = ArteriaPalette.Gold.copy(alpha = 0.7f),
            )
            TextButton(onClick = onDismiss) {
                Text("×", color = ArteriaPalette.TextMuted)
            }
        }
        Text(
            text = suggestion.message,
            style = MaterialTheme.typography.bodyMedium,
            color = ArteriaPalette.TextPrimary,
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = suggestion.cta,
            style = MaterialTheme.typography.bodySmall
                .copy(fontWeight = FontWeight.Medium),
            color = ArteriaPalette.Gold,
        )
    }
}

// ─── 1g. Level-Up Mini Card ─────────────────────────────────────────────────

@Composable
private fun LevelUpMiniCard(
    skillId: SkillId,
    level: Int,
    modifier: Modifier = Modifier,
) {
    val accent = pillarColor[skillId.pillar] ?: ArteriaPalette.AccentPrimary

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = ArteriaPalette.BgCard,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(accent, CircleShape),
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "${skillId.displayName} reached level $level",
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaPalette.TextPrimary,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "Lv $level",
                style = MaterialTheme.typography.bodySmall
                    .copy(fontWeight = FontWeight.Bold),
                color = accent,
            )
        }
    }
}

// ─── Empty hub prompt ───────────────────────────────────────────────────────

@Composable
private fun EmptyHubPrompt(
    onNavigateToSkills: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HubCard(
        accent = ArteriaPalette.AccentPrimary.copy(alpha = 0.4f),
        modifier = modifier.clickable(onClick = onNavigateToSkills),
    ) {
        Text(
            text = "Ready to begin?",
            style = MaterialTheme.typography.titleMedium,
            color = ArteriaPalette.TextPrimary,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Head to Skills and start training to see your " +
                "progress here.",
            style = MaterialTheme.typography.bodySmall,
            color = ArteriaPalette.TextMuted,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Go to Skills →",
            style = MaterialTheme.typography.bodySmall
                .copy(fontWeight = FontWeight.Medium),
            color = ArteriaPalette.AccentPrimary,
        )
    }
}

// ─── Shared card wrapper ────────────────────────────────────────────────────

@Composable
private fun HubCard(
    accent: Color,
    modifier: Modifier = Modifier,
    isHolo: Boolean = false,
    content: @Composable () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = ArteriaPalette.BgCard.copy(alpha = if (isHolo) 0.8f else 1f),
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                // Gradient accent stripe
                val stripeW = 3.dp.toPx()
                drawRect(
                    brush = Brush.verticalGradient(
                        0f to accent,
                        0.7f to accent.copy(alpha = 0.5f),
                        1f to Color.Transparent,
                    ),
                    topLeft = Offset.Zero,
                    size = androidx.compose.ui.geometry.Size(stripeW, size.height),
                )

                // Subtitle/Detail subtle grid background for Holo cards
                if (isHolo) {
                    val gSize = 20.dp.toPx()
                    var gx = 0f
                    while (gx < size.width) {
                        drawLine(ArteriaPalette.GridLine, Offset(gx, 0f), Offset(gx, size.height), 0.5f)
                        gx += gSize
                    }
                    var gy = 0f
                    while (gy < size.height) {
                        drawLine(ArteriaPalette.GridLine, Offset(0f, gy), Offset(size.width, gy), 0.5f)
                        gy += gSize
                    }
                }
            },
    ) {
        Box {
            // Glassmorphism top highlight
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            0f to Color.Transparent,
                            0.2f to ArteriaPalette.GlassHighlight,
                            0.8f to ArteriaPalette.GlassHighlight,
                            1f to Color.Transparent,
                        ),
                    ),
            )
            Column(modifier = Modifier.padding(start = 14.dp, end = 12.dp, top = 10.dp, bottom = 10.dp)) {
                content()
            }
        }
    }
}

// ─── Data models ────────────────────────────────────────────────────────────

internal data class Milestone(
    val skillId: SkillId,
    val currentLevel: Int,
    val targetLevel: Int,
    val progress: Float,
    val message: String,
)

internal sealed class Suggestion(
    val message: String,
    val cta: String,
) {
    class CraftOpportunity(
        val itemName: String,
        message: String,
        cta: String,
    ) : Suggestion(message, cta)

    class IdleSkill(
        val skillId: SkillId,
        message: String,
        cta: String,
    ) : Suggestion(message, cta)
}

// ─── Logic helpers ──────────────────────────────────────────────────────────

/**
 * Finds the skill closest to its next level (within top 80%+
 * progress, or just the closest one overall).
 */
private fun findNextMilestone(
    skills: Map<SkillId, SkillState>,
): Milestone? {
    // Only look at skills that have any XP
    val candidates = skills.entries
        .filter { it.value.xp > 0 && XPTable.levelForXp(it.value.xp) < XPTable.MAX_LEVEL }
        .map { (id, state) ->
            val level = XPTable.levelForXp(state.xp)
            val progress = XPTable.progressToNextLevel(state.xp)
            Triple(id, level, progress)
        }
        .sortedByDescending { it.third }

    val best = candidates.firstOrNull() ?: return null
    val (skillId, level, progress) = best
    val targetLevel = level + 1

    // Check if this level unlocks a new action
    val unlockText = SkillDataRegistry.actionsForSkill(skillId)
        .firstOrNull { it.levelRequired == targetLevel }
        ?.let { " — unlocks ${it.name}" }
        ?: ""

    return Milestone(
        skillId = skillId,
        currentLevel = level,
        targetLevel = targetLevel,
        progress = progress,
        message = "${skillId.displayName} $level/$targetLevel$unlockText",
    )
}

/**
 * Scans bank for craftable recipes or finds idle implemented skills.
 */
private fun findSmartSuggestion(
    skills: Map<SkillId, SkillState>,
    bank: Map<String, Int>,
): Suggestion? {
    // Priority 1: craftable opportunity in bank
    val craftable = SkillDataRegistry.actionRegistry.values
        .filter { action ->
            action.inputItems.isNotEmpty() &&
                action.inputItems.all { (id, qty) -> (bank[id] ?: 0) >= qty }
        }
        .maxByOrNull { action ->
            // Prefer the highest-value craftable action
            action.xpPerAction
        }

    if (craftable != null) {
        val outputName = craftable.resourceId
            ?.let { SkillDataRegistry.itemName(it) }
            ?: craftable.name
        val inputSummary = craftable.inputItems.entries
            .joinToString(", ") { (id, qty) ->
                "${bank[id] ?: 0} ${SkillDataRegistry.itemName(id)}"
            }
        return Suggestion.CraftOpportunity(
            itemName = outputName,
            message = "You have $inputSummary sitting in your bank.",
            cta = "Smelt or cook it →",
        )
    }

    // Priority 2: idle implemented skill with XP that isn't training
    val idle = skills.entries
        .filter { (id, state) ->
            !state.isTraining &&
                state.xp > 0 &&
                SkillDataRegistry.isSkillImplemented(id)
        }
        .minByOrNull { it.value.xp }

    if (idle != null) {
        return Suggestion.IdleSkill(
            skillId = idle.key,
            message = "${idle.key.displayName} is idle at level " +
                "${XPTable.levelForXp(idle.value.xp)}.",
            cta = "Resume training →",
        )
    }

    return null
}

private fun formatDuration(totalSeconds: Long): String {
    val h = totalSeconds / 3600
    val m = (totalSeconds % 3600) / 60
    val s = totalSeconds % 60
    return when {
        h > 0 -> "${h}h ${m}m"
        m > 0 -> "${m}m ${s}s"
        else -> "${s}s"
    }
}
