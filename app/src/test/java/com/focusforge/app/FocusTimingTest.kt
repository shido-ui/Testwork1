package com.focusforge.app

import com.focusforge.app.core.focus.FocusTiming
import org.junit.Assert.assertEquals
import org.junit.Test

class FocusTimingTest {
    @Test
    fun pausedTimeIsNotCountedTwice() {
        val firstSegment = FocusTiming.accumulated(0L, 1_000L, 6_000L)
        val resumedSegment = FocusTiming.accumulated(firstSegment, 10_000L, 13_000L)
        assertEquals(8_000L, resumedSegment)
    }

    @Test
    fun clockRollbackCannotReduceElapsedTime() {
        assertEquals(5_000L, FocusTiming.accumulated(5_000L, 10_000L, 9_000L))
    }
}
