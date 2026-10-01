package com.focusforge.app.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS focus_sessions (
                id TEXT NOT NULL PRIMARY KEY,
                startedAtEpochMs INTEGER NOT NULL,
                startedElapsedMs INTEGER NOT NULL,
                accumulatedElapsedMs INTEGER NOT NULL,
                endedAtEpochMs INTEGER,
                state TEXT NOT NULL,
                allowlist TEXT NOT NULL,
                revision INTEGER NOT NULL
            )
            """.trimIndent()
        )
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS index_focus_sessions_state ON focus_sessions(state)"
        )
    }
}
