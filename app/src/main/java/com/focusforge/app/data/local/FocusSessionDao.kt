package com.focusforge.app.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusSessionDao {
    @Query("SELECT * FROM focus_sessions WHERE id = :id LIMIT 1")
    suspend fun get(id: String): FocusSessionEntity?

    @Query("SELECT * FROM focus_sessions WHERE state IN ('RUNNING','PAUSED') ORDER BY startedAtEpochMs DESC LIMIT 1")
    fun observeActive(): Flow<FocusSessionEntity?>

    @Query("SELECT * FROM focus_sessions WHERE state IN ('RUNNING','PAUSED') ORDER BY startedAtEpochMs DESC LIMIT 1")
    suspend fun getActive(): FocusSessionEntity?

    @Upsert
    suspend fun upsert(session: FocusSessionEntity)
}
