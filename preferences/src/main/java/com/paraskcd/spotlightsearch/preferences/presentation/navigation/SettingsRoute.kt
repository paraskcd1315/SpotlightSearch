package com.paraskcd.spotlightsearch.preferences.presentation.navigation

object SettingsRoute {
    const val HOME = "settings_home"
    const val PERSONALIZATION = "settings_personalization"
    const val COLOR_PICKER = "settings_color_picker/{key}"
    const val COLOR_PICKER_ARG = "key"
    const val FEATURES = "settings_features"
    const val QUICK_SEARCH = "settings_quick_search"
    const val MANAGE_APPS = "settings_manage_apps"
    const val WEB_SUGGESTIONS = "settings_web_suggestions"
    const val MANAGE_CONTACTS = "settings_manage_contacts"
    const val APPS_BLACKLIST = "settings_apps_blacklist"
    const val ABOUT = "settings_about"
    const val RESULTS = "settings_results"
    const val APP_ICONS = "settings_app_icons"
    const val APP_ICON = "settings_app_icon/{pkg}/{profile}"
    const val APP_ICON_PACKAGE_ARG = "pkg"
    const val APP_ICON_PROFILE_ARG = "profile"

    fun colorPicker(key: String) = "settings_color_picker/$key"

    fun appIcon(packageName: String, profileColumn: Long) = "settings_app_icon/$packageName/$profileColumn"
}
