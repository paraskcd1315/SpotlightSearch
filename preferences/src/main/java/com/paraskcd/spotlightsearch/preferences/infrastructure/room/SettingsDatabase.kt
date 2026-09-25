package com.paraskcd.spotlightsearch.preferences.infrastructure.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao.BlacklistAppsDao
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao.GlobalSearchConfigDao
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao.QuickSearchProviderDao
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao.UserThemeDao
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity.BlacklistAppsEntity
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity.GlobalSearchConfigEntity
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity.QuickSearchProviderEntity
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity.UserThemeEntity

@Database(
    entities = [
        UserThemeEntity::class,
        QuickSearchProviderEntity::class,
        GlobalSearchConfigEntity::class,
        BlacklistAppsEntity::class
    ],
    version = 4,
    exportSchema = true
)
abstract class SettingsDatabase : RoomDatabase() {
    abstract fun userThemeDao(): UserThemeDao
    abstract fun quickSearchProviderDao(): QuickSearchProviderDao
    abstract fun globalSearchConfigDao(): GlobalSearchConfigDao
    abstract fun blacklistAppsDao(): BlacklistAppsDao
}