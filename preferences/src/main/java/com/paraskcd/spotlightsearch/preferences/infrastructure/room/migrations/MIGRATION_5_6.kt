package com.paraskcd.spotlightsearch.preferences.infrastructure.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        listOf(
            "ALTER TABLE `global_search_config` ADD COLUMN `sectionOrder` TEXT",
            "ALTER TABLE `global_search_config` ADD COLUMN `hiddenSections` TEXT",
            "ALTER TABLE `global_search_config` ADD COLUMN `rowsPerSection` INTEGER",
            "ALTER TABLE `global_search_config` ADD COLUMN `frequentRows` INTEGER",
            "ALTER TABLE `global_search_config` ADD COLUMN `searchEngine` TEXT",
            "ALTER TABLE `user_theme` ADD COLUMN `glassStrength` TEXT",
            "ALTER TABLE `user_theme` ADD COLUMN `textSize` TEXT",
            "ALTER TABLE `user_theme` ADD COLUMN `appLayout` TEXT"
        ).forEach(db::execSQL)
    }
}
