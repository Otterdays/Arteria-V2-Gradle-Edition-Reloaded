package com.arteria.game.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arteria.game.ui.theme.ArteriaPalette

// ─────────────────────────────────────────────────────────────────────────
//  Version history data
// ─────────────────────────────────────────────────────────────────────────

data class ChangelogEntry(
    val version: String,
    val date: String,
    val tag: String,           // e.g. "UI", "ENGINE", "FIX"
    val tagColor: Color,
    val changes: List<String>,
)

val APP_CHANGELOG: List<ChangelogEntry> = listOf(
    ChangelogEntry(
        version = "1.3.0",
        date = "2026-04-01",
        tag = "SKILLS",
        tagColor = ArteriaPalette.BalancedEnd,
        changes = listOf(
            "Six trainable skill lines in the registry: Mining, Logging, Fishing, Harvesting, Smithing, Cooking",
            "Tiered actions, XP, timers, and bank resources for each implemented skill",
            "Coming Soon dialog when tapping skills not yet wired — replaces empty detail confusion",
            "Game DB v2: offline catch-up audit field persisted across saves and migrations",
            "Atomic Room transactions for game saves; offline tick simulation off the UI thread",
            "Custom bottom bar (Skills · Bank · Combat) with animated tab chrome",
        ),
    ),
    ChangelogEntry(
        version = "1.2.0",
        date = "2026-03-31",
        tag = "UI",
        tagColor = ArteriaPalette.AccentPrimary,
        changes = listOf(
            "Docking Station glitch materialization — CRT scan sweep + RGB aberration on account cards",
            "Timeline sidebar with animated node, ripple ring, and energy flow particle",
            "Skill pillar badges (GATHER / CRAFT / COMBAT / ARCANE) with spring pop-in",
            "Hex ghost text overlay during card entry animation",
            "Ambient selected-card effects: slow scan drift, border pulse, tint wash",
            "\"Enter timeline\" CTA button on account selection",
        ),
    ),
    ChangelogEntry(
        version = "1.1.0",
        date = "2026-03-31",
        tag = "UI",
        tagColor = ArteriaPalette.AccentPrimary,
        changes = listOf(
            "Settings moved from bottom nav tab to full-screen overlay",
            "TopAppBar added to game screen — account name + gear icon",
            "Android back-press closes settings overlay",
            "Bottom nav reduced to 3 core tabs: Skills · Bank · Combat",
        ),
    ),
    ChangelogEntry(
        version = "1.0.0",
        date = "2026-03-30",
        tag = "LAUNCH",
        tagColor = ArteriaPalette.Gold,
        changes = listOf(
            "Room-backed account profiles — create, select, persist across sessions",
            "Game engine: tick system, XP table, 16 skills across 4 pillars",
            "Offline progression — XP and resources calculated from last save timestamp",
            "Skills screen with training actions and XP progress",
            "Bank screen — resource inventory per profile",
            "Level-up snackbar notifications",
            "Offline report dialog on app resume",
        ),
    ),
    ChangelogEntry(
        version = "0.1.0",
        date = "2026-03-22",
        tag = "FOUNDATION",
        tagColor = ArteriaPalette.BalancedEnd,
        changes = listOf(
            "Initial Compose shell with Gradle 9.6 + AGP 9.1 + JDK 21",
            "Cinzel variable font bundled (display / title typography)",
            "Animated space backdrop — twinkling stars, nebula blobs, gradient breath",
            "Account selection and creation screens (Docking Station theme)",
            "Navigation scaffold with NavHost and URI-encoded profile routing",
        ),
    ),
)

// ─────────────────────────────────────────────────────────────────────────
//  Changelog screen — full-screen overlay
// ─────────────────────────────────────────────────────────────────────────

@Composable
fun ChangelogScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bgBrush = Brush.verticalGradient(
        colors = listOf(
            ArteriaPalette.BgDeepSpaceTop,
            ArteriaPalette.BgDeepSpaceMid,
            ArteriaPalette.BgDeepSpaceBottom,
        ),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgBrush)
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = ArteriaPalette.TextSecondary,
                )
            }
            Column {
                Text(
                    text = "WHAT'S NEW",
                    style = MaterialTheme.typography.labelSmall,
                    color = ArteriaPalette.Gold,
                )
                Text(
                    text = "Version History",
                    style = MaterialTheme.typography.titleMedium,
                    color = ArteriaPalette.TextPrimary,
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(APP_CHANGELOG) { entry ->
                ChangelogCard(entry)
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun ChangelogCard(entry: ChangelogEntry) {
    val shape = RoundedCornerShape(12.dp)
    Surface(
        shape = shape,
        color = ArteriaPalette.BgCard.copy(alpha = 0.94f),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ArteriaPalette.Border, shape),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Version + tag row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "v${entry.version}",
                    style = MaterialTheme.typography.titleMedium,
                    color = ArteriaPalette.TextPrimary,
                )
                TagBadge(label = entry.tag, color = entry.tagColor)
                Spacer(Modifier.weight(1f))
                Text(
                    text = entry.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.TextMuted,
                )
            }

            Spacer(Modifier.height(10.dp))

            // Change list
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                entry.changes.forEach { change ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "·",
                            style = MaterialTheme.typography.bodySmall,
                            color = entry.tagColor.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            text = change,
                            style = MaterialTheme.typography.bodySmall,
                            color = ArteriaPalette.TextSecondary,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TagBadge(label: String, color: Color) {
    val shape = RoundedCornerShape(4.dp)
    Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        color = color.copy(alpha = 0.9f),
        modifier = Modifier
            .background(color.copy(alpha = 0.12f), shape)
            .border(0.5.dp, color.copy(alpha = 0.4f), shape)
            .padding(horizontal = 6.dp, vertical = 2.dp),
    )
}
