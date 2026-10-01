package com.focusforge.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey val id: String,
    val startedAtEpochMs: Long,
    val startedElapsedMs: Long,
    val accumulatedElapsedMs: Long = 0L,
    val endedAtEpochMs: Long? = null,
    val state: String,
    val allowlist: String,
    val revision: Long = 0L
)
