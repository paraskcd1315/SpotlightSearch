package com.paraskcd.spotlightsearch.search.infrastructure.window

import android.content.Context
import android.os.PowerManager
import android.view.WindowManager

object WindowBlur {
    fun isAvailable(context: Context, userEnabled: Boolean): Boolean {
        val windowManager = context.getSystemService(WindowManager::class.java)
        val powerManager = context.getSystemService(PowerManager::class.java)
        return userEnabled &&
            windowManager?.isCrossWindowBlurEnabled == true &&
            powerManager?.isPowerSaveMode != true
    }
}
