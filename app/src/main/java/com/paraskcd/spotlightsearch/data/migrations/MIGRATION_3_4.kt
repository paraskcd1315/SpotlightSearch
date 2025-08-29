package com.paraskcd.spotlightsearch.data.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `blacklist_apps` (
                `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                `packageName` TEXT NOT NULL
            )
            """.trimIndent()
        )
    }
}