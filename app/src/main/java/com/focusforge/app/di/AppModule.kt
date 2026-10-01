package com.focusforge.app.di

import android.content.Context
import androidx.room.Room
import com.focusforge.app.data.local.FocusForgeDatabase
import com.focusforge.app.data.storage.AppStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton
    fun database(@ApplicationContext context: Context): FocusForgeDatabase =
        Room.databaseBuilder(context, FocusForgeDatabase::class.java, "focusforge.db")
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()

    @Provides @Singleton
    fun storage(@ApplicationContext context: Context): AppStorage = AppStorage(context)
}
