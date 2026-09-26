package com.paraskcd.spotlightsearch.sources.infrastructure.devicesettings

import android.provider.Settings
import com.paraskcd.spotlightsearch.sources.domain.model.DeviceSetting

object DeviceSettingsCatalog {
    val keywords: Map<DeviceSetting, List<String>> = mapOf(
        DeviceSetting.MAIN to listOf("main settings", "settings app", "settings", "ajustes", "principal"),
        DeviceSetting.AIRPLANE_MODE to listOf("modo avión", "airplane mode", "airplane"),
        DeviceSetting.WIFI to listOf("wifi", "wi‑fi", "internet"),
        DeviceSetting.BLUETOOTH to listOf("bluetooth"),
        DeviceSetting.DISPLAY to listOf("brightness", "display", "dark mode"),
        DeviceSetting.SOUND to listOf("volume", "sound"),
        DeviceSetting.DATE_TIME to listOf("fecha", "hora", "time", "date"),
        DeviceSetting.LANGUAGE to listOf("idioma", "language", "locale"),
        DeviceSetting.WIRELESS to listOf("redes", "wireless"),
        DeviceSetting.SECURITY to listOf("security", "lockscreen", "password"),
        DeviceSetting.APPS to listOf("apps", "applications"),
        DeviceSetting.BATTERY_SAVER to listOf("battery", "savings", "battery save"),
        DeviceSetting.DATA_ROAMING to listOf("data roaming", "roaming"),
        DeviceSetting.LOCATION to listOf("gps", "location"),
        DeviceSetting.INPUT_METHOD to listOf("keyboard", "input settings", "input"),
        DeviceSetting.ACCESSIBILITY to listOf("accessibility"),
        DeviceSetting.NFC to listOf("nfc", "contactless"),
        DeviceSetting.PRIVACY to listOf("privacy"),
        DeviceSetting.BATTERY_OPTIMIZATION to listOf("battery", "battery optimization"),
        DeviceSetting.STORAGE to listOf("internal memory", "storage"),
        DeviceSetting.DEVICE_INFO to listOf("information", "device info", "about device", "device", "info"),
        DeviceSetting.NOTIFICATIONS to listOf("notifications", "notification")
    )

    fun action(setting: DeviceSetting): String = when (setting) {
        DeviceSetting.MAIN -> Settings.ACTION_SETTINGS
        DeviceSetting.AIRPLANE_MODE -> Settings.ACTION_AIRPLANE_MODE_SETTINGS
        DeviceSetting.WIFI -> Settings.ACTION_WIFI_SETTINGS
        DeviceSetting.BLUETOOTH -> Settings.ACTION_BLUETOOTH_SETTINGS
        DeviceSetting.DISPLAY -> Settings.ACTION_DISPLAY_SETTINGS
        DeviceSetting.SOUND -> Settings.ACTION_SOUND_SETTINGS
        DeviceSetting.DATE_TIME -> Settings.ACTION_DATE_SETTINGS
        DeviceSetting.LANGUAGE -> Settings.ACTION_LOCALE_SETTINGS
        DeviceSetting.WIRELESS -> Settings.ACTION_WIRELESS_SETTINGS
        DeviceSetting.SECURITY -> Settings.ACTION_SECURITY_SETTINGS
        DeviceSetting.APPS -> Settings.ACTION_APPLICATION_SETTINGS
        DeviceSetting.BATTERY_SAVER -> Settings.ACTION_BATTERY_SAVER_SETTINGS
        DeviceSetting.DATA_ROAMING -> Settings.ACTION_DATA_ROAMING_SETTINGS
        DeviceSetting.LOCATION -> Settings.ACTION_LOCATION_SOURCE_SETTINGS
        DeviceSetting.INPUT_METHOD -> Settings.ACTION_INPUT_METHOD_SETTINGS
        DeviceSetting.ACCESSIBILITY -> Settings.ACTION_ACCESSIBILITY_SETTINGS
        DeviceSetting.NFC -> Settings.ACTION_NFC_SETTINGS
        DeviceSetting.PRIVACY -> Settings.ACTION_PRIVACY_SETTINGS
        DeviceSetting.BATTERY_OPTIMIZATION -> Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
        DeviceSetting.STORAGE -> Settings.ACTION_INTERNAL_STORAGE_SETTINGS
        DeviceSetting.DEVICE_INFO -> Settings.ACTION_DEVICE_INFO_SETTINGS
        DeviceSetting.NOTIFICATIONS -> Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
    }
}
