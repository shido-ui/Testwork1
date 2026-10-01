package com.focusforge.app.core.focus

enum class FocusState { RUNNING, PAUSED, ENDED }

data class FocusSession(
    val id: String,
    val state: FocusState,
    val startedElapsedMs: Long,
    val accumulatedElapsedMs: Long,
    val allowlist: Set<String>,
    val revision: Long
)
