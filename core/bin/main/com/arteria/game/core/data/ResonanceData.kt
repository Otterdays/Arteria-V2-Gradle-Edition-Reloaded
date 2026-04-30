package com.arteria.game.core.data

import com.arteria.game.core.model.GameState
import com.arteria.game.core.skill.SkillId
import java.util.Locale
import kotlin.math.roundToInt

/**
 * Resonance clicker tuning aligned with `DOCS/ARTERIA-V1-DOCS/DOCU/CLICKER_DESIGN.md`.
 */
object ResonanceData {
    const val MOMENTUM_CAP: Double = 100.0
    /** Design §4.2: 2% of bar per second. */
    const val MOMENTUM_DECAY_PER_SECOND: Double = 2.0
    /** Design §4.2: `1 + (momentum/100) * 0.5` at full bar ⇒ 1.5×. */
    fun hasteMultiplier(momentum: Double): Double {
        val m = momentum.coerceIn(0.0, MOMENTUM_CAP)
        return 1.0 + (m / MOMENTUM_CAP) * 0.5
    }

    const val UNLOCK_MULTI_PULSE: Int = 20
    const val UNLOCK_RESONANT_ECHO: Int = 40
    const val UNLOCK_SOUL_CRANKING: Int = 60
    const val UNLOCK_KINETIC_FEEDBACK: Int = 80
    const val UNLOCK_PERFECT_STABILITY: Int = 99

    const val KINETIC_FEEDBACK_CHANCE: Double = 0.10
    /** +1% momentum per successful kinetic proc (design §4.2). */
    const val KINETIC_MOMENTUM_BONUS: Double = 1.0

    const val ANCHOR_ENERGY_CAP: Int = 50
    const val ANCHOR_MS_PER_POINT: Long = 60_000L
    const val HEAVY_PULSE_ENERGY_COST: Int = 5
    const val HEAVY_PULSE_MOMENTUM: Double = 20.0
    const val HEAVY_PULSE_XP: Double = 40.0

    /**
     * Taps farther apart than this break the Rhythm chain (QoL pacing similar to Cookie-style combos).
     */
    const val FLOW_CHAIN_GAP_MS: Long = 900L
    /** Max chain depth counted for bonus — keeps tap-spam humane. */
    const val FLOW_CHAIN_CAP: Int = 14
    /** Per chained tap before this pulse, additive XP/Momentum multiplier (cap via [FLOW_CHAIN_CAP]). */
    const val FLOW_BONUS_PER_PRIOR_TAP: Double = 0.017

    /**
     * `priorChain` = rhythm streak **before** this tap (number of taps since last chain break − 1 semantics:
     * first tap uses 0).
     */
    fun flowMultiplier(priorChain: Int): Double {
        val c = priorChain.coerceIn(0, FLOW_CHAIN_CAP)
        return 1.0 + FLOW_BONUS_PER_PRIOR_TAP * c
    }

    fun baseTapHintsLine(resonanceLevel: Int): String {
        val xp = resonanceXpPerTap(resonanceLevel)
        val mom = momentumPerTap(resonanceLevel)
        return String.format(
            Locale.US,
            "Base pulse ~+%s XP · +%s momentum — Rhythm boosts chained taps (<%ds). ",
            formatHint(xp),
            formatMom(mom),
            FLOW_CHAIN_GAP_MS / 1000L,
        ) + "Long-press Heavy when Soul Cranking is live."
    }

    private fun formatHint(x: Double): String =
        if (x >= 100) x.roundToInt().toString() else String.format(Locale.US, "%.1f", x)

    private fun formatMom(x: Double): String = String.format(Locale.US, "%.1f", x)

    private const val BASE_XP_PER_TAP: Double = 12.0
    private const val BASE_MOMENTUM_PER_TAP: Double = 4.0

    /** Proxy for future multi-touch: small tap efficiency bump at Lv 20+. */
    private fun multiPulseMultiplier(level: Int): Double =
        if (level >= UNLOCK_MULTI_PULSE) 1.15 else 1.0

    private fun echoMultiplier(level: Int): Double =
        if (level >= UNLOCK_RESONANT_ECHO) 1.5 else 1.0

    fun resonanceXpPerTap(resonanceLevel: Int): Double =
        BASE_XP_PER_TAP * echoMultiplier(resonanceLevel) * multiPulseMultiplier(resonanceLevel)

    fun momentumPerTap(resonanceLevel: Int): Double =
        BASE_MOMENTUM_PER_TAP * echoMultiplier(resonanceLevel) * multiPulseMultiplier(resonanceLevel)

    const val PERFECT_STABILITY_FLOOR: Double = 25.0

    fun applyMomentumDecay(momentum: Double, deltaMs: Long, resonanceLevel: Int): Double {
        val decay = (deltaMs / 1000.0) * MOMENTUM_DECAY_PER_SECOND
        var next = momentum - decay
        if (resonanceLevel >= UNLOCK_PERFECT_STABILITY) {
            next = next.coerceAtLeast(PERFECT_STABILITY_FLOOR)
        }
        return next.coerceIn(0.0, MOMENTUM_CAP)
    }

    fun canHeavyPulse(resonanceLevel: Int, anchorEnergy: Int): Boolean =
        resonanceLevel >= UNLOCK_SOUL_CRANKING && anchorEnergy >= HEAVY_PULSE_ENERGY_COST

    /**
     * While any non-Resonance skill is actively training, accrue ms toward +1 anchor / minute (capped).
     */
    fun accrueAnchorEnergy(state: GameState, deltaMs: Long): GameState {
        val trainingOther = state.skills.any { (id, s) ->
            id != SkillId.RESONANCE && s.isTraining && s.currentActionId != null
        }
        if (!trainingOther) return state

        var acc = state.anchorEnergyAccMs + deltaMs
        var energy = state.anchorEnergy
        while (acc >= ANCHOR_MS_PER_POINT && energy < ANCHOR_ENERGY_CAP) {
            acc -= ANCHOR_MS_PER_POINT
            energy += 1
        }
        return state.copy(
            anchorEnergy = energy.coerceAtMost(ANCHOR_ENERGY_CAP),
            anchorEnergyAccMs = acc,
        )
    }

    data class UnlockRow(
        val level: Int,
        val id: String,
        val label: String,
        val effect: String,
    )

    val unlockRows: List<UnlockRow> = listOf(
        UnlockRow(1, "flow_rhythm", "Flow Rhythm", "Keep pulses under ~1s apart to stack Rhythm — bigger XP + momentum."),
        UnlockRow(UNLOCK_MULTI_PULSE, "multi_pulse", "Multi-Pulse", "Faster pulses — bonus tap efficiency."),
        UnlockRow(UNLOCK_RESONANT_ECHO, "echo", "Resonant Echo", "+50% Resonance XP and Momentum per tap."),
        UnlockRow(
            UNLOCK_SOUL_CRANKING,
            "soul_cranking",
            "Soul Cranking",
            "Heavy Pulse: spend Anchor Energy for a surge of Momentum + XP.",
        ),
        UnlockRow(
            UNLOCK_KINETIC_FEEDBACK,
            "kinetic",
            "Kinetic Feedback",
            "Other skills can randomly grant +1% Momentum on action completes.",
        ),
        UnlockRow(
            UNLOCK_PERFECT_STABILITY,
            "stability",
            "Perfect Stability",
            "Momentum never decays below 25% (permanent partial haste).",
        ),
    )
}
