package com.paraskcd.spotlightsearch.preferences.infrastructure.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `app_icons` (`packageName` TEXT NOT NULL, `profile` INTEGER NOT NULL, " +
                "`iconPack` TEXT NOT NULL, `drawable` TEXT NOT NULL, PRIMARY KEY(`packageName`, `profile`))"
        )
    }
}
