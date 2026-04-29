package com.arteria.game.core.model

import com.arteria.game.core.skill.SkillId

// EquippedGear is defined in Equipment.kt (same package)

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
    /** Currently equipped gear slots (persisted). */
    val equippedGear: EquippedGear = EquippedGear(),
    /** Active companion id, or null if none summoned (persisted). */
    val activeCompanionId: String? = null,
    /** Resonance clicker: global haste driver, 0–100 (persisted). */
    val momentum: Double = 0.0,
    /** Soul Cranking fuel; earned from non-Resonance training, capped (persisted). */
    val anchorEnergy: Int = 0,
    /** Ms accumulated toward the next +1 anchor energy while training other skills (persisted). */
    val anchorEnergyAccMs: Long = 0L,
    /** Lifetime normal pulses (persisted). */
    val totalResonancePulses: Long = 0L,
    /** Lifetime heavy pulses (persisted). */
    val totalHeavyPulses: Long = 0L,
    /** High-water mark for momentum (achievements / UI) (persisted). */
    val peakMomentum: Double = 0.0,
    /** Auto-encounter combat session, or null when not fighting (persisted in `game_meta`). */
    val activeCombat: ActiveCombat? = null,
    /** Recent combat log lines (persisted; capped in `CombatEngine`). */
    val combatLog: List<CombatLogEntry> = emptyList(),
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
    /** Foreground-only: momentum from Kinetic Feedback (Lv 80+) on other skills' action completes. */
    val kineticMomentumDelta: Double = 0.0,
)

