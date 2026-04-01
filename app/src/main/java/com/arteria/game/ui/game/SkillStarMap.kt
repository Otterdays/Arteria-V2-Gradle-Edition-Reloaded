package com.arteria.game.ui.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arteria.game.core.data.SkillDataRegistry
import com.arteria.game.core.model.SkillState
import com.arteria.game.core.skill.SkillId
import com.arteria.game.core.skill.SkillPillar
import com.arteria.game.core.skill.XPTable
import com.arteria.game.ui.theme.ArteriaPalette
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Constellation / star-map view of all skills.
 * Pillars sit at pentagon vertices; their skills fan outward from each anchor.
 * Crossover edges connect skills whose outputs feed other skills (dashed lines).
 * Tap radius: 28dp around a node centre triggers [onSkillClick].
 *
 * [TRACE: claudes_checklist_by_ryan.md — 3g Star map layout]
 */
@Composable
fun SkillStarMap(
    skills: Map<SkillId, SkillState>,
    onSkillClick: (SkillId) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current

    BoxWithConstraints(modifier = modifier.padding(bottom = 8.dp)) {
        val canvasW = constraints.maxWidth.toFloat()
        val canvasH = constraints.maxHeight.toFloat()

        // Pre-compute positions once per canvas size
        val pillarPositions: Map<SkillPillar, Offset> = remember(canvasW, canvasH) {
            computePillarPositions(canvasW, canvasH)
        }
        val nodePositions: Map<SkillId, Offset> = remember(canvasW, canvasH) {
            computeNodePositions(pillarPositions, canvasW, canvasH)
        }

        val tapRadiusPx = with(density) { 28.dp.toPx() }
        val tapRadiusSq = tapRadiusPx * tapRadiusPx

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(nodePositions) {
                    detectTapGestures { tap ->
                        // Find nearest node within tap radius
                        val hit = nodePositions.entries.minByOrNull { (_, pos) ->
                            val dx = tap.x - pos.x
                            val dy = tap.y - pos.y
                            dx * dx + dy * dy
                        }
                        hit?.let { (skillId, pos) ->
                            val dx = tap.x - pos.x
                            val dy = tap.y - pos.y
                            if (dx * dx + dy * dy <= tapRadiusSq) {
                                onSkillClick(skillId)
                            }
                        }
                    }
                },
        ) {
            val nodeR = min(canvasW, canvasH) * 0.028f
            val dashEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 5f))

            // ── 1. Pillar ambient glows ──────────────────────────────────────
            for ((pillar, anchor) in pillarPositions) {
                val color = pillarColor[pillar] ?: ArteriaPalette.AccentPrimary
                val glowR = min(canvasW, canvasH) * 0.18f
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(color.copy(alpha = 0.07f), Color.Transparent),
                        center = anchor,
                        radius = glowR,
                    ),
                    radius = glowR,
                    center = anchor,
                )
            }

            // ── 2. Crossover dashed edges ────────────────────────────────────
            for ((fromId, targets) in skillCrossover) {
                val fromPos = nodePositions[fromId] ?: continue
                for (toId in targets) {
                    val toPos = nodePositions[toId] ?: continue
                    drawLine(
                        color = Color.White.copy(alpha = 0.12f),
                        start = fromPos,
                        end = toPos,
                        strokeWidth = with(density) { 1.dp.toPx() },
                        cap = StrokeCap.Round,
                        pathEffect = dashEffect,
                    )
                }
            }

            // ── 3. Pillar connector lines (anchor → each node) ───────────────
            for ((pillar, anchor) in pillarPositions) {
                val color = pillarColor[pillar] ?: ArteriaPalette.AccentPrimary
                for (skillId in SkillId.byPillar(pillar)) {
                    val nodePos = nodePositions[skillId] ?: continue
                    drawLine(
                        color = color.copy(alpha = 0.10f),
                        start = anchor,
                        end = nodePos,
                        strokeWidth = with(density) { 0.8.dp.toPx() },
                    )
                }
            }

            // ── 4. Skill nodes ───────────────────────────────────────────────
            for ((skillId, pos) in nodePositions) {
                val state = skills[skillId] ?: SkillState(skillId = skillId)
                val level = XPTable.levelForXp(state.xp)
                val progress = XPTable.progressToNextLevel(state.xp)
                val color = pillarColor[skillId.pillar] ?: ArteriaPalette.AccentPrimary
                val implemented = SkillDataRegistry.isSkillImplemented(skillId)
                val nodeAlpha = if (implemented) 1f else 0.35f
                // Node scales very slightly with level (max ~20% bigger at 99)
                val scaledR = nodeR * (1f + level * 0.002f)

                // Outer glow ring
                drawCircle(
                    color = color.copy(alpha = 0.15f * nodeAlpha),
                    radius = scaledR * 2.2f,
                    center = pos,
                )

                // XP progress arc (track)
                val arcStroke = Stroke(width = with(density) { 1.5.dp.toPx() })
                drawArc(
                    color = color.copy(alpha = 0.2f * nodeAlpha),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = arcStroke,
                    topLeft = Offset(pos.x - scaledR - 3f, pos.y - scaledR - 3f),
                    size = androidx.compose.ui.geometry.Size(
                        (scaledR + 3f) * 2,
                        (scaledR + 3f) * 2,
                    ),
                )
                // XP progress arc (fill)
                if (progress > 0f && implemented) {
                    drawArc(
                        color = color.copy(alpha = 0.7f),
                        startAngle = -90f,
                        sweepAngle = 360f * progress,
                        useCenter = false,
                        style = arcStroke,
                        topLeft = Offset(pos.x - scaledR - 3f, pos.y - scaledR - 3f),
                        size = androidx.compose.ui.geometry.Size(
                            (scaledR + 3f) * 2,
                            (scaledR + 3f) * 2,
                        ),
                    )
                }

                // Filled node circle
                drawCircle(
                    color = color.copy(alpha = (if (implemented) 0.85f else 0.3f)),
                    radius = scaledR,
                    center = pos,
                )

                // Training pulse ring
                if (state.isTraining) {
                    drawCircle(
                        color = color,
                        radius = scaledR + with(density) { 4.dp.toPx() },
                        center = pos,
                        style = Stroke(width = with(density) { 1.5.dp.toPx() }),
                    )
                }
            }

            // ── 5. Skill name labels (native canvas text) ───────────────────
            val paint = android.graphics.Paint().apply {
                isAntiAlias = true
                textAlign = android.graphics.Paint.Align.CENTER
                textSize = with(density) { 7.5.sp.toPx() }
                typeface = android.graphics.Typeface.DEFAULT
            }

            for ((skillId, pos) in nodePositions) {
                val state = skills[skillId] ?: SkillState(skillId = skillId)
                val implemented = SkillDataRegistry.isSkillImplemented(skillId)
                val alpha = if (implemented) 200 else 100
                paint.color = android.graphics.Color.argb(alpha, 232, 233, 237)

                val labelY = pos.y + nodeR * 2.4f + with(density) { 2.dp.toPx() }
                drawContext.canvas.nativeCanvas.drawText(skillId.displayName, pos.x, labelY, paint)

                // Level number inside node for implemented skills
                if (implemented) {
                    val levelPaint = android.graphics.Paint().apply {
                        isAntiAlias = true
                        textAlign = android.graphics.Paint.Align.CENTER
                        textSize = with(density) { 6.sp.toPx() }
                        typeface = android.graphics.Typeface.DEFAULT_BOLD
                        color = android.graphics.Color.argb(220, 232, 233, 237)
                    }
                    val level = XPTable.levelForXp(state.xp)
                    drawContext.canvas.nativeCanvas.drawText(
                        "$level",
                        pos.x,
                        pos.y + with(density) { 2.dp.toPx() },
                        levelPaint,
                    )
                }
            }

            // ── 6. Pillar label anchors ──────────────────────────────────────
            val pillarPaint = android.graphics.Paint().apply {
                isAntiAlias = true
                textAlign = android.graphics.Paint.Align.CENTER
                textSize = with(density) { 8.sp.toPx() }
                typeface = android.graphics.Typeface.DEFAULT_BOLD
            }

            for ((pillar, anchor) in pillarPositions) {
                val color = pillarColor[pillar] ?: ArteriaPalette.AccentPrimary
                pillarPaint.color = android.graphics.Color.argb(
                    160,
                    (color.red * 255).toInt(),
                    (color.green * 255).toInt(),
                    (color.blue * 255).toInt(),
                )
                drawContext.canvas.nativeCanvas.drawText(
                    pillar.displayName.uppercase(),
                    anchor.x,
                    anchor.y + with(density) { 3.dp.toPx() },
                    pillarPaint,
                )
            }
        }
    }
}

// ─── Position helpers ───────────────────────────────────────────────────────

/**
 * Pentagon arrangement — pillar anchors at vertices.
 * Starts from top (GATHERING), clockwise.
 */
private fun computePillarPositions(w: Float, h: Float): Map<SkillPillar, Offset> {
    val cx = w / 2f
    val cy = h * 0.42f
    val r = min(w, h) * 0.30f

    return SkillPillar.entries.mapIndexed { i, pillar ->
        val angleDeg = -90.0 + i * 72.0
        val angleRad = Math.toRadians(angleDeg)
        pillar to Offset(
            (cx + r * cos(angleRad)).toFloat(),
            (cy + r * sin(angleRad)).toFloat(),
        )
    }.toMap()
}

/**
 * Fan each pillar's skills outward from the pillar anchor in an arc,
 * pointing away from the canvas centre so they don't overlap the anchor label.
 */
private fun computeNodePositions(
    pillarPositions: Map<SkillPillar, Offset>,
    w: Float,
    h: Float,
): Map<SkillId, Offset> {
    val cx = w / 2f
    val cy = h * 0.42f
    val spread = Math.PI * 0.85   // total arc angle for a pillar's nodes
    val nodeRadius = min(w, h) * 0.175f
    val result = mutableMapOf<SkillId, Offset>()

    for (pillar in SkillPillar.entries) {
        val anchor = pillarPositions[pillar] ?: continue
        val pillarSkills = SkillId.byPillar(pillar)
        val n = pillarSkills.size
        // Direction from canvas centre → pillar anchor (radians)
        val dirAngle = atan2((anchor.y - cy).toDouble(), (anchor.x - cx).toDouble())

        pillarSkills.forEachIndexed { i, skillId ->
            val t = if (n <= 1) 0.5 else i.toDouble() / (n - 1)
            val angle = dirAngle + (t - 0.5) * spread
            result[skillId] = Offset(
                (anchor.x + nodeRadius * cos(angle)).toFloat(),
                (anchor.y + nodeRadius * sin(angle)).toFloat(),
            )
        }
    }
    return result
}
