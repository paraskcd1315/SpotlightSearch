package com.paraskcd.spotlightsearch.data.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `global_search_config` (
                `id` INTEGER NOT NULL,
                `appsEnabled` INTEGER NOT NULL,
                `contactsEnabled` INTEGER NOT NULL,
                `webSuggestionsEnabled` INTEGER NOT NULL,
                PRIMARY KEY(`id`)
            )
            """.trimIndent()
        )
        db.execSQL(
            "INSERT OR IGNORE INTO global_search_config (id,appsEnabled,contactsEnabled,webSuggestionsEnabled) VALUES (0,1,1,1)"
        )
    }
}