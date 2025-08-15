package com.paraskcd.spotlightsearch.di

import android.content.Context
import androidx.room.Room
import com.paraskcd.spotlightsearch.data.AppDatabase
import com.paraskcd.spotlightsearch.data.dao.AppUsageDao
import com.paraskcd.spotlightsearch.data.repo.AppUsageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        )
        .fallbackToDestructiveMigration(false)
        .build()

    @Provides
    fun provideAppUsageDao(appDatabase: AppDatabase): AppUsageDao =
        appDatabase.appUsageDao()

    @Provides
    @Singleton
    fun provideAppUsageRepository(appUsageDao: AppUsageDao): AppUsageRepository =
        AppUsageRepository(appUsageDao)
}