package com.arteria.game.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** Text/icon colors that follow space gradient (dark vs light shell). */
object ArteriaContentColors {
    @Composable
    fun primary(): Color =
        if (LocalArteriaDarkSpace.current) ArteriaPalette.TextPrimary else ArteriaPalette.LightTextPrimary

    @Composable
    fun secondary(): Color =
        if (LocalArteriaDarkSpace.current) ArteriaPalette.TextSecondary else ArteriaPalette.LightTextSecondary

    @Composable
    fun muted(): Color =
        if (LocalArteriaDarkSpace.current) ArteriaPalette.TextMuted else ArteriaPalette.LightTextMuted

    @Composable
    fun cardSurface(): Color =
        if (LocalArteriaDarkSpace.current) ArteriaPalette.BgCard else ArteriaPalette.LightBgCard

    @Composable
    fun border(): Color =
        if (LocalArteriaDarkSpace.current) ArteriaPalette.Border else ArteriaPalette.LightBorder
}
