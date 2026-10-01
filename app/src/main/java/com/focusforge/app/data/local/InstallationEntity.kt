package com.focusforge.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "installation")
data class InstallationEntity(
    @PrimaryKey val id: Int = 1,
    val installationId: String,
    val createdAtEpochMs: Long,
    val updatedAtEpochMs: Long
)
