package com.paraskcd.spotlightsearch.providers

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import com.paraskcd.spotlightsearch.enums.SearchResultType
import com.paraskcd.spotlightsearch.types.SearchResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsSearchProvider @Inject constructor(@ApplicationContext private val context: Context) {

    private val settingsItems = listOf(
        Triple(
            listOf("main settings", "settings app", "settings", "ajustes", "principal"),
            "Main settings",
            Settings.ACTION_SETTINGS
        ),
        Triple(
            listOf("modo avión", "airplane mode", "airplane"),
            "Airplane mode settings",
            Settings.ACTION_AIRPLANE_MODE_SETTINGS
        ),
        Triple(
            listOf("wifi", "wi‑fi", "internet"),
            "Wi‑Fi settings",
            Settings.ACTION_WIFI_SETTINGS
        ),
        Triple(
            listOf("bluetooth"),
            "Bluetooth settings",
            Settings.ACTION_BLUETOOTH_SETTINGS
        ),
        Triple(
            listOf("brightness", "display", "dark mode"),
            "Display settings",
            Settings.ACTION_DISPLAY_SETTINGS
        ),
        Triple(
            listOf( "volume", "sound"),
            "Sound settings",
            Settings.ACTION_SOUND_SETTINGS
        ),
        Triple(
            listOf("fecha", "hora", "time", "date"),
            "Date & time settings",
            Settings.ACTION_DATE_SETTINGS
        ),
        Triple(
            listOf("idioma", "language", "locale"),
            "Language & input settings",
            Settings.ACTION_LOCALE_SETTINGS
        ),
        Triple(
            listOf("redes", "wireless"),
            "Wireless Settings",
            Settings.ACTION_WIRELESS_SETTINGS
        ),
        Triple(
            listOf("security", "lockscreen", "password"),
            "Security Settings",
            Settings.ACTION_SECURITY_SETTINGS
        ),
        Triple(
            listOf("apps", "applications"),
            "Apps Settings",
            Settings.ACTION_APPLICATION_SETTINGS
        ),
        Triple(
            listOf("battery", "savings", "battery save"),
            "Battery Settings",
            Settings.ACTION_BATTERY_SAVER_SETTINGS
        ),
        Triple(
            listOf("data roaming", "roaming"),
            "Battery Settings",
            Settings.ACTION_DATA_ROAMING_SETTINGS
        ),
        Triple(
            listOf("gps", "location"),
            "Location Settings",
            Settings.ACTION_LOCATION_SOURCE_SETTINGS
        ),
        Triple(
            listOf("keyboard", "input settings", "input"),
            "Input method settings",
            Settings.ACTION_INPUT_METHOD_SETTINGS
        ),
        Triple(
            listOf("accessibility"),
            "Accessibility settings",
            Settings.ACTION_ACCESSIBILITY_SETTINGS
        ),
        Triple(
            listOf("nfc", "contactless"),
            "NFC settings",
            Settings.ACTION_NFC_SETTINGS
        ),
        Triple(
            listOf("privacy"),
            "Privacy settings",
            Settings.ACTION_PRIVACY_SETTINGS
        ),
        Triple(
            listOf("battery", "battery optimization"),
            "Ignore battery optimization",
            Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
        ),
        Triple(
            listOf("internal memory", "storage"),
            "Storage settings",
            Settings.ACTION_INTERNAL_STORAGE_SETTINGS
        ),
        Triple(
            listOf("information", "device info", "about device", "device", "info"),
            "Device info",
            Settings.ACTION_DEVICE_INFO_SETTINGS
        ),
        Triple(
            listOf("notifications", "notification"),
            "Notification settings",
            Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
        )
    )

    fun searchSettings(query: String): List<SearchResult> {
        if (query.isBlank()) return emptyList()
        val lowerQuery = query.lowercase()

        val matches = settingsItems.filter { (keywords, _, _) ->
            keywords.any { lowerQuery.contains(it) }
        }

        if (matches.isEmpty()) return emptyList()

        val results = mutableListOf<SearchResult>()

        results.add(SearchResult(title = "Settings", isHeader = true, onClick = {}))

        matches.forEach { (_, title, action) ->
            val intent = Intent(action)
            if (intent.resolveActivity(context.packageManager) != null) {
                results.add(
                    SearchResult(
                        title = title,
                        subtitle = "Open $title",
                        iconVector = Icons.Filled.Settings,
                        onClick = {
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(intent)
                        },
                        searchResultType = SearchResultType.SETTINGS
                    )
                )
            }
        }
        return results
    }
}