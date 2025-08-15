package com.paraskcd.spotlightsearch.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.paraskcd.spotlightsearch.data.dao.AppUsageDao
import com.paraskcd.spotlightsearch.data.entities.AppUsageEntity

@Database(
    entities = [AppUsageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun appUsageDao(): AppUsageDao
}