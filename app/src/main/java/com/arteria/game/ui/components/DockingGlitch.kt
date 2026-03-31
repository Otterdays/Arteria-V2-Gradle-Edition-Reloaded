package com.arteria.game.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin
import kotlin.random.Random

// ═══════════════════════════════════════════════════════════════════════
//  Precomputed glitch artifact layout — deterministic per card seed
// ═══════════════════════════════════════════════════════════════════════

data class RgbBandSpec(val yFrac: Float, val heightPx: Float, val shiftPx: Float)

data class NoiseBlockSpec(
    val xFrac: Float,
    val yFrac: Float,
    val widthFrac: Float,
    val heightPx: Float,
    val shiftPx: Float,
)

data class GrainDot(val xFrac: Float, val yFrac: Float, val radius: Float, val alpha: Float)

data class GlitchLayout(
    val rgbBands: List<RgbBandSpec>,
    val noiseBlocks: List<NoiseBlockSpec>,
    val grainDots: List<GrainDot>,
    val scanLines: List<Float>,
)

@Composable
fun rememberGlitchLayout(seed: Int): GlitchLayout {
    return remember(seed) {
        val rnd = Random(seed)
        GlitchLayout(
            rgbBands = List(8) {
                RgbBandSpec(
                    yFrac = rnd.nextFloat(),
                    heightPx = rnd.nextFloat() * 4f + 1f,
                    shiftPx = (rnd.nextFloat() - 0.5f) * 14f,
                )
            },
            noiseBlocks = List(5) {
                NoiseBlockSpec(
                    xFrac = rnd.nextFloat() * 0.6f,
                    yFrac = rnd.nextFloat(),
                    widthFrac = rnd.nextFloat() * 0.35f + 0.1f,
                    heightPx = rnd.nextFloat() * 6f + 2f,
                    shiftPx = (rnd.nextFloat() - 0.5f) * 22f,
                )
            },
            grainDots = List(30) {
                GrainDot(
                    xFrac = rnd.nextFloat(),
                    yFrac = rnd.nextFloat(),
                    radius = rnd.nextFloat() * 1.2f + 0.3f,
                    alpha = rnd.nextFloat() * 0.18f + 0.03f,
                )
            },
            scanLines = List(6) { rnd.nextFloat() },
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════
//  Animation value bundles
// ═══════════════════════════════════════════════════════════════════════

data class EntryAnimations(
    /** 0→1 overall card visibility ramp. */
    val materializeAlpha: Float,
    /** 0→1 scan-line position (top→bottom). */
    val scanSweep: Float,
    /** 1→0 intensity of all glitch artifacts. */
    val glitchIntensity: Float,
    /** Horizontal px displacement that dampens with [glitchIntensity]. */
    val horizontalJitter: Float,
    /** 0→1 scale for badge spring pop-in. */
    val badgeScale: Float,
    /** Raw high-freq phase used for flicker math. */
    val jitterPhase: Float,
)

data class AmbientAnimations(
    /** Fraction 0→1 repeating; slow scan-line position on selected card. */
    val scanY: Float,
    /** 0→1 repeating; border luminance cycle. */
    val borderPulse: Float,
    /** 1.0→1.2 repeating; timeline node breathing scale. */
    val nodeScale: Float,
    /** 0→1 repeating; energy-flow particle position on connector line. */
    val connectorFlow: Float,
) {
    companion object {
        val IDLE = AmbientAnimations(
            scanY = -1f,
            borderPulse = 0f,
            nodeScale = 1f,
            connectorFlow = 0f,
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════
//  Entry animation orchestrator
//
//  Timeline:
//    0ms  ─ stagger delay (entryIndex × 160ms)
//    +0ms ─ materialize alpha ramp 0→1 over 550ms
//   +40ms ─ scan sweep 0→1 over 480ms
//    +0ms ─ glitch intensity 1→0 over 650ms  (RGB, blocks, grain, jitter)
//    +0ms ─ jitter phase  0→80  over 650ms  (high-freq oscillation source)
//  +420ms ─ badge scale  0→1  via spring   (overshoot pop-in)
// ═══════════════════════════════════════════════════════════════════════

@Composable
fun rememberEntryAnimations(entryIndex: Int): EntryAnimations {
    val materialize = remember { Animatable(0f) }
    val scanSweep = remember { Animatable(0f) }
    val glitchIntensity = remember { Animatable(1f) }
    val jitterPhase = remember { Animatable(0f) }
    val badgeScale = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(entryIndex * 160L)
        coroutineScope {
            launch {
                materialize.animateTo(1f, tween(550, easing = FastOutSlowInEasing))
            }
            launch {
                scanSweep.animateTo(1f, tween(480, delayMillis = 40, easing = LinearOutSlowInEasing))
            }
            launch {
                glitchIntensity.animateTo(0f, tween(650, easing = LinearOutSlowInEasing))
            }
            launch {
                jitterPhase.animateTo(80f, tween(650, easing = LinearEasing))
            }
            launch {
                delay(420)
                badgeScale.animateTo(1f, spring(dampingRatio = 0.55f, stiffness = 350f))
            }
        }
    }

    val jitter = sin(jitterPhase.value * 17.3f) * glitchIntensity.value * 8f

    return EntryAnimations(
        materializeAlpha = materialize.value,
        scanSweep = scanSweep.value,
        glitchIntensity = glitchIntensity.value,
        horizontalJitter = jitter,
        badgeScale = badgeScale.value,
        jitterPhase = jitterPhase.value,
    )
}

// ═══════════════════════════════════════════════════════════════════════
//  Ambient animations — infinite loops, only created for selected card
// ═══════════════════════════════════════════════════════════════════════

@Composable
fun rememberAmbientAnimations(active: Boolean): AmbientAnimations {
    if (!active) return AmbientAnimations.IDLE

    val inf = rememberInfiniteTransition(label = "ambient_glow")

    val scanY = inf.animateFloat(
        initialValue = -0.12f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(tween(6500, easing = LinearEasing), RepeatMode.Restart),
        label = "a_scan",
    ).value

    val borderPulse = inf.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "a_border",
    ).value

    val nodeScale = inf.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "a_node",
    ).value

    val connectorFlow = inf.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1800, easing = LinearEasing), RepeatMode.Restart),
        label = "a_flow",
    ).value

    return AmbientAnimations(scanY, borderPulse, nodeScale, connectorFlow)
}

// ═══════════════════════════════════════════════════════════════════════
//  Materialization overlay — the big show
//
//  Layers (drawn bottom-to-top):
//    1. CRT phosphor-settle dark veil over scanned region
//    2. Scan-line: halo → inner glow → bright leading edge
//    3. RGB chromatic aberration bands (red left, cyan right)
//    4. Block displacement artifacts (shifted rectangles)
//    5. Noise grain (sparse random dots)
//    6. Interference scanlines (fixed horizontal hairlines)
//    7. Stabilization flicker (rapid tinted flash near end of glitch)
// ═══════════════════════════════════════════════════════════════════════

@Composable
fun GlitchMaterializeOverlay(
    entry: EntryAnimations,
    layout: GlitchLayout,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    val sweep = entry.scanSweep
    val intensity = entry.glitchIntensity
    if (intensity <= 0.003f && sweep >= 0.997f) return

    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val scanY = sweep * h

        // ─── 1. Phosphor settle veil ────────────────────────────────
        if (sweep < 0.99f) {
            drawRect(
                color = Color.Black.copy(alpha = 0.28f * (1f - sweep)),
                topLeft = Offset.Zero,
                size = Size(w, scanY),
            )
        }

        // ─── 2. Scan line ───────────────────────────────────────────
        if (sweep in 0.008f..0.992f) {
            val haloH = 40f
            val haloTop = (scanY - haloH).coerceAtLeast(0f)
            drawRect(
                color = tint.copy(alpha = 0.09f),
                topLeft = Offset(0f, haloTop),
                size = Size(w, scanY - haloTop),
            )
            val innerH = 10f
            val innerTop = (scanY - innerH).coerceAtLeast(0f)
            drawRect(
                color = tint.copy(alpha = 0.32f),
                topLeft = Offset(0f, innerTop),
                size = Size(w, scanY - innerTop),
            )
            val edgeTop = (scanY - 2f).coerceAtLeast(0f)
            drawRect(
                color = tint.copy(alpha = 0.92f),
                topLeft = Offset(0f, edgeTop),
                size = Size(w, 2f),
            )
            // Thin trailing edge below the scan
            drawRect(
                color = tint.copy(alpha = 0.15f),
                topLeft = Offset(0f, scanY),
                size = Size(w, 4f.coerceAtMost(h - scanY)),
            )
        }

        // ─── 3. RGB chromatic aberration ────────────────────────────
        if (intensity > 0.01f) {
            for (band in layout.rgbBands) {
                val by = band.yFrac * h
                if (by < scanY) {
                    val shift = band.shiftPx * intensity
                    drawRect(
                        color = Color.Red.copy(alpha = 0.14f * intensity),
                        topLeft = Offset(shift.coerceAtLeast(0f), by),
                        size = Size(w * 0.65f, band.heightPx),
                    )
                    drawRect(
                        color = Color(0xFF44AAFF).copy(alpha = 0.11f * intensity),
                        topLeft = Offset((-shift).coerceAtLeast(0f), by + 1f),
                        size = Size(w * 0.55f, band.heightPx * 0.7f),
                    )
                }
            }
        }

        // ─── 4. Block displacement artifacts ────────────────────────
        if (intensity > 0.04f) {
            for (block in layout.noiseBlocks) {
                val by = block.yFrac * h
                if (by < scanY) {
                    val shift = block.shiftPx * intensity
                    drawRect(
                        color = tint.copy(alpha = 0.07f * intensity),
                        topLeft = Offset(
                            (block.xFrac * w + shift).coerceIn(0f, w - 10f),
                            by,
                        ),
                        size = Size(block.widthFrac * w, block.heightPx),
                    )
                }
            }
        }

        // ─── 5. Noise grain ─────────────────────────────────────────
        if (intensity > 0.02f) {
            for (dot in layout.grainDots) {
                val dy = dot.yFrac * h
                if (dy < scanY) {
                    drawCircle(
                        color = Color.White.copy(alpha = dot.alpha * intensity),
                        radius = dot.radius,
                        center = Offset(dot.xFrac * w, dy),
                    )
                }
            }
        }

        // ─── 6. Interference scanlines ──────────────────────────────
        if (intensity > 0.01f) {
            for (frac in layout.scanLines) {
                val ly = frac * h
                if (ly < scanY) {
                    drawRect(
                        color = tint.copy(alpha = 0.06f * intensity),
                        topLeft = Offset(0f, ly),
                        size = Size(w, 1f),
                    )
                }
            }
        }

        // ─── 7. Stabilization flicker ───────────────────────────────
        if (intensity in 0.012f..0.16f) {
            val flicker = sin(entry.jitterPhase * 4.1f) * 0.14f
            if (flicker > 0f) {
                drawRect(
                    color = tint.copy(alpha = flicker * intensity * 3.5f),
                    topLeft = Offset.Zero,
                    size = size,
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════
//  Ambient scan overlay — persistent subtle effects on selected card
//
//  - Slow-drifting faint scan line
//  - Edge chromatic aberration (red top, cyan bottom, barely visible)
// ═══════════════════════════════════════════════════════════════════════

@Composable
fun AmbientScanOverlay(
    ambient: AmbientAnimations,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    if (ambient.scanY < -0.5f) return // IDLE sentinel

    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val y = ambient.scanY * h

        // Faint moving scanline
        if (y in 0f..h) {
            drawRect(
                color = tint.copy(alpha = 0.035f),
                topLeft = Offset(0f, (y - 24f).coerceAtLeast(0f)),
                size = Size(w, 24f),
            )
            drawRect(
                color = tint.copy(alpha = 0.09f),
                topLeft = Offset(0f, (y - 1f).coerceAtLeast(0f)),
                size = Size(w, 1.5f),
            )
        }

        // Persistent edge chromatic aberration
        drawRect(
            color = Color.Red.copy(alpha = 0.022f),
            topLeft = Offset(1.5f, 0f),
            size = Size(w, 4f),
        )
        drawRect(
            color = Color(0xFF44AAFF).copy(alpha = 0.018f),
            topLeft = Offset(-1f, h - 4f),
            size = Size(w, 4f),
        )
    }
}
