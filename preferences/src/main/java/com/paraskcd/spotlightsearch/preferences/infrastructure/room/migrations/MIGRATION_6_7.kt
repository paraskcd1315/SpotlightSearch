package com.paraskcd.spotlightsearch.preferences.infrastructure.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.paraskcd.spotlightsearch.preferences.data.ProfileColumn

val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `blacklist_apps` ADD COLUMN `profile` INTEGER NOT NULL DEFAULT ${ProfileColumn.OWN_SQL}")
    }
}
