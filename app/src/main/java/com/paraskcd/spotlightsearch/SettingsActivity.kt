package com.paraskcd.spotlightsearch

import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.paraskcd.spotlightsearch.ui.screens.SettingsScreen
import com.paraskcd.spotlightsearch.ui.theme.SpotlightSearchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isBatterySaver = remember {
                isBatterySaverOn(this)
            }
            val supportsBlur = remember {
                supportsBlur()
            }

            SpotlightSearchTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = if (isBatterySaver || !supportsBlur) MaterialTheme.colorScheme.background.copy(alpha = 0.9f) else MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                ) {
                    SettingsScreen()
                }
            }
        }

        window.setBackgroundBlurRadius(100)
        window.setDimAmount(0.0f)
    }

    private fun supportsBlur(): Boolean {
        return windowManager.isCrossWindowBlurEnabled
    }

    private fun isBatterySaverOn(context: Context): Boolean {
        val powerManager = context.getSystemService(POWER_SERVICE) as PowerManager
        return powerManager.isPowerSaveMode
    }
}