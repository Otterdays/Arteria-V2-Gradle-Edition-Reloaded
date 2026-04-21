package com.arteria.game.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.arteria.game.ui.theme.ArteriaPalette

/**
 * Visual "Juice" components for the "Mad" thematic feel.
 * Includes moving grids, scanlines, and holographic particles.
 */

@Composable
fun HoloGridBackground(
    modifier: Modifier = Modifier,
    gridSize: Float = 60f,
    lineColor: Color = ArteriaPalette.GridLine,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "holo_grid")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = gridSize,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "grid_offset"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Vertical lines
        var x = offset
        while (x < width + gridSize) {
            drawLine(
                color = lineColor,
                start = Offset(x, 0f),
                end = Offset(x, height),
                strokeWidth = 1f
            )
            x += gridSize
        }

        // Horizontal lines
        var y = offset
        while (y < height + gridSize) {
            drawLine(
                color = lineColor,
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1f
            )
            y += gridSize
        }
    }
}

@Composable
fun ScanlineOverlay(
    modifier: Modifier = Modifier,
    lineColor: Color = ArteriaPalette.Scanline,
    lineSpacing: Float = 8f
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val height = size.height
        val width = size.width
        var y = 0f
        while (y < height) {
            drawLine(
                color = lineColor,
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 2f
            )
            y += lineSpacing
        }
    }
}

@Composable
fun CyberHUD(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        HoloGridBackground()
        content()
        ScanlineOverlay()
    }
}
