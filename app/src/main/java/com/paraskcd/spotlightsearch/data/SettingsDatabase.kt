package com.paraskcd.spotlightsearch.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.paraskcd.spotlightsearch.data.dao.BlacklistAppsDao
import com.paraskcd.spotlightsearch.data.dao.GlobalSearchConfigDao
import com.paraskcd.spotlightsearch.data.dao.QuickSearchProviderDao
import com.paraskcd.spotlightsearch.data.dao.UserThemeDao
import com.paraskcd.spotlightsearch.data.entities.BlacklistAppsEntity
import com.paraskcd.spotlightsearch.data.entities.GlobalSearchConfigEntity
import com.paraskcd.spotlightsearch.data.entities.QuickSearchProviderEntity
import com.paraskcd.spotlightsearch.data.entities.UserThemeEntity

@Database(
    entities = [
        UserThemeEntity::class,
        QuickSearchProviderEntity::class,
        GlobalSearchConfigEntity::class,
        BlacklistAppsEntity::class
   ],
    version = 4,
    exportSchema = false
)
abstract class SettingsDatabase: RoomDatabase() {
    abstract fun userThemeDao(): UserThemeDao
    abstract fun quickSearchProviderDao(): QuickSearchProviderDao
    abstract fun globalSearchConfigDao(): GlobalSearchConfigDao
    abstract fun blacklistAppsDao(): BlacklistAppsDao
}