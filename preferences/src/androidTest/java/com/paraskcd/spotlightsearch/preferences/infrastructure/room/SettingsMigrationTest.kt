package com.paraskcd.spotlightsearch.preferences.infrastructure.room

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.paraskcd.spotlightsearch.preferences.data.ProfileColumn
import com.paraskcd.spotlightsearch.preferences.domain.model.ThemeMode
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity.AppIconEntity
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity.BlacklistAppsEntity
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.migrations.SettingsMigrations
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsMigrationTest {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val databaseName = "settings_migration_test"

    @Before
    fun createVersionOneDatabase() {
        context.deleteDatabase(databaseName)
        val callback = object : SupportSQLiteOpenHelper.Callback(1) {
            override fun onCreate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `user_theme` (`id` INTEGER NOT NULL, `theme` TEXT NOT NULL, " +
                        "`enableBlur` INTEGER, `surfaceColor` INTEGER, `surfaceBrightColor` INTEGER, " +
                        "`backgroundColor` INTEGER, `surfaceTintColor` INTEGER, `onSurfaceColor` INTEGER, " +
                        "`outlineColor` INTEGER, `iconPack` TEXT, PRIMARY KEY(`id`))"
                )
                db.execSQL(
                    "INSERT INTO user_theme (id, theme, enableBlur, surfaceColor) VALUES (0, 'DARK', 1, -16777216)"
                )
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
    fun versionOneMigratesToCurrentSchemaAndKeepsTheSavedTheme() = runTest {
        val database = Room.databaseBuilder(context, SettingsDatabase::class.java, databaseName)
            .addMigrations(*SettingsMigrations)
            .build()

        val theme = database.userThemeDao().get()
        val quickSearch = database.quickSearchProviderDao().observeAll().first()
        val config = database.globalSearchConfigDao().get()
        val blacklist = database.blacklistAppsDao().observe().first()
        database.close()

        assertEquals(ThemeMode.DARK, theme?.theme)
        assertEquals(true, theme?.enableBlur)
        assertEquals(-16777216, theme?.surfaceColor)
        assertEquals(null, theme?.showBranding)
        assertEquals(null, theme?.glassStrength)
        assertEquals(null, config?.sectionOrder)
        assertEquals(null, config?.searchEngine)
        assertEquals(9, quickSearch.size)
        assertTrue(quickSearch.all { it.enabled })
        assertEquals(
            listOf(true, true, true),
            listOf(config?.appsEnabled, config?.contactsEnabled, config?.webSuggestionsEnabled)
        )
        assertTrue(blacklist.isEmpty())
    }

    @Test
    fun theBlacklistKeepsAWorkCopyApartFromItsPersonalCopy() = runTest {
        val database = Room.databaseBuilder(context, SettingsDatabase::class.java, databaseName)
            .addMigrations(*SettingsMigrations)
            .build()
        val dao = database.blacklistAppsDao()

        dao.insert(BlacklistAppsEntity(packageName = "com.clock"))
        dao.insert(BlacklistAppsEntity(packageName = "com.clock", profile = WORK_PROFILE))
        dao.delete("com.clock", ProfileColumn.OWN)
        val blacklist = dao.observe().first()
        database.close()

        assertEquals(listOf(WORK_PROFILE), blacklist.map { it.profile })
    }

    @Test
    fun aChosenIconIsKeptPerAppAndProfileAndReplacedWhole() = runTest {
        val database = Room.databaseBuilder(context, SettingsDatabase::class.java, databaseName)
            .addMigrations(*SettingsMigrations)
            .build()
        val dao = database.appIconDao()

        dao.upsert(AppIconEntity("com.clock", ProfileColumn.OWN, "pack.a", "clock"))
        dao.upsert(AppIconEntity("com.clock", WORK_PROFILE, "pack.a", "clock_work"))
        dao.upsert(AppIconEntity("com.clock", ProfileColumn.OWN, "pack.b", "clock_b"))
        dao.delete("com.clock", WORK_PROFILE)
        val icons = dao.observe().first()
        database.close()

        assertEquals(listOf(AppIconEntity("com.clock", ProfileColumn.OWN, "pack.b", "clock_b")), icons)
    }

    private companion object {
        const val WORK_PROFILE = 10L
    }
}
