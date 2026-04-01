package com.arteria.game.core.engine

import com.arteria.game.core.data.MiningData
import com.arteria.game.core.model.GameState
import com.arteria.game.core.model.SkillState
import com.arteria.game.core.skill.SkillId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TickEngineTest {
    @Test
    fun processTick_grantsXpAndResource_forActiveMiningAction() {
        val initial = GameState(
            profileId = "p1",
            skills = mapOf(
                SkillId.MINING to SkillState(
                    skillId = SkillId.MINING,
                    xp = 0.0,
                    isTraining = true,
                    currentActionId = "mine_copper",
                    actionProgressMs = 0L,
                ),
            ),
            bank = emptyMap(),
            lastSaveTimestamp = 0L,
        )

        val result = TickEngine.processTick(
            state = initial,
            deltaMs = 2400L,
            actionRegistry = MiningData.actionRegistry,
        )

        val mining = result.state.skills.getValue(SkillId.MINING)
        assertEquals(17.5, mining.xp, 0.001)
        assertEquals(1, result.state.bank["copper_ore"])
        assertEquals(17.5, result.xpGained.getValue(SkillId.MINING), 0.001)
    }

    @Test
    fun processOffline_capsDuration_and_accumulatesProgress() {
        val initial = GameState(
            profileId = "p1",
            skills = mapOf(
                SkillId.MINING to SkillState(
                    skillId = SkillId.MINING,
                    xp = 0.0,
                    isTraining = true,
                    currentActionId = "mine_copper",
                    actionProgressMs = 0L,
                ),
            ),
            bank = emptyMap(),
            lastSaveTimestamp = 0L,
        )

        val result = TickEngine.processOffline(
            state = initial,
            elapsedMs = 2 * 24 * 60 * 60 * 1000L,
            tickIntervalMs = 600L,
            actionRegistry = MiningData.actionRegistry,
            maxOfflineMs = 1000L,
        )

        assertTrue(result.resourcesGained.getOrDefault("copper_ore", 0) >= 0)
        assertTrue(result.state.skills.getValue(SkillId.MINING).xp >= 0.0)
    }
}
