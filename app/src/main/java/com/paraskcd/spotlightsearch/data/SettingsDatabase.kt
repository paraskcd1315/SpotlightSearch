package com.paraskcd.spotlightsearch.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.paraskcd.spotlightsearch.data.dao.UserThemeDao
import com.paraskcd.spotlightsearch.data.entities.UserThemeEntity

@Database(
    entities = [UserThemeEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SettingsDatabase: RoomDatabase() {
    abstract fun userThemeDao(): UserThemeDao
}