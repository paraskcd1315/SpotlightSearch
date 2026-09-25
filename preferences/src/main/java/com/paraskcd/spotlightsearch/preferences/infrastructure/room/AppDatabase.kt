package com.paraskcd.spotlightsearch.preferences.infrastructure.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao.AppUsageDao
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity.AppUsageEntity

@Database(
    entities = [AppUsageEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appUsageDao(): AppUsageDao
}