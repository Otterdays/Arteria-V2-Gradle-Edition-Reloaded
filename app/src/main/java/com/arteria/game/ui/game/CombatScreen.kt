package com.arteria.game.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arteria.game.core.data.EncounterData
import com.arteria.game.core.model.ActiveCombat
import com.arteria.game.core.model.CombatLogEntry
import com.arteria.game.core.model.CombatLogType
import com.arteria.game.core.model.GameState
import com.arteria.game.core.skill.SkillId
import com.arteria.game.core.skill.XPTable
import com.arteria.game.ui.theme.ArteriaPalette

// [TRACE: DOCS/ARTERIA-V1-DOCS/DOCU/ARCHITECTURE.md — Combat System Phase 4 sketch]

@Composable
fun CombatScreen(
    gameState: GameState,
    onStartEncounter: (locationId: String, enemyId: String) -> Unit,
    onFlee: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val combat = gameState.activeCombat
    val loc = EncounterData.location(EncounterData.LOCATION_SUNNY_MEADOW_BARN)
    val rat = EncounterData.enemy(EncounterData.ENEMY_BARN_RAT)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ArteriaPalette.BgApp)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Encounter",
            style = MaterialTheme.typography.headlineMedium,
            color = ArteriaPalette.CombatAccent,
        )
        CombatSkillStrip(gameState = gameState)

        if (combat == null && loc != null && rat != null) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = ArteriaPalette.BgCard,
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(loc.name, style = MaterialTheme.typography.titleLarge, color = ArteriaPalette.Gold)
                    Text(
                        loc.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = ArteriaPalette.TextSecondary,
                    )
                    Text(
                        "Foe: ${rat.name} — HP ${rat.maxHp}, ATK ${rat.attack}, DEF ${rat.defence}",
                        style = MaterialTheme.typography.bodySmall,
                        color = ArteriaPalette.TextMuted,
                    )
                    Button(
                        onClick = {
                            onStartEncounter(
                                EncounterData.LOCATION_SUNNY_MEADOW_BARN,
                                EncounterData.ENEMY_BARN_RAT,
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = ArteriaPalette.CombatAccent),
                    ) {
                        Text("Engage", color = ArteriaPalette.TextPrimary)
                    }
                }
            }
        } else if (combat != null) {
            ActiveCombatPanel(combat = combat, onFlee = onFlee, log = gameState.combatLog)
        }
    }
}

@Composable
private fun CombatSkillStrip(gameState: GameState, modifier: Modifier = Modifier) {
    val s = gameState.skills
    fun lv(id: SkillId) = XPTable.levelForXp(s[id]?.xp ?: 0.0)
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text(
            "ATK ${lv(SkillId.ATTACK)}",
            style = MaterialTheme.typography.labelSmall,
            color = ArteriaPalette.Gold,
        )
        Text(
            "STR ${lv(SkillId.STRENGTH)}",
            style = MaterialTheme.typography.labelSmall,
            color = ArteriaPalette.CombatAccent,
        )
        Text(
            "DEF ${lv(SkillId.DEFENCE)}",
            style = MaterialTheme.typography.labelSmall,
            color = ArteriaPalette.TextSecondary,
        )
        Text(
            "HP ${lv(SkillId.HITPOINTS)}",
            style = MaterialTheme.typography.labelSmall,
            color = ArteriaPalette.BalancedEnd,
        )
    }
}

@Composable
private fun ActiveCombatPanel(
    combat: ActiveCombat,
    onFlee: () -> Unit,
    log: List<CombatLogEntry>,
    modifier: Modifier = Modifier,
) {
    val def = EncounterData.enemy(combat.enemyId)
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = combat.enemyName,
                style = MaterialTheme.typography.titleLarge,
                color = ArteriaPalette.CombatAccent,
            )
            OutlinedButton(onClick = onFlee) {
                Text("Flee", color = ArteriaPalette.TextSecondary)
            }
        }
        if (def != null) {
            Text(
                "Tier 1 — ${def.name}",
                style = MaterialTheme.typography.bodySmall,
                color = ArteriaPalette.TextMuted,
            )
        }
        Text(
            "Kills: ${combat.killCount}",
            style = MaterialTheme.typography.bodyMedium,
            color = ArteriaPalette.Gold,
        )

        Text("Your health", style = MaterialTheme.typography.labelSmall, color = ArteriaPalette.TextMuted)
        val pFrac =
            (combat.playerCurrentHp.toFloat() / combat.playerMaxHp.toFloat().coerceAtLeast(1f)).coerceIn(0f, 1f)
        LinearProgressIndicator(
            progress = { pFrac },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = ArteriaPalette.BalancedEnd,
            trackColor = ArteriaPalette.BgInput,
        )
        Text(
            "${combat.playerCurrentHp} / ${combat.playerMaxHp}",
            style = MaterialTheme.typography.bodySmall,
            color = ArteriaPalette.TextSecondary,
        )

        Text("${combat.enemyName} health", style = MaterialTheme.typography.labelSmall, color = ArteriaPalette.TextMuted)
        val eFrac =
            (combat.enemyCurrentHp.toFloat() / combat.enemyMaxHp.toFloat().coerceAtLeast(1f)).coerceIn(0f, 1f)
        LinearProgressIndicator(
            progress = { eFrac },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = ArteriaPalette.CombatAccent,
            trackColor = ArteriaPalette.BgInput,
        )
        Text(
            "${combat.enemyCurrentHp} / ${combat.enemyMaxHp}",
            style = MaterialTheme.typography.bodySmall,
            color = ArteriaPalette.TextSecondary,
        )

        Text("Combat log", style = MaterialTheme.typography.titleSmall, color = ArteriaPalette.TextPrimary)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            shape = RoundedCornerShape(8.dp),
            color = ArteriaPalette.BgCard.copy(alpha = 0.92f),
        ) {
            LazyColumn(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(log.size) { idx ->
                    val entry = log[log.size - 1 - idx]
                    Text(
                        text = entry.message,
                        style = MaterialTheme.typography.bodySmall,
                        color = logColor(entry.type),
                    )
                }
            }
        }
        Spacer(Modifier.height(4.dp))
    }
}

private fun logColor(type: CombatLogType): Color = when (type) {
    CombatLogType.PLAYER_HIT -> ArteriaPalette.BalancedEnd
    CombatLogType.ENEMY_HIT -> ArteriaPalette.CombatAccent
    CombatLogType.KILL -> ArteriaPalette.Gold
    CombatLogType.LOOT -> ArteriaPalette.GoldDim
    CombatLogType.PLAYER_DEATH -> ArteriaPalette.CombatAccent
    else -> ArteriaPalette.TextMuted
}
