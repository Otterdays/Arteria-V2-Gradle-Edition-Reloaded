package com.arteria.game.ui.game

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.arteria.game.ui.theme.ArteriaPalette
import kotlin.math.cos
import kotlin.math.sin

/**
 * Alive bottom navigation bar. Replaces the stock Material NavigationBar with:
 * - Custom themed vector icons (pickaxe / vault / crossed swords)
 * - Animated XP progress ring around the Skills icon when training
 * - Gold opportunity badge on Bank when crafting is affordable
 * - Radial glow on selected tab using Arteria accent palette
 * - Cinzel labels (via MaterialTheme.typography.labelSmall)
 * - Gradient top-edge that blends into the space backdrop behind it
 */
@Composable
fun ArteriaBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    activeTrainingProgress: Float,
    hasTrainingActive: Boolean,
    bankHasOpportunity: Boolean,
    modifier: Modifier = Modifier,
) {
    // Gradient bleeds upward — transparent at top, opaque at bottom — blends into space backdrop
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    0f to Color.Transparent,
                    0.28f to ArteriaPalette.BgCard.copy(alpha = 0.88f),
                    1f to ArteriaPalette.BgCard,
                ),
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Skills tab — XP ring when training
            NavTab(
                selected = selectedTab == 0,
                label = "Skills",
                onClick = { onTabSelected(0) },
                modifier = Modifier.weight(1f),
            ) { tint ->
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(38.dp)) {
                    if (hasTrainingActive) {
                        XpProgressRing(
                            progress = activeTrainingProgress,
                            color = tint,
                            modifier = Modifier.matchParentSize(),
                        )
                    }
                    PickaxeIcon(color = tint, modifier = Modifier.size(20.dp))
                }
            }

            // Bank tab — gold dot badge when crafting is affordable
            NavTab(
                selected = selectedTab == 1,
                label = "Bank",
                onClick = { onTabSelected(1) },
                modifier = Modifier.weight(1f),
            ) { tint ->
                Box(modifier = Modifier.size(38.dp)) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.matchParentSize(),
                    ) {
                        VaultIcon(color = tint, modifier = Modifier.size(20.dp))
                    }
                    if (bankHasOpportunity) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .align(Alignment.TopEnd)
                                .background(ArteriaPalette.Gold, CircleShape),
                        )
                    }
                }
            }

            // Combat tab
            NavTab(
                selected = selectedTab == 2,
                label = "Combat",
                onClick = { onTabSelected(2) },
                modifier = Modifier.weight(1f),
            ) { tint ->
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(38.dp)) {
                    SwordsIcon(color = tint, modifier = Modifier.size(20.dp))
                }
            }
        }
        // Push content above gesture navigation bar
        Spacer(Modifier.navigationBarsPadding())
    }
}

@Composable
private fun NavTab(
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconContent: @Composable (tint: Color) -> Unit,
) {
    val tint by animateColorAsState(
        targetValue = if (selected) ArteriaPalette.AccentPrimary else ArteriaPalette.TextMuted,
        animationSpec = tween(250),
        label = "tab_tint",
    )
    val glowAlpha by animateFloatAsState(
        targetValue = if (selected) 0.22f else 0f,
        animationSpec = tween(300),
        label = "tab_glow",
    )

    Column(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(42.dp)
                .drawBehind {
                    if (glowAlpha > 0f) {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    ArteriaPalette.AccentPrimary.copy(alpha = glowAlpha),
                                    ArteriaPalette.LuminarEnd.copy(alpha = glowAlpha * 0.4f),
                                    Color.Transparent,
                                ),
                                center = center,
                                radius = size.minDimension * 0.75f,
                            ),
                        )
                    }
                },
        ) {
            iconContent(tint)
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = tint,
        )
    }
}

// ─── XP Progress Ring ────────────────────────────────────────────────────────

@Composable
private fun XpProgressRing(
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(800),
        label = "xp_ring",
    )
    Canvas(modifier = modifier) {
        val stroke = Stroke(width = 2.8.dp.toPx(), cap = StrokeCap.Round)
        val inset = stroke.width / 2f
        val arcSize = Size(size.width - inset * 2, size.height - inset * 2)
        val topLeft = Offset(inset, inset)

        // Track (dim)
        drawArc(
            color = color.copy(alpha = 0.18f),
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            style = stroke,
            size = arcSize,
            topLeft = topLeft,
        )
        // Progress arc
        if (animatedProgress > 0f) {
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = stroke,
                size = arcSize,
                topLeft = topLeft,
            )
        }
    }
}

// ─── Custom Icons ─────────────────────────────────────────────────────────────

/** Pickaxe — gathering / skills identity icon */
@Composable
private fun PickaxeIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Handle: diagonal thick line bottom-left → upper-right
        drawLine(
            color = color,
            start = Offset(w * 0.18f, h * 0.88f),
            end = Offset(w * 0.62f, h * 0.44f),
            strokeWidth = w * 0.11f,
            cap = StrokeCap.Round,
        )

        // Pick head: filled blade pointing upper-right
        val head = Path().apply {
            moveTo(w * 0.54f, h * 0.52f)
            lineTo(w * 0.80f, h * 0.10f)
            lineTo(w * 0.93f, h * 0.22f)
            lineTo(w * 0.70f, h * 0.56f)
            close()
        }
        drawPath(head, color)

        // Butt cap: small filled wedge at handle base
        val butt = Path().apply {
            moveTo(w * 0.20f, h * 0.82f)
            lineTo(w * 0.06f, h * 0.68f)
            lineTo(w * 0.16f, h * 0.62f)
            lineTo(w * 0.30f, h * 0.76f)
            close()
        }
        drawPath(butt, color)
    }
}

/** Vault door — bank / storage icon */
@Composable
private fun VaultIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w * 0.5f
        val cy = h * 0.5f
        val strokeW = w * 0.09f

        // Outer rim
        drawCircle(
            color = color,
            radius = w * 0.43f,
            center = Offset(cx, cy),
            style = Stroke(width = strokeW),
        )
        // Combination dial
        drawCircle(
            color = color,
            radius = w * 0.17f,
            center = Offset(cx, cy),
            style = Stroke(width = strokeW * 0.75f),
        )
        // Center pin
        drawCircle(color = color, radius = w * 0.04f, center = Offset(cx, cy))

        // 4 locking bolts at 45° compass positions
        val boltInner = w * 0.22f
        val boltOuter = w * 0.37f
        listOf(45.0, 135.0, 225.0, 315.0).forEach { deg ->
            val rad = Math.toRadians(deg)
            drawLine(
                color = color,
                start = Offset(cx + (boltInner * cos(rad)).toFloat(), cy + (boltInner * sin(rad)).toFloat()),
                end = Offset(cx + (boltOuter * cos(rad)).toFloat(), cy + (boltOuter * sin(rad)).toFloat()),
                strokeWidth = strokeW * 0.75f,
                cap = StrokeCap.Round,
            )
        }
    }
}

/** Two crossed swords — combat icon */
@Composable
private fun SwordsIcon(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val blade = w * 0.075f
        val handle = w * 0.13f

        // ── Sword 1: top-left blade → bottom-right handle ──
        // Blade
        drawLine(color, Offset(w * 0.08f, h * 0.08f), Offset(w * 0.60f, h * 0.60f), blade, cap = StrokeCap.Round)
        // Handle (thicker)
        drawLine(color, Offset(w * 0.60f, h * 0.60f), Offset(w * 0.88f, h * 0.88f), handle, cap = StrokeCap.Round)
        // Crossguard (perpendicular at 1/3 from tip)
        drawLine(color, Offset(w * 0.27f, h * 0.47f), Offset(w * 0.47f, h * 0.27f), blade * 1.4f, cap = StrokeCap.Round)

        // ── Sword 2: top-right blade → bottom-left handle ──
        // Blade
        drawLine(color, Offset(w * 0.92f, h * 0.08f), Offset(w * 0.40f, h * 0.60f), blade, cap = StrokeCap.Round)
        // Handle (thicker)
        drawLine(color, Offset(w * 0.40f, h * 0.60f), Offset(w * 0.12f, h * 0.88f), handle, cap = StrokeCap.Round)
        // Crossguard
        drawLine(color, Offset(w * 0.53f, h * 0.27f), Offset(w * 0.73f, h * 0.47f), blade * 1.4f, cap = StrokeCap.Round)
    }
}
