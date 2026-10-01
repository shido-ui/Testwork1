package com.focusforge.app.core.focus

object FocusTiming {
    fun accumulated(previous: Long, segmentStart: Long, now: Long): Long =
        previous + (now - segmentStart).coerceAtLeast(0L)
}
