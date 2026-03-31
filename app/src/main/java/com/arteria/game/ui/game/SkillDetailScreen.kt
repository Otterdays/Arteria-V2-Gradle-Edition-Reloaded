package com.arteria.game.ui.game

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arteria.game.core.data.MiningData
import com.arteria.game.core.model.SkillAction
import com.arteria.game.core.model.SkillState
import com.arteria.game.core.skill.SkillId
import com.arteria.game.core.skill.SkillPillar
import com.arteria.game.core.skill.XPTable
import com.arteria.game.ui.theme.ArteriaPalette
import java.text.NumberFormat

private val detailPillarColors = mapOf(
    SkillPillar.GATHERING to ArteriaPalette.BalancedEnd,
    SkillPillar.CRAFTING to ArteriaPalette.Gold,
    SkillPillar.COMBAT to Color(0xFFE74C3C),
    SkillPillar.SUPPORT to ArteriaPalette.LuminarEnd,
)

private fun actionsForSkill(skillId: SkillId): List<SkillAction> = when (skillId) {
    SkillId.MINING -> MiningData.actions
    else -> emptyList()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillDetailScreen(
    skillId: SkillId,
    skillState: SkillState,
    onBack: () -> Unit,
    onStartTraining: (actionId: String) -> Unit,
    onStopTraining: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val level = XPTable.levelForXp(skillState.xp)
    val progress = XPTable.progressToNextLevel(skillState.xp)
    val pillarColor = detailPillarColors[skillId.pillar] ?: ArteriaPalette.AccentPrimary
    val actions = actionsForSkill(skillId)
    val nf = NumberFormat.getIntegerInstance()

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
        TopAppBar(
            title = {
                Text(
                    skillId.displayName,
                    style = MaterialTheme.typography.titleLarge,
                    color = ArteriaPalette.TextPrimary,
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Text(
                        "\u2190",
                        style = MaterialTheme.typography.titleLarge,
                        color = ArteriaPalette.TextPrimary,
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
            ),
        )

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = ArteriaPalette.BgCard.copy(alpha = 0.94f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(1.dp, pillarColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Level $level",
                        style = MaterialTheme.typography.headlineMedium,
                        color = pillarColor,
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = skillId.pillar.displayName,
                        style = MaterialTheme.typography.bodyLarge,
                        color = ArteriaPalette.TextSecondary,
                    )
                }

                Spacer(Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = pillarColor,
                    trackColor = ArteriaPalette.Border,
                )

                Spacer(Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "${nf.format(skillState.xp.toLong())} XP",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ArteriaPalette.TextSecondary,
                    )
                    if (level < XPTable.MAX_LEVEL) {
                        Text(
                            text = "${nf.format(XPTable.xpToNextLevel(skillState.xp))} to next",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ArteriaPalette.TextMuted,
                        )
                    }
                }

                Spacer(Modifier.height(6.dp))

                Text(
                    text = skillId.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.TextMuted,
                )

                if (skillState.isTraining) {
                    Spacer(Modifier.height(8.dp))
                    TrainingIndicator(
                        skillState = skillState,
                        pillarColor = pillarColor,
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (skillState.isTraining) {
            OutlinedButton(
                onClick = onStopTraining,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFE74C3C),
                ),
            ) {
                Text("Stop Training")
            }
            Spacer(Modifier.height(12.dp))
        }

        if (actions.isNotEmpty()) {
            Text(
                text = "AVAILABLE ACTIONS",
                style = MaterialTheme.typography.labelSmall,
                color = ArteriaPalette.Gold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
            )

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(actions, key = { it.id }) { action ->
                    ActionCard(
                        action = action,
                        currentLevel = level,
                        isCurrentAction = skillState.isTraining && skillState.currentActionId == action.id,
                        onTrain = { onStartTraining(action.id) },
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Content coming soon...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = ArteriaPalette.TextMuted,
                )
            }
        }
    }
}

@Composable
private fun TrainingIndicator(
    skillState: SkillState,
    pillarColor: Color,
    modifier: Modifier = Modifier,
) {
    val action = skillState.currentActionId?.let { MiningData.actionRegistry[it] }
    val actionName = action?.name ?: "Unknown"
    val actionTime = action?.actionTimeMs ?: 1L
    val progressFraction = (skillState.actionProgressMs.toFloat() / actionTime).coerceIn(0f, 1f)

    val infiniteTransition = rememberInfiniteTransition(label = "training_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
        ),
        label = "pulse",
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(
            text = "\u2022",
            color = pillarColor.copy(alpha = pulseAlpha),
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = "Training: $actionName",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = pillarColor,
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "${(progressFraction * 100f).toInt()}%",
            style = MaterialTheme.typography.bodySmall,
            color = ArteriaPalette.TextSecondary,
        )
    }
}

@Composable
private fun ActionCard(
    action: SkillAction,
    currentLevel: Int,
    isCurrentAction: Boolean,
    onTrain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val meetsLevel = currentLevel >= action.levelRequired
    val cardShape = RoundedCornerShape(10.dp)
    val nf = NumberFormat.getIntegerInstance()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = when {
                    isCurrentAction -> ArteriaPalette.BalancedEnd.copy(alpha = 0.5f)
                    !meetsLevel -> ArteriaPalette.Border.copy(alpha = 0.3f)
                    else -> ArteriaPalette.Border
                },
                shape = cardShape,
            ),
        shape = cardShape,
        color = when {
            isCurrentAction -> ArteriaPalette.BgCardHover
            !meetsLevel -> ArteriaPalette.BgCard.copy(alpha = 0.5f)
            else -> ArteriaPalette.BgCard
        },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = action.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (meetsLevel) ArteriaPalette.TextPrimary else ArteriaPalette.TextMuted,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Lv ${action.levelRequired}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (meetsLevel) ArteriaPalette.TextSecondary else Color(0xFFE74C3C),
                    )
                    Text(
                        text = "${nf.format(action.xpPerAction.toLong())} XP",
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaPalette.TextSecondary,
                    )
                    Text(
                        text = "${action.actionTimeMs / 1000.0}s",
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaPalette.TextSecondary,
                    )
                }
            }

            if (meetsLevel) {
                Button(
                    onClick = onTrain,
                    enabled = !isCurrentAction,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCurrentAction) ArteriaPalette.BalancedEnd else ArteriaPalette.AccentPrimary,
                        contentColor = Color.White,
                        disabledContainerColor = ArteriaPalette.BalancedEnd.copy(alpha = 0.7f),
                        disabledContentColor = Color.White,
                    ),
                ) {
                    Text(if (isCurrentAction) "Active" else "Train")
                }
            } else {
                Text(
                    text = "Locked",
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.TextMuted,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            }
        }
    }
}

