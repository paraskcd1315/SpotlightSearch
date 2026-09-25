package com.paraskcd.spotlightsearch.preferences.infrastructure.system

import android.content.Context
import android.content.Intent
import android.provider.Settings

object AssistantSettings {
    fun open(context: Context): Boolean {
        val intent = listOf(
            Intent(Settings.ACTION_VOICE_INPUT_SETTINGS),
            Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
        ).firstOrNull { it.resolveActivity(context.packageManager) != null } ?: return false
        context.startActivity(intent)
        return true
    }
}
