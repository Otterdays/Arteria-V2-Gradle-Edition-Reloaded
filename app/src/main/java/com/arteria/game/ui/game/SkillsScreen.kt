package com.arteria.game.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arteria.game.core.model.SkillState
import com.arteria.game.core.skill.SkillId
import com.arteria.game.core.skill.SkillPillar
import com.arteria.game.core.skill.XPTable
import com.arteria.game.ui.theme.ArteriaPalette
import java.text.NumberFormat

private val pillarColors = mapOf(
    SkillPillar.GATHERING to ArteriaPalette.BalancedEnd,
    SkillPillar.CRAFTING to ArteriaPalette.Gold,
    SkillPillar.COMBAT to Color(0xFFE74C3C),
    SkillPillar.SUPPORT to ArteriaPalette.LuminarEnd,
)

@Composable
fun SkillsScreen(
    skills: Map<SkillId, SkillState>,
    onSkillClick: (SkillId) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Text(
            text = "SKILLS",
            style = MaterialTheme.typography.labelSmall,
            color = ArteriaPalette.Gold,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp),
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(SkillId.entries, key = { it.name }) { skillId ->
                val state = skills[skillId] ?: SkillState(skillId = skillId)
                SkillCard(
                    skillId = skillId,
                    state = state,
                    onClick = { onSkillClick(skillId) },
                )
            }
        }
    }
}

@Composable
private fun SkillCard(
    skillId: SkillId,
    state: SkillState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val level = XPTable.levelForXp(state.xp)
    val progress = XPTable.progressToNextLevel(state.xp)
    val pillarColor = pillarColors[skillId.pillar] ?: ArteriaPalette.AccentPrimary
    val cardShape = RoundedCornerShape(12.dp)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = if (state.isTraining) pillarColor.copy(alpha = 0.7f) else ArteriaPalette.Border,
                shape = cardShape,
            ),
        shape = cardShape,
        color = if (state.isTraining) ArteriaPalette.BgCardHover else ArteriaPalette.BgCard,
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = skillId.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        color = ArteriaPalette.TextPrimary,
                    )
                    Text(
                        text = skillId.pillar.displayName,
                        style = MaterialTheme.typography.bodySmall,
                        color = pillarColor,
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = pillarColor.copy(alpha = 0.15f),
                            shape = CircleShape,
                        )
                        .border(1.dp, pillarColor.copy(alpha = 0.4f), CircleShape),
                ) {
                    Text(
                        text = "$level",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        ),
                        color = pillarColor,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = pillarColor,
                trackColor = ArteriaPalette.Border,
            )

            Spacer(Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                val nf = NumberFormat.getIntegerInstance()
                Text(
                    text = "${nf.format(state.xp.toLong())} XP",
                    style = MaterialTheme.typography.bodySmall,
                    color = ArteriaPalette.TextMuted,
                )
                if (state.isTraining) {
                    Text(
                        text = "TRAINING",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = pillarColor,
                    )
                }
            }
        }
    }
}

