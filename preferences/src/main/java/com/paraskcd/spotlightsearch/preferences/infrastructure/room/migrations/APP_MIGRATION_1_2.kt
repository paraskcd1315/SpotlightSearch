package com.paraskcd.spotlightsearch.preferences.infrastructure.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.paraskcd.spotlightsearch.preferences.data.ProfileColumn

val APP_MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        listOf(
            "CREATE TABLE IF NOT EXISTS `app_usage_new` (`packageName` TEXT NOT NULL, `profile` INTEGER NOT NULL, " +
                "`openCount` INTEGER NOT NULL, `lastOpenedAt` INTEGER NOT NULL, PRIMARY KEY(`packageName`, `profile`))",
            "INSERT INTO `app_usage_new` (`packageName`, `profile`, `openCount`, `lastOpenedAt`) " +
                "SELECT `packageName`, ${ProfileColumn.OWN}, `openCount`, `lastOpenedAt` FROM `app_usage`",
            "DROP TABLE `app_usage`",
            "ALTER TABLE `app_usage_new` RENAME TO `app_usage`"
        ).forEach(db::execSQL)
    }
}
