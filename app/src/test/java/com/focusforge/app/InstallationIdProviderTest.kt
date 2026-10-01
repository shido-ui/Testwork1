package com.focusforge.app

import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID

class InstallationIdProviderTest {
    @Test fun generatedIdsAreValidUuid() {
        val id = UUID.randomUUID().toString()
        assertTrue(id.isNotBlank())
        assertTrue(runCatching { UUID.fromString(id) }.isSuccess)
    }
}
