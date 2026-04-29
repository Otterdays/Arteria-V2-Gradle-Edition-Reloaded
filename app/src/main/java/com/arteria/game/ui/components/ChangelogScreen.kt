package com.arteria.game.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.arteria.game.ui.theme.LocalReduceMotion
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.arteria.game.BuildConfig
import com.arteria.game.ui.theme.ArteriaPalette

// ─────────────────────────────────────────────────────────────────────────
//  Version history data — keep first entry + app/build.gradle.kts in sync
//  when shipping (see CLAUDE.md / SCRATCHPAD release hygiene).
// ─────────────────────────────────────────────────────────────────────────

data class ChangelogEntry(
    val version: String,
    val date: String,
    val tag: String,           // e.g. "UI", "ENGINE", "FIX"
    val tagEmoji: String,      // e.g. "🎨", "⚙️", "🐛"
    val tagColor: Color,
    val changes: List<String>,
)

val APP_CHANGELOG: List<ChangelogEntry> = listOf(
    ChangelogEntry(
        version = "1.8.0",
        date = "2026-04-29",
        tag = "SKILLS",
        tagEmoji = "SK",
        tagColor = ArteriaPalette.VoidAccent,
        changes = listOf(
            "Summoning is now trainable: bind familiar pouches with charms, spirit shards, " +
                "and skill materials across a RuneScape-inspired progression ladder",
            "Barn Rat encounters can now drop gold charms and spirit shards, giving Summoning " +
                "an early combat-to-pouch entry loop",
            "The top-bar familiar button now opens the Summoning skill page instead of the old " +
                "standalone companion roster, keeping familiar progression in the normal skill flow",
            "Main menu polish: a fast Continue panel, richer account metadata, and a current-release " +
                "strip make returning to a save cleaner",
            "Settings panel upgrade: clearer command summary, compact session chips, and account " +
                "actions grouped with the profile card",
            "Skill detail action lists now reserve scrollable space with bottom padding, so long " +
                "recipe pages like Summoning stay usable on smaller screens",
        ),
    ),
    ChangelogEntry(
        version = "1.7.0",
        date = "2026-04-29",
        tag = "COMBAT",
        tagEmoji = "⚔️",
        tagColor = ArteriaPalette.CombatAccent,
        changes = listOf(
            "First encounter area: Sunny Meadow Barn with a Barn Rat — Engage from the Combat tab " +
                "for auto-battle ticks, HP bars, kill counter, and a scrolling combat log",
            "Combat XP splits across Attack, Strength, Defence, and Hitpoints on each kill; " +
                "rat tails can drop into your bank (new item entry)",
            "Active combat persists per profile in `game_meta` (Room v5) and resumes when you return; " +
                "skilling is blocked while a fight is active; Flee clears the encounter",
        ),
    ),
    ChangelogEntry(
        version = "1.6.0",
        date = "2026-04-29",
        tag = "FEATURE",
        tagEmoji = "💠",
        tagColor = ArteriaPalette.VoidAccent,
        changes = listOf(
            "Resonance clicker tab — pulse the orb for Resonance XP and Momentum; Momentum grants " +
                "global haste to all other training skills and decays while you play",
            "Anchor Energy builds when you train non-Resonance skills (1 per minute, cap 50). " +
                "At Lv 60+, long-press the orb for a Heavy Pulse (costs 5 Anchor, big Momentum + XP surge)",
            "Level unlocks Lv 20 / 40 / 60 / 80 / 99 (Multi-Pulse proxy, Resonant Echo, Soul Cranking, " +
                "Kinetic Feedback, Perfect Stability floor) with on-screen unlock list + Hub shortcut card",
            "Five new Resonance achievements in the Chronicle (pulses, Heavy Pulse, 100% peak Momentum)",
        ),
    ),
    ChangelogEntry(
        version = "1.5.0",
        date = "2026-04-01",
        tag = "CONTENT",
        tagEmoji = "🌱",
        tagColor = ArteriaPalette.BalancedEnd,
        changes = listOf(
            "4 new trainable skills — Farming (harvest 8 crop tiers from carrot to starfruit), " +
                "Thieving (8 tiers from market stalls to void nodes), " +
                "Woodworking (craft furniture and carvings from logged timber), " +
                "Tailoring (weave cloth and garments from harvested flax)",
            "Equipment is now live — tap ⚔️ in the top bar to open your gear panel. " +
                "Equip a weapon, tool, armor piece, and accessory — each item boosts XP gain " +
                "or improves specific skills while it's equipped",
            "Companions are now live — tap 🐾 to summon a familiar from your roster of 11. " +
                "Each companion provides a passive bonus (XP boost, extra resource drops, or " +
                "offline efficiency) while active. Only one companion can be summoned at a time",
            "Gear and companions affect every training tick — bonuses stack and apply in real time, " +
                "so swapping loadouts immediately changes how fast you progress",
            "Your equipped gear is saved — loadout persists across sessions and survives app restarts",
        ),
    ),
    ChangelogEntry(
        version = "1.4.5",
        date = "2026-04-01",
        tag = "RELEASE",
        tagEmoji = "📌",
        tagColor = ArteriaPalette.Gold,
        changes = listOf(
            "Version alignment — Settings About, README release badge, and this What's New list " +
                "match `versionName` **1.4.5** / `versionCode` **7** in `app/build.gradle.kts`",
            "Skills screen tablet layout — width-based column count (2–5) and centered max width " +
                "so skill cards stay readable on large screens",
            "Shared `PanelBackHeader` — consistent back affordance on Changelog, Open Source, and Credits",
            "Skills crossover chips — FlowRow + compact labels so multi-target skills (e.g. Logging) " +
                "don't break the grid",
            "Chronicle / achievements — deeper registry and refreshed Chronicle UI (categories, " +
                "rarity, progress)",
            "Native skills playbook — `DOCS/SKILLS_EXPANSION_NATIVE.md` kept current with roster and " +
                "implementation status",
        ),
    ),
    ChangelogEntry(
        version = "1.4.3",
        date = "2026-04-01",
        tag = "POLISH",
        tagEmoji = "✨",
        tagColor = ArteriaPalette.AccentWeb,
        changes = listOf(
            "What's New screen polish — staggered entry animations (160ms per card), tag emoji badges, " +
                "left accent stripes (tag-colored), glass highlight, improved spacing",
            "Interactive border color animation on card tap (tag color reveal)",
            "Premium typography — larger headers, Cinzel font on version labels",
            "Accessibility: respects reduce-motion setting; all colors from design tokens",
        ),
    ),
    ChangelogEntry(
        version = "1.4.2",
        date = "2026-04-01",
        tag = "QoL",
        tagEmoji = "⚡",
        tagColor = ArteriaPalette.VoidAccent,
        changes = listOf(
            "Achievement system: 18 achievements across 6 categories (Combat, Gathering, Crafting, Exploration, Mastery, Prestige), " +
                "with 🏆 icon in TopAppBar",
            "Bank enhancements: search + 4-mode sort (type/name/quantity/rarity) + withdraw presets",
            "Haptics: tactile feedback on training start/stop and achievement unlocks",
            "Random Events: 6 weighted events (tick-triggered dialog), adds game narrative",
            "Equipment/Gear system: 4 slots (head/body/hands/feet), 19 items with stat modifiers, " +
                "EquipmentScreen with drag-to-equip flow",
            "Companions/Familiars: 11 summonable companions, 5 rarities, passive skill bonuses, " +
                "CompanionsScreen with rarity filters",
            "Prestige/Ascension system: reset progression to gain 6 permanent perks (XP multiplier, " +
                "resource boost, etc.), PrestigeScreen with ascend flow confirmation",
        ),
    ),
    ChangelogEntry(
        version = "1.4.1",
        date = "2026-03-31",
        tag = "DOCS",
        tagEmoji = "📚",
        tagColor = ArteriaPalette.Gold,
        changes = listOf(
            "v1.4.1 — Settings About and What's New match this build number",
            "Release notes: this screen is the player-facing history; new ships get a new top card + version bump",
            "Contributors: when you update SCRATCHPAD for shipped work, also refresh this list and " +
                "app/build.gradle.kts version (see CLAUDE.md Doc Update Rules)",
        ),
    ),
    ChangelogEntry(
        version = "1.4.0",
        date = "2026-04-01",
        tag = "HUB",
        tagEmoji = "🎯",
        tagColor = ArteriaPalette.AccentPrimary,
        changes = listOf(
            "Hub as your home tab — dismissible offline gains card keeps flow instead of a blocking dialog",
            "Active training panel, quick stats, next-milestone nudge, smart bank-based suggestion, " +
                "recent level-up cards, and shortcuts into Skills",
            "Fourth bottom tab with Anchor hub icon — Skills, Bank, and Combat follow",
            "Herblore and Scavenging — new trainable lines with actions, XP, and bank inputs/outputs",
            "Settings parity: edit display name, last played, About version, tick/save cadence notes, " +
                "test sound",
            "Skill roster expanded toward V1 parity (incl. Cosmic pillar); unwired skills still use " +
                "Coming Soon",
        ),
    ),
    ChangelogEntry(
        version = "1.3.0",
        date = "2026-04-01",
        tag = "SKILLS",
        tagEmoji = "🎪",
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
        tagEmoji = "🎨",
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
        tagEmoji = "🎨",
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
        tagEmoji = "🚀",
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
        tagEmoji = "🏗️",
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
    /**
     * When false (default), lists only [APP_CHANGELOG] entries whose [ChangelogEntry.version]
     * matches [BuildConfig.VERSION_NAME] so players see the current update first; full history
     * is one tap away.
     */
    startWithFullHistory: Boolean = false,
) {
    val bgBrush = Brush.verticalGradient(
        colors = listOf(
            ArteriaPalette.BgDeepSpaceTop,
            ArteriaPalette.BgDeepSpaceMid,
            ArteriaPalette.BgDeepSpaceBottom,
        ),
    )

    var showFullHistory by remember(startWithFullHistory) {
        mutableStateOf(startWithFullHistory)
    }

    val currentBuildEntries = remember {
        APP_CHANGELOG.filter { it.version == BuildConfig.VERSION_NAME }
    }
    val visibleEntries = if (showFullHistory) {
        APP_CHANGELOG
    } else {
        currentBuildEntries.ifEmpty { listOf(APP_CHANGELOG.first()) }
    }
    val canShowEarlier = !showFullHistory && APP_CHANGELOG.size > visibleEntries.size

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgBrush)
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        PanelBackHeader(
            title = if (showFullHistory) "Version history" else "What's new",
            overline = if (showFullHistory) "CHANGELOG" else "CURRENT RELEASE",
            subtitle = "v${BuildConfig.VERSION_NAME} (build ${BuildConfig.VERSION_CODE})",
            onBack = onBack,
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            itemsIndexed(
                items = visibleEntries,
                key = { _, entry -> entry.version },
            ) { index, entry ->
                val reduceMotion = LocalReduceMotion.current
                val animations = rememberEntryAnimations(index, reduceMotion)

                ChangelogCard(
                    entry = entry,
                    animation = animations,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            if (canShowEarlier) {
                item {
                    TextButton(
                        onClick = { showFullHistory = true },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = ArteriaPalette.AccentWeb,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "Earlier releases",
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }
            if (showFullHistory && currentBuildEntries.isNotEmpty()) {
                item {
                    TextButton(
                        onClick = { showFullHistory = false },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = ArteriaPalette.TextMuted,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "Current release only",
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun ChangelogCard(
    entry: ChangelogEntry,
    animation: EntryAnimations,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(12.dp)
    var isPressed by remember { mutableStateOf(false) }

    val animatedBorderColor by animateColorAsState(
        targetValue = if (isPressed) entry.tagColor else ArteriaPalette.Border,
        animationSpec = tween(350),
    )

    Surface(
        shape = shape,
        color = ArteriaPalette.BgCard.copy(alpha = 0.94f),
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer(alpha = animation.materializeAlpha)
            .border(1.dp, animatedBorderColor, shape)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                )
            }
            .drawBehind {
                // Left accent stripe (3dp, gradient fade)
                val stripeW = 3.dp.toPx()
                drawRect(
                    brush = Brush.verticalGradient(
                        0f to entry.tagColor,
                        0.7f to entry.tagColor.copy(alpha = 0.5f),
                        1f to Color.Transparent,
                    ),
                    topLeft = Offset.Zero,
                    size = Size(stripeW, size.height),
                )

                // Glass highlight at top (2dp, horizontal gradient)
                val highlightH = 2.dp.toPx()
                drawRect(
                    brush = Brush.horizontalGradient(
                        0f to ArteriaPalette.GlassHighlight,
                        1f to ArteriaPalette.GlassHighlight.copy(alpha = 0.3f),
                    ),
                    topLeft = Offset.Zero,
                    size = Size(size.width, highlightH),
                )
            },
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Version + tag row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text = "v${entry.version}",
                    style = MaterialTheme.typography.titleLarge,
                    color = ArteriaPalette.TextPrimary,
                )
                TagBadge(label = entry.tag, emoji = entry.tagEmoji, color = entry.tagColor)
                Spacer(Modifier.weight(1f))
                Text(
                    text = entry.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.TextMuted,
                )
            }

            Spacer(Modifier.height(12.dp))

            // Change list
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
private fun TagBadge(label: String, emoji: String, color: Color) {
    val shape = RoundedCornerShape(4.dp)
    Text(
        text = "$emoji $label",
        style = MaterialTheme.typography.labelSmall,
        color = color.copy(alpha = 0.9f),
        modifier = Modifier
            .background(color.copy(alpha = 0.12f), shape)
            .border(0.5.dp, color.copy(alpha = 0.4f), shape)
            .padding(horizontal = 6.dp, vertical = 2.dp),
    )
}
