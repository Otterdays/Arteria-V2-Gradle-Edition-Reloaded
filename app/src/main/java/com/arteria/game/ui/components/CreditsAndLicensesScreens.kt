package com.arteria.game.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.arteria.game.ui.theme.ArteriaContentColors
import com.arteria.game.ui.theme.ArteriaPalette
import com.arteria.game.ui.theme.LocalArteriaDarkSpace
import com.arteria.game.ui.theme.rememberArteriaSpaceBackgroundBrush

@Composable
fun OpenSourceNoticesScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val darkSpace = LocalArteriaDarkSpace.current
    val bg = rememberArteriaSpaceBackgroundBrush(darkSpace)
    BackHandler(onBack = onBack)
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bg)
            .statusBarsPadding()
            .padding(horizontal = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = ArteriaContentColors.secondary(),
                )
            }
            Text(
                "OPEN SOURCE",
                style = MaterialTheme.typography.labelSmall,
                color = ArteriaPalette.Gold,
            )
        }
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                "Third-party libraries used in this build. For full attribution, see project SBOM.",
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaContentColors.muted(),
            )
            for (lib in ARTERIA_OSS_NOTICES) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = ArteriaContentColors.cardSurface().copy(alpha = 0.92f),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(lib.name, style = MaterialTheme.typography.titleMedium, color = ArteriaContentColors.primary())
                        Text(
                            "${lib.license} · ${lib.url}",
                            style = MaterialTheme.typography.bodySmall,
                            color = ArteriaContentColors.secondary(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CreditsScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val darkSpace = LocalArteriaDarkSpace.current
    val bg = rememberArteriaSpaceBackgroundBrush(darkSpace)
    BackHandler(onBack = onBack)
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bg)
            .statusBarsPadding()
            .padding(horizontal = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = ArteriaContentColors.secondary(),
                )
            }
            Text(
                "CREDITS",
                style = MaterialTheme.typography.labelSmall,
                color = ArteriaPalette.Gold,
            )
        }
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                "Arteria V2 — Gradle Edition Reloaded",
                style = MaterialTheme.typography.titleLarge,
                color = ArteriaContentColors.primary(),
            )
            Text(
                "Native Android client: Kotlin, Jetpack Compose, Room. Game design lineage from the Arteria " +
                    "project; see bundled DOCS for design truth.",
                style = MaterialTheme.typography.bodyMedium,
                color = ArteriaContentColors.secondary(),
            )
            Text(
                "Typography: Cinzel (OFL), bundled under res/font.",
                style = MaterialTheme.typography.bodyMedium,
                color = ArteriaContentColors.secondary(),
            )
        }
    }
}
