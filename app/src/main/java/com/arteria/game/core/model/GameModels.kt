package com.arteria.game.core.model

import com.arteria.game.core.skill.SkillId

data class SkillAction(
    val id: String,
    val skillId: SkillId,
    val name: String,
    val levelRequired: Int,
    val xpPerAction: Double,
    val actionTimeMs: Long,
    val resourceId: String? = null,
    val resourceAmount: Int = 1,
    /** Items consumed from the bank per action completion. Empty = pure gathering. */
    val inputItems: Map<String, Int> = emptyMap(),
)

data class ItemDef(
    val id: String,
    val name: String,
    val description: String,
    val stackable: Boolean = true,
)

data class SkillState(
    val skillId: SkillId,
    val xp: Double = 0.0,
    val isTraining: Boolean = false,
    val currentActionId: String? = null,
    val actionProgressMs: Long = 0L,
)

data class GameState(
    val profileId: String,
    val skills: Map<SkillId, SkillState>,
    val bank: Map<String, Int>,
    val lastSaveTimestamp: Long,
    /** Wall-clock ms when offline catch-up was last applied on session load (persisted in `game_meta`). */
    val lastOfflineTickAppliedAt: Long = 0L,
)

data class LevelUp(
    val skillId: SkillId,
    val oldLevel: Int,
    val newLevel: Int,
)

data class TickResult(
    val state: GameState,
    val xpGained: Map<SkillId, Double>,
    val resourcesGained: Map<String, Int>,
    val levelUps: List<LevelUp>,
)

