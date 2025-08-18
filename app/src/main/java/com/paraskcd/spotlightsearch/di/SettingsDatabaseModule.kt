package com.paraskcd.spotlightsearch.di

import android.content.Context
import androidx.room.Room
import com.paraskcd.spotlightsearch.data.SettingsDatabase
import com.paraskcd.spotlightsearch.data.dao.GlobalSearchConfigDao
import com.paraskcd.spotlightsearch.data.dao.QuickSearchProviderDao
import com.paraskcd.spotlightsearch.data.dao.UserThemeDao
import com.paraskcd.spotlightsearch.data.migrations.MIGRATION_1_2
import com.paraskcd.spotlightsearch.data.migrations.MIGRATION_2_3
import com.paraskcd.spotlightsearch.data.repo.GlobalSearchConfigRepository
import com.paraskcd.spotlightsearch.data.repo.QuickSearchProviderRepository
import com.paraskcd.spotlightsearch.data.repo.UserThemeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsDatabaseModule {
    @Provides
    @Singleton
    fun provideSettingsDatabase(
        @ApplicationContext context: Context
    ): SettingsDatabase =
        Room.databaseBuilder(
            context,
            SettingsDatabase::class.java,
            "settings_database"
        )
        .addMigrations(MIGRATION_1_2)
        .addMigrations(MIGRATION_2_3)
        .build()

    @Provides
    fun provideUserThemeDao(db: SettingsDatabase): UserThemeDao = db.userThemeDao()

    @Provides
    @Singleton
    fun provideUserThemeRepository(dao: UserThemeDao): UserThemeRepository =
        UserThemeRepository(dao)

    @Provides
    fun provideQuickSearchProviderDao(db: SettingsDatabase): QuickSearchProviderDao = db.quickSearchProviderDao()

    @Provides
    @Singleton
    fun provideQuickSearchProviderRepository(dao: QuickSearchProviderDao): QuickSearchProviderRepository = QuickSearchProviderRepository(dao)

    @Provides
    fun provideGlobalSearchConfigDao(db: SettingsDatabase): GlobalSearchConfigDao = db.globalSearchConfigDao()

    @Provides
    @Singleton
    fun provideGlobalSearchConfigRepository(dao: GlobalSearchConfigDao): GlobalSearchConfigRepository = GlobalSearchConfigRepository(dao)
}