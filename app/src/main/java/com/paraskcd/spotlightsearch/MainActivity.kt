package com.paraskcd.spotlightsearch

import android.content.Intent
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.ThemeViewModel
import com.paraskcd.spotlightsearch.search.infrastructure.window.WindowBlur
import com.paraskcd.spotlightsearch.search.presentation.screens.SearchScreen
import com.paraskcd.spotlightsearch.search.presentation.viewmodels.SearchViewModel
import com.paraskcd.spotlightsearch.ui.theme.SpotlightAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val searchViewModel: SearchViewModel by viewModels()
    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.setDimAmount(0f)
        window.insetsController?.apply {
            show(WindowInsets.Type.statusBars())
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        setContent {
            val theme by themeViewModel.state.collectAsState()
            val blurEnabled = remember(theme.enableBlur) { WindowBlur.isAvailable(this, theme.enableBlur) }
            SpotlightAppTheme(themeViewModel) {
                SearchScreen(
                    viewModel = searchViewModel,
                    blurEnabled = blurEnabled,
                    appName = stringResource(R.string.app_name),
                    onOpenSettings = { startActivity(Intent(this, SettingsActivity::class.java)) },
                    onClose = ::finish
                )
            }
        }
    }
}
