package com.focusforge.app

import com.focusforge.app.data.local.FocusSessionConverters
import org.junit.Assert.assertEquals
import org.junit.Test

class FocusSessionConvertersTest {
    @Test
    fun allowlistRoundTrips() {
        val input = setOf("com.focusforge.app", "com.example.study")
        val output = FocusSessionConverters.decodeAllowlist(
            FocusSessionConverters.encodeAllowlist(input)
        )
        assertEquals(input, output)
    }

    @Test
    fun malformedAllowlistFailsClosed() {
        assertEquals(emptySet<String>(), FocusSessionConverters.decodeAllowlist("not-json"))
    }
}
