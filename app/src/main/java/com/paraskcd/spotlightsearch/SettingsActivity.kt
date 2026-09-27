package com.paraskcd.spotlightsearch

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.paraskcd.spotlightsearch.preferences.data.ProfileColumn
import com.paraskcd.spotlightsearch.preferences.presentation.navigation.SettingsNavHost
import com.paraskcd.spotlightsearch.preferences.presentation.navigation.SettingsRoute
import com.paraskcd.spotlightsearch.preferences.presentation.utils.SettingsMetrics
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.ThemeViewModel
import com.paraskcd.spotlightsearch.search.infrastructure.window.WindowBlur
import com.paraskcd.spotlightsearch.search.presentation.utils.LocalAppIcons
import com.paraskcd.spotlightsearch.search.presentation.utils.LocalIconPack
import com.paraskcd.spotlightsearch.sources.infrastructure.launch.AppIconIntents
import com.paraskcd.spotlightsearch.ui.theme.SpotlightAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModels()
    private var openRoute by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.setBackgroundBlurRadius(0)
        window.setDimAmount(0f)
        if (savedInstanceState == null) openRoute = routeFor(intent)

        setContent {
            val theme by themeViewModel.state.collectAsState()
            val appIcons by themeViewModel.appIcons.collectAsState()
            val blurEnabled = remember(theme.enableBlur) { WindowBlur.isAvailable(this, theme.enableBlur) }
            LaunchedEffect(blurEnabled) {
                window.setBackgroundBlurRadius(if (blurEnabled) SettingsMetrics.WindowBlurRadius else 0)
            }
            SpotlightAppTheme(themeViewModel) {
                CompositionLocalProvider(LocalIconPack provides theme.iconPack, LocalAppIcons provides appIcons) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background.copy(alpha = SettingsMetrics.backgroundAlpha(blurEnabled))
                    ) {
                        SettingsNavHost(themeViewModel = themeViewModel, onClose = ::finish, openRoute = openRoute)
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        openRoute = routeFor(intent)
    }

    private fun routeFor(intent: Intent): String? {
        if (intent.action != AppIconIntents.ACTION) return null
        val packageName = intent.getStringExtra(AppIconIntents.EXTRA_PACKAGE) ?: return null
        val profile = if (intent.hasExtra(AppIconIntents.EXTRA_PROFILE)) intent.getLongExtra(AppIconIntents.EXTRA_PROFILE, 0) else null
        return SettingsRoute.appIcon(packageName, ProfileColumn.of(profile))
    }
}
