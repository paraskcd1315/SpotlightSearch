package com.paraskcd.spotlightsearch.preferences.infrastructure.room

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.paraskcd.spotlightsearch.preferences.data.ProfileColumn
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.migrations.AppMigrations
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppMigrationTest {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val databaseName = "app_migration_test"

    @Before
    fun createVersionOneDatabase() {
        context.deleteDatabase(databaseName)
        val callback = object : SupportSQLiteOpenHelper.Callback(1) {
            override fun onCreate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `app_usage` (`packageName` TEXT NOT NULL, `openCount` INTEGER NOT NULL, " +
                        "`lastOpenedAt` INTEGER NOT NULL, PRIMARY KEY(`packageName`))"
                )
                db.execSQL("INSERT INTO app_usage (packageName, openCount, lastOpenedAt) VALUES ('com.clock', 7, 1000)")
            }

            override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) = Unit
        }
        val configuration = SupportSQLiteOpenHelper.Configuration.builder(context)
            .name(databaseName)
            .callback(callback)
            .build()
        FrameworkSQLiteOpenHelperFactory().create(configuration).writableDatabase.close()
    }

    @After
    fun deleteDatabase() {
        context.deleteDatabase(databaseName)
    }

    @Test
    fun versionOneKeepsItsCountsAsTheOwnProfile() = runTest {
        val database = Room.databaseBuilder(context, AppDatabase::class.java, databaseName)
            .addMigrations(*AppMigrations)
            .build()
        val dao = database.appUsageDao()

        dao.increment("com.clock", WORK_PROFILE, 2000)
        dao.increment("com.clock", ProfileColumn.OWN, 3000)
        val rows = dao.observeTopAppUsages(10).first()
        database.close()

        assertEquals(2, rows.size)
        assertEquals(8L, rows.single { it.profile == ProfileColumn.OWN }.openCount)
        assertEquals(1L, rows.single { it.profile == WORK_PROFILE }.openCount)
    }

    private companion object {
        const val WORK_PROFILE = 10L
    }
}
