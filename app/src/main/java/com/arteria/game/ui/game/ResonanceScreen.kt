package com.arteria.game.ui.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arteria.game.core.data.ResonanceData
import com.arteria.game.core.model.GameState
import com.arteria.game.core.model.SkillState
import com.arteria.game.core.skill.SkillId
import com.arteria.game.core.skill.XPTable
import com.arteria.game.ui.theme.ArteriaPalette
import com.arteria.game.ui.theme.LocalReduceMotion
import kotlin.math.roundToInt

// [TRACE: DOCS/ARTERIA-V1-DOCS/DOCU/CLICKER_DESIGN.md — Resonance tab UX]

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResonanceScreen(
    gameState: GameState,
    onPulse: () -> Unit,
    onHeavyPulse: () -> Unit,
    hapticsEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val reduceMotion = LocalReduceMotion.current
    val haptics = LocalHapticFeedback.current
    val resState = gameState.skills[SkillId.RESONANCE] ?: SkillState(skillId = SkillId.RESONANCE)
    val level = XPTable.levelForXp(resState.xp)
    val xpBar = XPTable.progressToNextLevel(resState.xp)
    val mom = gameState.momentum.toFloat().coerceIn(0f, 100f) / 100f
    val haste = ResonanceData.hasteMultiplier(gameState.momentum)
    val hastePct = ((haste - 1.0) * 100).roundToInt()
    val accent = ArteriaPalette.VoidAccent
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val orbScale by animateFloatAsState(
        targetValue = if (reduceMotion) 1f else if (pressed) 0.92f else 1f,
        animationSpec = tween(180),
        label = "orb_scale",
    )

    fun feedbackHeavy() {
        if (hapticsEnabled) {
            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    fun feedbackTap() {
        if (hapticsEnabled) {
            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = SkillId.RESONANCE.displayName.uppercase(),
            style = MaterialTheme.typography.headlineMedium,
            color = accent,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = "The Pulse — steady the flow of time.",
            style = MaterialTheme.typography.bodySmall,
            color = ArteriaPalette.TextMuted,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .scale(orbScale)
                    .clip(CircleShape)
                    .background(ArteriaPalette.BgApp)
                    .border(3.dp, accent, CircleShape)
                    .combinedClickable(
                        interactionSource = interaction,
                        indication = null,
                        onClick = {
                            feedbackTap()
                            onPulse()
                        },
                        onLongClick = {
                            feedbackHeavy()
                            onHeavyPulse()
                        },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Pulse",
                    style = MaterialTheme.typography.titleLarge,
                    color = accent,
                )
            }
        }

        Text(
            text = "Tap to build Momentum · Long-press Heavy Pulse (Lv 60+, 5 Anchor)",
            style = MaterialTheme.typography.bodySmall,
            color = ArteriaPalette.TextSecondary,
        )

        Text("Momentum", style = MaterialTheme.typography.labelSmall, color = ArteriaPalette.TextMuted)
        LinearProgressIndicator(
            progress = { mom },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(CircleShape),
            color = accent,
            trackColor = ArteriaPalette.BgInput,
        )
        Text(
            text = "${gameState.momentum.roundToInt()}% · other skills ~$hastePct% faster",
            style = MaterialTheme.typography.bodySmall,
            color = ArteriaPalette.TextSecondary,
        )

        Text("Anchor energy", style = MaterialTheme.typography.labelSmall, color = ArteriaPalette.TextMuted)
        Text(
            text = "${gameState.anchorEnergy} / ${ResonanceData.ANCHOR_ENERGY_CAP} " +
                "(+1 / min while training other skills)",
            style = MaterialTheme.typography.bodySmall,
            color = ArteriaPalette.TextSecondary,
        )

        Text("Level $level", style = MaterialTheme.typography.titleMedium, color = ArteriaPalette.Gold)
        if (level < XPTable.MAX_LEVEL) {
            LinearProgressIndicator(
                progress = { xpBar },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = accent,
                trackColor = ArteriaPalette.BgInput,
            )
        } else {
            Text("Max level", style = MaterialTheme.typography.bodySmall, color = ArteriaPalette.TextMuted)
        }

        Text("Unlocks", style = MaterialTheme.typography.titleSmall, color = ArteriaPalette.TextPrimary)
        ResonanceData.unlockRows.forEach { row ->
            val unlocked = level >= row.level
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = ArteriaPalette.BgCard,
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(Modifier.padding(10.dp)) {
                    Text(
                        text = "${if (unlocked) "✓" else "○"} Lv${row.level} · ${row.label}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (unlocked) ArteriaPalette.Gold else ArteriaPalette.TextMuted,
                    )
                    Text(
                        text = row.effect,
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaPalette.TextSecondary,
                    )
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}
