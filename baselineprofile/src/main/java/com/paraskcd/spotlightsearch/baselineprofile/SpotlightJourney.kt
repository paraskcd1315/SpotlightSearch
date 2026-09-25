package com.paraskcd.spotlightsearch.baselineprofile

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until

object SpotlightJourney {
    private const val WAIT_MS = 5_000L
    private const val SETTLE_MS = 1_500L
    private const val SEARCH_FIELD = "android.widget.EditText"
    private const val SETTINGS_DESC = "Settings"
    private const val SETTINGS_HOME_ROW = "Features and Functionality"
    private val Queries = listOf("ma", "maps", "2+2", "wifi")

    fun MacrobenchmarkScope.typeQueries() {
        device.wait(Until.hasObject(By.clazz(SEARCH_FIELD)), WAIT_MS)
        Queries.forEach { query ->
            device.findObject(By.clazz(SEARCH_FIELD))?.text = query
            device.waitForIdle(SETTLE_MS)
        }
        device.findObject(By.clazz(SEARCH_FIELD))?.text = ""
        device.waitForIdle(SETTLE_MS)
    }

    fun MacrobenchmarkScope.openSettings() {
        device.findObject(By.desc(SETTINGS_DESC))?.click() ?: return
        device.wait(Until.hasObject(By.text(SETTINGS_HOME_ROW)), WAIT_MS)
        device.findObject(By.text(SETTINGS_HOME_ROW))?.click()
        device.waitForIdle(SETTLE_MS)
        device.pressBack()
        device.pressBack()
    }
}
