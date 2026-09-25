package com.paraskcd.spotlightsearch.preferences.di

import android.content.Context
import androidx.room.Room
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.AppDatabase
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.DatabaseNames
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.SettingsDatabase
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao.AppUsageDao
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao.BlacklistAppsDao
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao.GlobalSearchConfigDao
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao.QuickSearchProviderDao
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao.UserThemeDao
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.migrations.SettingsMigrations
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, DatabaseNames.APP).build()

    @Provides
    @Singleton
    fun provideSettingsDatabase(@ApplicationContext context: Context): SettingsDatabase =
        Room.databaseBuilder(context, SettingsDatabase::class.java, DatabaseNames.SETTINGS)
            .addMigrations(*SettingsMigrations)
            .build()

    @Provides
    fun provideAppUsageDao(db: AppDatabase): AppUsageDao = db.appUsageDao()

    @Provides
    fun provideUserThemeDao(db: SettingsDatabase): UserThemeDao = db.userThemeDao()

    @Provides
    fun provideQuickSearchProviderDao(db: SettingsDatabase): QuickSearchProviderDao =
        db.quickSearchProviderDao()

    @Provides
    fun provideGlobalSearchConfigDao(db: SettingsDatabase): GlobalSearchConfigDao =
        db.globalSearchConfigDao()

    @Provides
    fun provideBlacklistAppsDao(db: SettingsDatabase): BlacklistAppsDao = db.blacklistAppsDao()
}
