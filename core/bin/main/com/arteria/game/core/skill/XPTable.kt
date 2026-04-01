package com.arteria.game.core.skill

import kotlin.math.floor
import kotlin.math.pow

/**
 * Classic RS-style XP progression curve.
 * Level 1 = 0 XP, Level 2 = 83 XP, ... Level 99 = 13,034,431 XP.
 */
object XPTable {
    const val MAX_LEVEL = 99

    private val thresholds: LongArray = LongArray(MAX_LEVEL + 1).also { table ->
        table[0] = 0L
        table[1] = 0L
        var accumulator = 0.0
        for (level in 2..MAX_LEVEL) {
            accumulator += floor(
                (level - 1).toDouble() + 300.0 * 2.0.pow((level - 1).toDouble() / 7.0),
            ) / 4.0
            table[level] = accumulator.toLong()
        }
    }

    fun xpForLevel(level: Int): Long = when {
        level <= 1 -> 0L
        level >= MAX_LEVEL -> thresholds[MAX_LEVEL]
        else -> thresholds[level]
    }

    fun levelForXp(xp: Double): Int {
        for (level in MAX_LEVEL downTo 1) {
            if (xp >= thresholds[level]) return level
        }
        return 1
    }

    fun progressToNextLevel(currentXp: Double): Float {
        val level = levelForXp(currentXp)
        if (level >= MAX_LEVEL) return 1f
        val currentThreshold = xpForLevel(level)
        val nextThreshold = xpForLevel(level + 1)
        val range = nextThreshold - currentThreshold
        if (range <= 0) return 1f
        return ((currentXp - currentThreshold) / range).toFloat().coerceIn(0f, 1f)
    }

    fun xpToNextLevel(currentXp: Double): Long {
        val level = levelForXp(currentXp)
        if (level >= MAX_LEVEL) return 0L
        return xpForLevel(level + 1) - currentXp.toLong()
    }
}

