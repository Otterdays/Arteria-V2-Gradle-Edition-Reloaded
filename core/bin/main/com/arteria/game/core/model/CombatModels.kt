package com.arteria.game.core.model

/** Starter encounter zone (native V2 slice). */
data class EncounterLocation(
    val id: String,
    val name: String,
    val description: String,
    val enemyIds: List<String>,
)

data class EnemyDrop(
    val itemId: String,
    /** Chance per kill, 0..1. */
    val chance: Double,
    val minQty: Int = 1,
    val maxQty: Int = 1,
)

data class EnemyDef(
    val id: String,
    val name: String,
    val maxHp: Int,
    val attack: Int,
    val defence: Int,
    val accuracy: Int,
    val attackIntervalMs: Long,
    /** Total combat XP granted on kill (split across melee skills + hitpoints). */
    val xpPerKill: Double,
    val drops: List<EnemyDrop> = emptyList(),
)

enum class CombatLogType {
    PLAYER_HIT,
    ENEMY_HIT,
    PLAYER_MISS,
    ENEMY_MISS,
    KILL,
    LOOT,
    INFO,
    PLAYER_DEATH,
}

data class CombatLogEntry(
    val type: CombatLogType,
    val message: String,
)

/**
 * Runtime encounter state. Player HP lives only here during a fight.
 * [TRACE: DOCS/ARTERIA-V1-DOCS/DOCU/ARCHITECTURE.md — Combat System Phase 4 sketch]
 */
data class ActiveCombat(
    val locationId: String,
    val enemyId: String,
    val enemyName: String,
    val enemyMaxHp: Int,
    val enemyCurrentHp: Int,
    val enemyAttack: Int,
    val enemyDefence: Int,
    val enemyAccuracy: Int,
    val enemyAttackIntervalMs: Long,
    val enemyAttackAccMs: Long = 0L,
    val playerMaxHp: Int,
    val playerCurrentHp: Int,
    val playerAttackIntervalMs: Long,
    val playerAttackAccMs: Long = 0L,
    /** Snapshot for hit rolls (derived from Attack + gear when encounter started). */
    val playerAccuracy: Int,
    val playerMaxHit: Int,
    val playerMeleeDefence: Int,
    val killCount: Int = 0,
    val xpPerKill: Double,
)
