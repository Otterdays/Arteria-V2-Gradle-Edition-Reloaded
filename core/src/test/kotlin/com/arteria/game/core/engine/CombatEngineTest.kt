package com.arteria.game.core.engine

import com.arteria.game.core.data.EncounterData
import com.arteria.game.core.model.GameState
import com.arteria.game.core.model.SkillState
import com.arteria.game.core.skill.SkillId
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CombatEngineTest {

    @Test
    fun startCombat_stopsTraining_andSetsActiveCombat() {
        val initial = minimalState().copy(
            skills = mapOf(
                SkillId.MINING to SkillState(
                    skillId = SkillId.MINING,
                    isTraining = true,
                    currentActionId = "mine_copper",
                    actionProgressMs = 100L,
                ),
            ),
        )
        val started = CombatEngine.startCombat(
            initial,
            EncounterData.LOCATION_SUNNY_MEADOW_BARN,
            EncounterData.ENEMY_BARN_RAT,
        )
        assertNotNull(started)
        assertEquals(false, started!!.skills[SkillId.MINING]!!.isTraining)
        assertNotNull(started.activeCombat)
        assertEquals(EncounterData.ENEMY_BARN_RAT, started.activeCombat!!.enemyId)
    }

    @Test
    fun processCombatTick_reducesEnemyHp_orCompletes() {
        val rng = Random(42)
        val started = CombatEngine.startCombat(
            minimalState(),
            EncounterData.LOCATION_SUNNY_MEADOW_BARN,
            EncounterData.ENEMY_BARN_RAT,
        )!!
        val after = CombatEngine.processCombatTick(started, 2_500L, rng)
        assertTrue(
            after.state.activeCombat!!.enemyCurrentHp < started.activeCombat!!.enemyCurrentHp ||
                after.xpGained.isNotEmpty(),
        )
    }

    private fun minimalState(): GameState {
        val skills = SkillId.entries.associateWith { SkillState(skillId = it) }
        return GameState(
            profileId = "p1",
            skills = skills,
            bank = emptyMap(),
            lastSaveTimestamp = 0L,
        )
    }
}
