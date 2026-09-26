package com.paraskcd.spotlightsearch

import android.os.Bundle
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
import com.paraskcd.spotlightsearch.preferences.presentation.navigation.SettingsNavHost
import com.paraskcd.spotlightsearch.preferences.presentation.utils.SettingsMetrics
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.ThemeViewModel
import com.paraskcd.spotlightsearch.search.infrastructure.window.WindowBlur
import com.paraskcd.spotlightsearch.ui.theme.SpotlightAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.setBackgroundBlurRadius(0)
        window.setDimAmount(0f)

        setContent {
            val theme by themeViewModel.state.collectAsState()
            val blurEnabled = remember(theme.enableBlur) { WindowBlur.isAvailable(this, theme.enableBlur) }
            LaunchedEffect(blurEnabled) {
                window.setBackgroundBlurRadius(if (blurEnabled) SettingsMetrics.WindowBlurRadius else 0)
            }
            SpotlightAppTheme(themeViewModel) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background.copy(alpha = SettingsMetrics.backgroundAlpha(blurEnabled))
                ) {
                    SettingsNavHost(themeViewModel = themeViewModel, onClose = ::finish)
                }
            }
        }
    }
}
