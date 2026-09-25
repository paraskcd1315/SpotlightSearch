package com.paraskcd.spotlightsearch.search.presentation.utils

import androidx.annotation.StringRes
import com.paraskcd.spotlightsearch.search.R
import com.paraskcd.spotlightsearch.sources.domain.model.DeviceSetting

@StringRes
fun DeviceSetting.titleRes(): Int = when (this) {
    DeviceSetting.MAIN -> R.string.setting_main
    DeviceSetting.AIRPLANE_MODE -> R.string.setting_airplane_mode
    DeviceSetting.WIFI -> R.string.setting_wifi
    DeviceSetting.BLUETOOTH -> R.string.setting_bluetooth
    DeviceSetting.DISPLAY -> R.string.setting_display
    DeviceSetting.SOUND -> R.string.setting_sound
    DeviceSetting.DATE_TIME -> R.string.setting_date_time
    DeviceSetting.LANGUAGE -> R.string.setting_language
    DeviceSetting.WIRELESS -> R.string.setting_wireless
    DeviceSetting.SECURITY -> R.string.setting_security
    DeviceSetting.APPS -> R.string.setting_apps
    DeviceSetting.BATTERY_SAVER -> R.string.setting_battery_saver
    DeviceSetting.DATA_ROAMING -> R.string.setting_data_roaming
    DeviceSetting.LOCATION -> R.string.setting_location
    DeviceSetting.INPUT_METHOD -> R.string.setting_input_method
    DeviceSetting.ACCESSIBILITY -> R.string.setting_accessibility
    DeviceSetting.NFC -> R.string.setting_nfc
    DeviceSetting.PRIVACY -> R.string.setting_privacy
    DeviceSetting.BATTERY_OPTIMIZATION -> R.string.setting_battery_optimization
    DeviceSetting.STORAGE -> R.string.setting_storage
    DeviceSetting.DEVICE_INFO -> R.string.setting_device_info
    DeviceSetting.NOTIFICATIONS -> R.string.setting_notifications
}
