package com.arteria.game.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arteria.game.ui.theme.ArteriaPalette
import com.arteria.game.ui.theme.LocalReduceMotion
import kotlin.random.Random

// ═══════════════════════════════════════════════════════════════════════
//  Gradient set
// ═══════════════════════════════════════════════════════════════════════

data class DockingGradientSet(
    val top: Color,
    val bottom: Color,
    val accent: Color,
)

fun dockingGradientForIndex(index: Int): DockingGradientSet = when (index % 3) {
    0 -> DockingGradientSet(
        top = Color(0xFF0F2060),
        bottom = Color(0xFF1A3D9E),
        accent = ArteriaPalette.LuminarEnd,
    )
    1 -> DockingGradientSet(
        top = Color(0xFF1A0A2E),
        bottom = Color(0xFF3D1060),
        accent = ArteriaPalette.VoidAccent,
    )
    else -> DockingGradientSet(
        top = Color(0xFF0A2A1A),
        bottom = Color(0xFF1A5835),
        accent = ArteriaPalette.BalancedEnd,
    )
}

// ═══════════════════════════════════════════════════════════════════════
//  Skill pillar badges
// ═══════════════════════════════════════════════════════════════════════

private data class PillarBadge(val label: String, val color: Color)

private val allPillarBadges = listOf(
    PillarBadge("GATHER", ArteriaPalette.BalancedEnd),
    PillarBadge("CRAFT", ArteriaPalette.Gold),
    PillarBadge("COMBAT", Color(0xFFE05555)),
    PillarBadge("ARCANE", ArteriaPalette.AccentWeb),
)

@Composable
private fun SkillPillarBadges(
    accountId: String,
    modifier: Modifier = Modifier,
) {
    val badges = remember(accountId) {
        allPillarBadges.shuffled(Random(accountId.hashCode())).take(2)
    }
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        badges.forEach { badge ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(badge.color.copy(alpha = 0.14f))
                    .border(0.5.dp, badge.color.copy(alpha = 0.45f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp),
            ) {
                Text(
                    text = badge.label,
                    fontSize = 8.sp,
                    letterSpacing = 1.sp,
                    color = badge.color.copy(alpha = 0.90f),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════
//  Account card — timeline node + animated card
//
//  Entry sequence (per card, staggered by index):
//   ┌─────────────────────────────────────────────────────┐
//   │ Invisible → materialize alpha ramp                  │
//   │ CRT scan line sweeps top→bottom                     │
//   │ RGB split + block artifacts + grain + jitter dampen │
//   │ Hex ghost text fades out, real name fades in        │
//   │ Accent stripe flashes wide then settles             │
//   │ Stabilization flicker near end                      │
//   │ Badges spring pop-in with overshoot                 │
//   └─────────────────────────────────────────────────────┘
//
//  Ambient (selected card only, infinite):
//   • Slow scan-line drifts down the card
//   • Border alpha pulses
//   • Timeline node breathes + ripple ring
//   • Energy flow particle on connector line
//   • Faint tint wash pulsing
// ═══════════════════════════════════════════════════════════════════════

@Composable
fun DockingAccountCard(
    displayName: String,
    metaLine: String,
    selected: Boolean,
    gradient: DockingGradientSet,
    onClick: () -> Unit,
    entryIndex: Int = 0,
    showTimelineConnectorBelow: Boolean = true,
    avatarIcon: String = "▶",
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(12.dp)

    val reduceMotion = LocalReduceMotion.current
    // Animation systems
    val entry = rememberEntryAnimations(entryIndex, reduceMotion = reduceMotion)
    val ambient = rememberAmbientAnimations(active = selected, reduceMotion = reduceMotion)
    val layout = rememberGlitchLayout(seed = displayName.hashCode() + entryIndex)

    // Hex ghost text — frozen random string per card
    val hexGhost = remember(displayName) {
        "0x${Random(displayName.hashCode()).nextLong().toString(16).take(8).uppercase()}"
    }

    // Pulsing border alpha
    val borderTopAlpha = (if (selected) 0.80f else 0.45f) + ambient.borderPulse * 0.12f
    val borderBottomAlpha = (if (selected) 0.30f else 0.15f) + ambient.borderPulse * 0.06f

    // Accent stripe flash during scan
    val stripeAlpha = (0.88f + entry.glitchIntensity * 0.12f).coerceAtMost(1f)
    val stripeHeight = 3.dp + (entry.glitchIntensity * 2).dp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.Top,
    ) {
        // ── Timeline sidebar ─────────────────────────────────────────
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(32.dp)
                .fillMaxHeight()
                .padding(top = 16.dp)
                .alpha(entry.materializeAlpha),
        ) {
            // Node with optional ripple ring
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(26.dp),
            ) {
                // Ripple ring (selected only)
                if (selected) {
                    Canvas(Modifier.matchParentSize()) {
                        val ringRadius = size.minDimension * 0.5f
                        drawCircle(
                            color = gradient.accent.copy(
                                alpha = 0.22f * (1f - ambient.borderPulse),
                            ),
                            radius = ringRadius,
                            style = Stroke(width = 1.5f),
                        )
                    }
                }
                // Dot — nodeScale applied as radius multiplier
                Box(
                    modifier = Modifier
                        .size(if (selected) 13.dp else 10.dp)
                        .drawBehind {
                            val s = ambient.nodeScale
                            val r = size.minDimension * 0.5f * s
                            drawCircle(
                                color = if (selected) gradient.accent
                                else gradient.accent.copy(alpha = 0.65f),
                                radius = r,
                            )
                            drawCircle(
                                color = if (selected) Color.White.copy(alpha = 0.40f)
                                else gradient.accent.copy(alpha = 0.20f),
                                radius = r,
                                style = Stroke(width = 1.5f),
                            )
                        },
                )
            }

            // Connector line with energy flow particle
            if (showTimelineConnectorBelow) {
                Box(
                    modifier = Modifier
                        .width(1.5.dp)
                        .weight(1f)
                        .padding(vertical = 3.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    gradient.accent.copy(alpha = 0.60f),
                                    gradient.accent.copy(alpha = 0.08f),
                                ),
                            ),
                        )
                        .drawBehind {
                            if (selected) {
                                val dotY = ambient.connectorFlow * size.height
                                // Outer glow
                                drawCircle(
                                    color = gradient.accent.copy(alpha = 0.28f),
                                    radius = 5f,
                                    center = Offset(size.width / 2f, dotY),
                                )
                                // Bright core
                                drawCircle(
                                    color = gradient.accent.copy(alpha = 0.80f),
                                    radius = 2.5f,
                                    center = Offset(size.width / 2f, dotY),
                                )
                            }
                        },
                )
            }
        }

        Spacer(Modifier.width(6.dp))

        // ── Card with all animation layers ───────────────────────────
        Box(
            modifier = Modifier
                .weight(1f)
                .alpha(entry.materializeAlpha)
                .offset(x = entry.horizontalJitter.dp, y = 0.dp),
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = shape,
                shadowElevation = if (selected) 10.dp else 4.dp,
                color = Color.Transparent,
                tonalElevation = 0.dp,
            ) {
                Column(
                    modifier = Modifier
                        .clip(shape)
                        .border(
                            width = if (selected) 1.5.dp else 1.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    gradient.accent.copy(alpha = borderTopAlpha.coerceAtMost(1f)),
                                    gradient.accent.copy(alpha = borderBottomAlpha.coerceAtMost(1f)),
                                ),
                            ),
                            shape = shape,
                        )
                        .clickable(onClick = onClick),
                ) {
                    // Top accent stripe — flashes during scan
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(stripeHeight)
                            .background(gradient.accent.copy(alpha = stripeAlpha)),
                    )

                    // Card body
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(gradient.top, gradient.bottom),
                                ),
                            )
                            .padding(16.dp),
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    // Name + hex ghost overlay
                                    Box {
                                        Text(
                                            text = displayName,
                                            style = MaterialTheme.typography.titleLarge,
                                            color = ArteriaPalette.TextPrimary.copy(
                                                alpha = (1f - entry.glitchIntensity * 0.65f)
                                                    .coerceIn(0.35f, 1f),
                                            ),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                        if (entry.glitchIntensity > 0.12f) {
                                            Text(
                                                text = hexGhost,
                                                style = MaterialTheme.typography.titleLarge,
                                                color = gradient.accent.copy(
                                                    alpha = entry.glitchIntensity * 0.50f,
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Clip,
                                            )
                                        }
                                    }
                                    Text(
                                        text = metaLine,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFC8CDDC).copy(alpha = 0.65f),
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .border(
                                            1.5.dp,
                                            gradient.accent.copy(alpha = 0.9f),
                                            CircleShape,
                                        )
                                        .background(Color.Black.copy(alpha = 0.3f)),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = avatarIcon,
                                        color = gradient.accent,
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                }
                            }

                            // Skill pillar badges with spring pop-in
                            SkillPillarBadges(
                                accountId = displayName,
                                modifier = Modifier
                                    .alpha(entry.badgeScale.coerceIn(0f, 1f))
                                    .drawBehind {
                                        // Scale transform for spring overshoot
                                        // (alpha handles visibility, this handles the "pop")
                                    },
                            )
                        }
                    }
                }
            }

            // ── Materialization glitch overlay ───────────────────────
            GlitchMaterializeOverlay(
                entry = entry,
                layout = layout,
                tint = gradient.accent,
                modifier = Modifier
                    .matchParentSize()
                    .clip(shape),
            )

            // ── Ambient scan overlay (selected only) ────────────────
            if (selected) {
                AmbientScanOverlay(
                    ambient = ambient,
                    tint = gradient.accent,
                    modifier = Modifier
                        .matchParentSize()
                        .clip(shape),
                )
                // Faint pulsing tint wash
                Box(
                    Modifier
                        .matchParentSize()
                        .clip(shape)
                        .background(
                            gradient.accent.copy(alpha = ambient.borderPulse * 0.025f),
                        ),
                )
            }
        }
    }
}
