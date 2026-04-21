package com.arteria.game.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.arteria.game.R

/**
 * Dark-first palette aligned with `apps/mobile/constants/theme.ts` (DARK_PALETTE +
 * character-select "Docking Station" gradient).
 */
object ArteriaPalette {
    val BgDeepSpaceTop = Color(0xFF020408)
    val BgDeepSpaceMid = Color(0xFF060D16)
    val BgDeepSpaceBottom = Color(0xFF080B1A)

    val BgApp = Color(0xFF0F111A)
    val BgCard = Color(0xFF1B1E29)
    val BgCardHover = Color(0xFF232636)
    val BgInput = Color(0xFF161825)

    val TextPrimary = Color(0xFFE8E9ED)
    val TextSecondary = Color(0xFF8B8FA3)
    val TextMuted = Color(0xFF6C7085)

    val AccentPrimary = Color(0xFF4A90E2)
    val AccentHover = Color(0xFF6AA3F5)
    val AccentWeb = Color(0xFF8B5CF6)
    val Gold = Color(0xFFF59E0B)
    val GoldDim = Color(0xFFC49B1A)

    val Border = Color(0xFF2B2F3D)
    val Divider = Color(0xFF252837)

    val LuminarEnd = Color(0xFF5B8CFF)
    val VoidAccent = Color(0xFF9B59B6)
    val BalancedEnd = Color(0xFF2ECC71)

    /** Combat pillar accent (skills grid / detail headers). */
    val CombatAccent = Color(0xFFE74C3C)

    /** Glassmorphism surface tokens — subtle translucency layer. */
    val GlassOverlay = Color(0x0AFFFFFF)   // ~4% white
    val GlassHighlight = Color(0x14FFFFFF) // ~8% white

    /** Light-mode glass counterparts. */
    val LightGlassOverlay = Color(0x08000000)   // ~3% black
    val LightGlassHighlight = Color(0x0D000000) // ~5% black

    /** Day / follow-system light shell (game + settings + docking). */
    val LightSpaceTop = Color(0xFFE4E9F5)
    val LightSpaceMid = Color(0xFFD4DCEF)
    val LightSpaceBottom = Color(0xFFC8D4E8)
    val LightBgCard = Color(0xFFF2F4FA)
    val LightTextPrimary = Color(0xFF1A1F2E)
    val LightTextSecondary = Color(0xFF4A5168)
    val LightTextMuted = Color(0xFF6B7288)
    val LightBorder = Color(0xFFC5CDDF)

    /** Cybernetic / Mad polish tokens. */
    val HoloBlue = Color(0xFF00F5FF)
    val HoloBlueDim = Color(0x3300F5FF)
    val CyberPink = Color(0xFFFF00E5)
    val CyberPinkDim = Color(0x33FF00E5)
    val MatrixGreen = Color(0xFF00FF41)
    val MatrixGreenDim = Color(0x3300FF41)
    val Scanline = Color(0x08FFFFFF)
    val GridLine = Color(0x05FFFFFF)
}

private val ArteriaDarkScheme = darkColorScheme(
    primary = ArteriaPalette.AccentPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF3A6DB5),
    onPrimaryContainer = Color(0xFFE8E9ED),
    secondary = ArteriaPalette.Gold,
    onSecondary = Color(0xFF1A1204),
    tertiary = ArteriaPalette.AccentWeb,
    onTertiary = Color.White,
    background = ArteriaPalette.BgDeepSpaceTop,
    onBackground = ArteriaPalette.TextPrimary,
    surface = ArteriaPalette.BgCard,
    onSurface = ArteriaPalette.TextPrimary,
    surfaceVariant = ArteriaPalette.BgCardHover,
    onSurfaceVariant = ArteriaPalette.TextSecondary,
    outline = ArteriaPalette.Border,
    outlineVariant = ArteriaPalette.Divider,
)

private val ArteriaLightScheme = lightColorScheme(
    primary = Color(0xFF2A6BB5),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD3E4FA),
    onPrimaryContainer = Color(0xFF0D2847),
    secondary = Color(0xFFB45309),
    onSecondary = Color.White,
    tertiary = Color(0xFF6D28D9),
    onTertiary = Color.White,
    background = ArteriaPalette.LightSpaceTop,
    onBackground = ArteriaPalette.LightTextPrimary,
    surface = ArteriaPalette.LightBgCard,
    onSurface = ArteriaPalette.LightTextPrimary,
    surfaceVariant = Color(0xFFE8ECF5),
    onSurfaceVariant = ArteriaPalette.LightTextSecondary,
    outline = ArteriaPalette.LightBorder,
    outlineVariant = Color(0xFFD8DEEA),
)

/**
 * **Cinzel** — matches Expo `Cinzel_400Regular` / `Cinzel_700Bold` (`apps/mobile` root layout).
 * Bundled variable font: `res/font/cinzel.ttf` ([google/fonts ofl/cinzel](https://github.com/google/fonts/tree/main/ofl/cinzel), OFL).
 */
private val Cinzel = FontFamily(
    Font(R.font.cinzel, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.cinzel, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.cinzel, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.cinzel, FontWeight.Bold, FontStyle.Normal),
)

private val ArteriaTypography = Typography(
    displaySmall = TextStyle(
        fontFamily = Cinzel,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = 1.5.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = Cinzel,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        letterSpacing = 0.5.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = Cinzel,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = Cinzel,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 19.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 17.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = Cinzel,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        letterSpacing = 3.sp,
    ),
)

@Composable
fun ArteriaTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) ArteriaDarkScheme else ArteriaLightScheme,
        typography = ArteriaTypography,
        content = content,
    )
}

@Composable
fun rememberArteriaSpaceBackgroundBrush(useDarkSpace: Boolean): Brush {
    return remember(useDarkSpace) {
        arteriaSpaceBackgroundBrush(useDarkSpace)
    }
}

fun arteriaSpaceBackgroundBrush(useDarkSpace: Boolean): Brush = if (useDarkSpace) {
    Brush.verticalGradient(
        colors = listOf(
            ArteriaPalette.BgDeepSpaceTop,
            ArteriaPalette.BgDeepSpaceMid,
            ArteriaPalette.BgDeepSpaceBottom,
        ),
    )
} else {
    Brush.verticalGradient(
        colors = listOf(
            ArteriaPalette.LightSpaceTop,
            ArteriaPalette.LightSpaceMid,
            ArteriaPalette.LightSpaceBottom,
        ),
    )
}
