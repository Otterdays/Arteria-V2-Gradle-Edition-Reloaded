package com.arteria.game.core.model

/**
 * One-shot UI payload after a Resonance orb interaction (normal tap or Heavy surge).
 */
data class ResonancePulseOutcome(
    val xpAdded: Double,
    val momentumAdded: Double,
    /** Rhythm depth after this pulse (1 = first tap in a fresh chain). */
    val rhythmDepthAfter: Int,
    /** Rhythm multiplier percent applied to this pulse from prior chaining (0–24%+). */
    val rhythmBonusPercentApplied: Int,
    val heavySurge: Boolean,
    /** Stable key for Compose transitions / floating numerals. */
    val pulseId: Long,
)
