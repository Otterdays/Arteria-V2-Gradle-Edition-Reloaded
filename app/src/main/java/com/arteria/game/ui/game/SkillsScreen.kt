package com.arteria.game.ui.game

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arteria.game.core.data.SkillDataRegistry
import com.arteria.game.core.model.SkillState
import com.arteria.game.core.skill.SkillId
import com.arteria.game.core.skill.SkillPillar
import com.arteria.game.core.skill.XPTable
import com.arteria.game.ui.theme.ArteriaPalette
import com.arteria.game.ui.theme.LocalReduceMotion
import java.text.NumberFormat

// ─── Pillar colour palette (shared with SkillDetailScreen & HubScreen) ───────

internal val pillarColor = mapOf(
    SkillPillar.GATHERING to ArteriaPalette.BalancedEnd,
    SkillPillar.CRAFTING  to ArteriaPalette.Gold,
    SkillPillar.COMBAT to ArteriaPalette.CombatAccent,
    SkillPillar.SUPPORT   to ArteriaPalette.LuminarEnd,
    SkillPillar.COSMIC    to ArteriaPalette.VoidAccent,
)

// ─── 3c. Crossover relationships (gathering feeds crafting, etc.) ─────────────

internal val skillCrossover: Map<SkillId, List<SkillId>> = mapOf(
    SkillId.MINING     to listOf(SkillId.SMITHING, SkillId.FORGING),
    SkillId.LOGGING    to listOf(SkillId.WOODWORKING, SkillId.FIREMAKING, SkillId.FLETCHING),
    SkillId.FISHING    to listOf(SkillId.COOKING),
    SkillId.HARVESTING to listOf(SkillId.HERBLORE, SkillId.COOKING, SkillId.TAILORING),
    SkillId.SCAVENGING to listOf(SkillId.SMITHING, SkillId.ALCHEMY),
    SkillId.FARMING    to listOf(SkillId.COOKING, SkillId.HERBLORE),
    SkillId.THIEVING   to listOf(SkillId.BARTER),
    SkillId.WOODWORKING to listOf(SkillId.FLETCHING, SkillId.FIREMAKING),
    SkillId.TAILORING  to listOf(SkillId.CONSTRUCTION, SkillId.FORGING),
    SkillId.SMITHING   to listOf(SkillId.FORGING),
    SkillId.RUNECRAFTING to listOf(SkillId.SORCERY),
    SkillId.HERBLORE   to listOf(SkillId.ALCHEMY),
    SkillId.FLETCHING  to listOf(SkillId.RANGED),
)

/** Short names for crossover chips — full [SkillId.displayName] overflows narrow grid cells. */
private fun SkillId.crossoverChipLabel(): String = when (this) {
    SkillId.WOODWORKING -> "Woodwork"
    SkillId.FIREMAKING -> "Fire"
    SkillId.FLETCHING -> "Fletch"
    SkillId.SMITHING -> "Smith"
    SkillId.FORGING -> "Forge"
    SkillId.COOKING -> "Cook"
    SkillId.HERBLORE -> "Herb"
    SkillId.ALCHEMY -> "Alch"
    SkillId.RUNECRAFTING -> "Runes"
    SkillId.SORCERY -> "Sorc"
    SkillId.RANGED -> "Range"
    SkillId.BARTER -> "Barter"
    SkillId.CONSTRUCTION -> "Build"
    else -> displayName
}

// ─── 3f. Sort order ──────────────────────────────────────────────────────────

internal enum class SkillSortOrder(val label: String) {
    DEFAULT("Default"),
    ACTIVE_FIRST("Active"),
    LEVEL_DESC("Level ↓"),
}

// ─── Main screen ─────────────────────────────────────────────────────────────

@Composable
fun SkillsScreen(
    skills: Map<SkillId, SkillState>,
    onSkillClick: (SkillId) -> Unit,
    modifier: Modifier = Modifier,
) {
    // 3f: filter / sort state
    var filterPillar by remember { mutableStateOf<SkillPillar?>(null) }
    var sortOrder    by remember { mutableStateOf(SkillSortOrder.DEFAULT) }
    // 3g: view toggle
    var showStarMap  by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {

        // ── Filter + sort + view-toggle bar (3f / 3g) ────────────────────────
        FilterSortBar(
            filterPillar  = filterPillar,
            onPillarClick = { filterPillar = if (filterPillar == it) null else it },
            sortOrder     = sortOrder,
            onSortCycle   = {
                sortOrder = SkillSortOrder.entries[
                    (sortOrder.ordinal + 1) % SkillSortOrder.entries.size
                ]
            },
            showStarMap    = showStarMap,
            onToggleView   = { showStarMap = !showStarMap },
        )

        // ── Content area ─────────────────────────────────────────────────────
        if (showStarMap) {
            // 3g — constellation view
            SkillStarMap(
                skills       = skills,
                onSkillClick = onSkillClick,
                modifier     = Modifier.fillMaxSize(),
            )
        } else {
            val pillarsToShow = if (filterPillar != null) {
                listOf(filterPillar!!)
            } else {
                SkillPillar.entries
            }

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
            ) {
                val columns = when {
                    maxWidth >= 1400.dp -> 5
                    maxWidth >= 1000.dp -> 4
                    maxWidth >= 700.dp -> 3
                    else -> 2
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .widthIn(max = 1280.dp)
                        .align(Alignment.TopCenter),
                ) {
                    item(span = { GridItemSpan(columns) }) {
                        Text(
                            text = "SKILLS",
                            style = MaterialTheme.typography.labelSmall,
                            color = ArteriaPalette.Gold,
                            modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 2.dp),
                        )
                    }

                    pillarsToShow.forEach { pillar ->
                        val pillarSkills = SkillId.byPillar(pillar)
                        val color = pillarColor[pillar] ?: ArteriaPalette.AccentPrimary
                        val pillarStates = pillarSkills.map { skills[it] ?: SkillState(skillId = it) }

                        // 3e — pillar summary header
                        item(span = { GridItemSpan(columns) }) {
                            PillarSectionHeader(
                                pillar      = pillar,
                                color       = color,
                                pillarStates = pillarStates,
                            )
                        }

                        // 3f — apply sort within pillar
                        val sorted = when (sortOrder) {
                            SkillSortOrder.DEFAULT      -> pillarSkills
                            SkillSortOrder.ACTIVE_FIRST -> pillarSkills
                                .sortedByDescending { skills[it]?.isTraining == true }
                            SkillSortOrder.LEVEL_DESC   -> pillarSkills
                                .sortedByDescending { skills[it]?.xp ?: 0.0 }
                        }

                        items(sorted, key = { it.name }) { skillId ->
                            val state = skills[skillId] ?: SkillState(skillId = skillId)
                            val implemented = remember(skillId) {
                                SkillDataRegistry.isSkillImplemented(skillId)
                            }
                            SkillCard(
                                skillId = skillId,
                                state = state,
                                accentColor = color,
                                implemented = implemented,
                                crossoverTargets = skillCrossover[skillId] ?: emptyList(),
                                onClick = { onSkillClick(skillId) },
                            )
                        }
                    }

                    item(span = { GridItemSpan(columns) }) { Spacer(Modifier.height(12.dp)) }
                }
            }
        }
    }
}

// ─── 3f. Filter / sort bar ───────────────────────────────────────────────────

@Composable
private fun FilterSortBar(
    filterPillar: SkillPillar?,
    onPillarClick: (SkillPillar) -> Unit,
    sortOrder: SkillSortOrder,
    onSortCycle: () -> Unit,
    showStarMap: Boolean,
    onToggleView: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Pillar filter chips (horizontally scrollable)
        Row(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            SkillPillar.entries.forEach { pillar ->
                val selected = filterPillar == pillar
                val color = pillarColor[pillar] ?: ArteriaPalette.AccentPrimary
                FilterChip(
                    label    = pillar.displayName.take(4).uppercase(),
                    color    = color,
                    selected = selected,
                    onClick  = { onPillarClick(pillar) },
                )
            }
        }

        Spacer(Modifier.width(8.dp))

        // Sort cycle button
        FilterChip(
            label    = sortOrder.label,
            color    = ArteriaPalette.AccentPrimary,
            selected = sortOrder != SkillSortOrder.DEFAULT,
            onClick  = onSortCycle,
        )

        Spacer(Modifier.width(6.dp))

        // Star map toggle
        FilterChip(
            label    = if (showStarMap) "List" else "Map",
            color    = ArteriaPalette.AccentWeb,
            selected = showStarMap,
            onClick  = onToggleView,
        )
    }
}

@Composable
private fun FilterChip(
    label: String,
    color: Color,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bg by animateColorAsState(
        targetValue = if (selected) color.copy(alpha = 0.20f) else ArteriaPalette.BgCard,
        animationSpec = tween(200),
        label = "chip_bg",
    )
    val border by animateColorAsState(
        targetValue = if (selected) color else ArteriaPalette.Border,
        animationSpec = tween(200),
        label = "chip_border",
    )

    Surface(
        shape  = RoundedCornerShape(20.dp),
        color  = bg,
        modifier = modifier
            .border(1.dp, border, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
    ) {
        Text(
            text  = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) color else ArteriaPalette.TextMuted,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
        )
    }
}

// ─── 3e. Pillar Section Header ───────────────────────────────────────────────

@Composable
private fun PillarSectionHeader(
    pillar: SkillPillar,
    color: Color,
    pillarStates: List<SkillState>,
    modifier: Modifier = Modifier,
) {
    val liveCount  = pillarStates.count { SkillDataRegistry.isSkillImplemented(it.skillId) }
    val totalCount = pillarStates.size
    val withXp     = pillarStates.filter { it.xp > 0 }
    val avgLevel   = if (withXp.isNotEmpty()) {
        withXp.sumOf { XPTable.levelForXp(it.xp) } / withXp.size
    } else 1
    val activeCount = pillarStates.count { it.isTraining }

    val summary = buildString {
        append("$liveCount/$totalCount live")
        if (withXp.isNotEmpty()) append(" · avg Lv $avgLevel")
        if (activeCount > 0) append(" · $activeCount active")
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 14.dp, bottom = 4.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp),
        ) {
            // Diamond glyph
            Text(
                text = "◆",
                color = color.copy(alpha = 0.6f),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(end = 6.dp),
            )
            Text(
                text = pillar.displayName.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = color,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = summary,
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaPalette.TextMuted,
            )
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = color.copy(alpha = 0.18f),
        )
    }
}

// ─── Skill Card ───────────────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SkillCard(
    skillId: SkillId,
    state: SkillState,
    accentColor: Color,
    implemented: Boolean,
    crossoverTargets: List<SkillId>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val reduceMotion = LocalReduceMotion.current
    val cardColor = accentColor
    val level    = XPTable.levelForXp(state.xp)
    val progress = XPTable.progressToNextLevel(state.xp)
    val cardShape = RoundedCornerShape(12.dp)

    val borderColor by animateColorAsState(
        targetValue = when {
            state.isTraining -> cardColor
            !implemented     -> ArteriaPalette.Border.copy(alpha = 0.4f)
            else             -> ArteriaPalette.Border
        },
        animationSpec = tween(350),
        label = "card_border_${skillId.name}",
    )
    val cardAlpha = if (!implemented) 0.5f else 1f

    // Press-scale spring
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 800f),
        label = "card_press",
    )

    // Training shimmer (4s sweep)
    val shimmerOffset = if (state.isTraining && !reduceMotion) {
        val inf = rememberInfiniteTransition(label = "shimmer_${skillId.name}")
        inf.animateFloat(
            initialValue = -0.3f,
            targetValue = 1.3f,
            animationSpec = infiniteRepeatable(
                tween(4000, easing = LinearEasing),
                RepeatMode.Restart,
            ),
            label = "shimmer_f",
        ).value
    } else -1f

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(cardShape)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        tryAwaitRelease()
                        pressed = false
                    },
                    onTap = { onClick() },
                )
            }
            .border(1.dp, borderColor, cardShape),
        shape = cardShape,
        color = when {
            state.isTraining -> ArteriaPalette.BgCardHover
            !implemented     -> ArteriaPalette.BgCard.copy(alpha = 0.6f)
            else             -> ArteriaPalette.BgCard
        },
    ) {
        Box {
            // Glassmorphism top highlight
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(
                        Brush.horizontalGradient(
                            0f to Color.Transparent,
                            0.3f to cardColor.copy(alpha = 0.10f),
                            0.7f to cardColor.copy(alpha = 0.10f),
                            1f to Color.Transparent,
                        ),
                    ),
            )

            // Shimmer sweep overlay for training cards
            if (shimmerOffset > -0.5f) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.horizontalGradient(
                                (shimmerOffset - 0.15f).coerceIn(0f, 1f) to Color.Transparent,
                                shimmerOffset.coerceIn(0f, 1f) to ArteriaPalette.GlassHighlight,
                                (shimmerOffset + 0.15f).coerceIn(0f, 1f) to Color.Transparent,
                            ),
                        ),
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    verticalAlignment   = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text  = skillId.displayName,
                            style = MaterialTheme.typography.titleMedium,
                            color = ArteriaPalette.TextPrimary.copy(alpha = cardAlpha),
                            maxLines = 1,
                        )
                        when {
                            state.isTraining -> Text(
                                text  = "TRAINING",
                                style = MaterialTheme.typography.labelSmall
                                    .copy(fontWeight = FontWeight.Bold),
                                color = cardColor,
                            )
                            !implemented -> Text(
                                text  = "SOON",
                                style = MaterialTheme.typography.labelSmall,
                                color = ArteriaPalette.TextMuted,
                            )
                            else -> Text(
                                text  = skillId.pillar.displayName,
                                style = MaterialTheme.typography.bodySmall,
                                color = cardColor.copy(alpha = 0.7f),
                            )
                        }
                    }

                    LevelBadge(
                        level       = level,
                        progress    = progress,
                        color       = cardColor.copy(alpha = cardAlpha),
                        implemented = implemented,
                    )
                }

                Spacer(Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress   = { progress },
                    modifier   = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(1.5.dp)),
                    color      = cardColor.copy(alpha = cardAlpha),
                    trackColor = ArteriaPalette.Border.copy(alpha = 0.4f),
                )

                if (state.xp > 0.0) {
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text  = "${NumberFormat.getIntegerInstance().format(state.xp.toLong())} XP",
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaPalette.TextMuted.copy(alpha = cardAlpha),
                    )
                }

                // 3c — crossover "feeds into" tags (FlowRow: narrow cells break a single Row)
                if (crossoverTargets.isNotEmpty()) {
                    Spacer(Modifier.height(5.dp))
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 22.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        crossoverTargets.take(3).forEach { target ->
                            val tColor = pillarColor[target.pillar] ?: ArteriaPalette.AccentPrimary
                            Text(
                                text = "→ ${target.crossoverChipLabel()}",
                                style = MaterialTheme.typography.labelSmall
                                    .copy(fontSize = 9.sp),
                                color = tColor.copy(alpha = 0.65f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                softWrap = false,
                                modifier = Modifier
                                    .background(
                                        tColor.copy(alpha = 0.08f),
                                        RoundedCornerShape(4.dp),
                                    )
                                    .padding(horizontal = 4.dp, vertical = 2.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─── Level Badge with XP Arc ─────────────────────────────────────────────────

@Composable
private fun LevelBadge(
    level: Int,
    progress: Float,
    color: Color,
    implemented: Boolean,
    modifier: Modifier = Modifier,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = if (implemented) progress.coerceIn(0f, 1f) else 0f,
        animationSpec = tween(600),
        label = "badge_arc",
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(44.dp)
            // Glow halo behind the arc when progress > 0
            .drawBehind {
                if (animatedProgress > 0.05f && implemented) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                color.copy(alpha = 0.12f * animatedProgress),
                                Color.Transparent,
                            ),
                            center = center,
                            radius = size.minDimension * 0.7f,
                        ),
                    )
                }
            },
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val stroke = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
            drawArc(
                color = color.copy(alpha = 0.15f),
                startAngle = -90f, sweepAngle = 360f,
                useCenter = false, style = stroke,
            )
            if (animatedProgress > 0f) {
                drawArc(
                    color = color.copy(alpha = 0.75f),
                    startAngle = -90f, sweepAngle = 360f * animatedProgress,
                    useCenter = false, style = stroke,
                )
            }
        }
        Text(
            text  = if (implemented) "$level" else "—",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize   = 15.sp,
                textAlign  = TextAlign.Center,
            ),
            color = color,
        )
    }
}
