package com.arteria.game.core.skill

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class XPTableTest {

    @Test
    fun xpForLevel_matchesDocumentedClassicCurve() {
        assertEquals(0L, XPTable.xpForLevel(1))
        assertEquals(83L, XPTable.xpForLevel(2))
    }

    @Test
    fun levelForXp_atThresholds() {
        assertEquals(1, XPTable.levelForXp(0.0))
        assertEquals(2, XPTable.levelForXp(83.0))
        assertEquals(99, XPTable.levelForXp(XPTable.xpForLevel(99).toDouble()))
    }

    @Test
    fun levelForXp_betweenThresholds_staysOnLowerLevelUntilNext() {
        assertEquals(2, XPTable.levelForXp(83.0))
        val startLevel3 = XPTable.xpForLevel(3).toDouble()
        assertEquals(2, XPTable.levelForXp(startLevel3 - 1.0))
    }

    @Test
    fun levelXp_roundTrip_forLevelsInRange() {
        for (level in 2..98) {
            val xpAt = XPTable.xpForLevel(level).toDouble()
            assertEquals(level, XPTable.levelForXp(xpAt))
            assertEquals(level, XPTable.levelForXp(xpAt + 0.5))
        }
    }

    @Test
    fun progressToNextLevel_monotonicAndClamped() {
        val t1 = XPTable.xpForLevel(1).toDouble()
        val t2 = XPTable.xpForLevel(2).toDouble()
        val mid = (t1 + t2) / 2.0
        val pLow = XPTable.progressToNextLevel(t1)
        val pMid = XPTable.progressToNextLevel(mid)
        val pHigh = XPTable.progressToNextLevel(t2 - 1.0)
        assertTrue(pLow <= pMid)
        assertTrue(pMid <= pHigh)
        assertTrue(pLow in 0f..1f)
        assertTrue(pMid in 0f..1f)
        assertTrue(pHigh in 0f..1f)
    }

    @Test
    fun progressToNextLevel_atMaxLevel_isOne() {
        val maxXp = XPTable.xpForLevel(XPTable.MAX_LEVEL).toDouble()
        assertEquals(1f, XPTable.progressToNextLevel(maxXp), 0f)
    }

    @Test
    fun xpToNextLevel_nonNegativeUntilMax() {
        val mid = (XPTable.xpForLevel(5) + XPTable.xpForLevel(6)) / 2L
        assertTrue(XPTable.xpToNextLevel(mid.toDouble()) >= 0L)
        assertEquals(0L, XPTable.xpToNextLevel(XPTable.xpForLevel(99).toDouble()))
    }
}
