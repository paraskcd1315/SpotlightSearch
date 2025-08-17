package com.paraskcd.spotlightsearch

import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.paraskcd.spotlightsearch.ui.screens.SettingsScreen
import com.paraskcd.spotlightsearch.ui.theme.SpotlightSearchTheme
import com.paraskcd.spotlightsearch.ui.theme.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue
import kotlin.math.roundToInt

@AndroidEntryPoint
class SettingsActivity : ComponentActivity() {
    val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeState by themeViewModel.state.collectAsState()

            val isBatterySaver = remember {
                isBatterySaverOn(this)
            }
            val supportsBlur = remember {
                supportsBlur()
            }

            val userPrefEnableBlur = themeState.enableBlur != false // null o true => true
            val effectiveBlur = supportsBlur && userPrefEnableBlur && !isBatterySaver

            SpotlightSearchTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = if (!effectiveBlur) MaterialTheme.colorScheme.background.copy(alpha = 0.9f) else MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                ) {
                    LaunchedEffect(effectiveBlur) {
                        window.setBackgroundBlurRadius(
                            if (effectiveBlur) (100f).coerceIn(0f, 100f).roundToInt()
                            else 0
                        )
                    }

                    SettingsScreen()
                }
            }
        }

        window.setBackgroundBlurRadius(0)
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