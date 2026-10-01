package com.focusforge.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [InstallationEntity::class], version = 1, exportSchema = true)
abstract class FocusForgeDatabase : RoomDatabase() {
    abstract fun installationDao(): InstallationDao
}
