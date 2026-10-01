package com.focusforge.app.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface InstallationDao {
    @Query("SELECT * FROM installation WHERE id = 1 LIMIT 1")
    fun observe(): Flow<InstallationEntity?>
    @Query("SELECT * FROM installation WHERE id = 1 LIMIT 1")
    suspend fun get(): InstallationEntity?
    @Upsert suspend fun upsert(entity: InstallationEntity)
}
