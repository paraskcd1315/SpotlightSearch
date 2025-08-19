package com.paraskcd.spotlightsearch.data.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS quick_search_provider (
                packageName TEXT NOT NULL PRIMARY KEY,
                enabled INTEGER NOT NULL,
                sortOrder INTEGER NOT NULL
            )
            """.trimIndent()
        )

        val defaults = listOf(
            "com.google.android.googlequicksearchbox",
            "com.google.android.youtube",
            "com.google.android.apps.youtube.music",
            "com.google.android.apps.maps",
            "com.android.vending",
            "com.instagram.barcelona",
            "com.linkedin.android",
            "com.twitter.android",
            "com.facebook.katana"
        )
        defaults.forEachIndexed { index, pkg ->
            db.execSQL(
                "INSERT OR IGNORE INTO quick_search_provider(packageName, enabled, sortOrder) VALUES(?, 1, ?)",
                arrayOf<Any?>(pkg, index)
            )
        }
    }
}