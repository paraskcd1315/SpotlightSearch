package com.paraskcd.spotlightsearch

import android.content.Intent
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.ThemeViewModel
import com.paraskcd.spotlightsearch.search.domain.model.peek.PeekPhase
import com.paraskcd.spotlightsearch.search.infrastructure.peek.PeekChannel
import com.paraskcd.spotlightsearch.search.infrastructure.peek.PeekProtocol
import com.paraskcd.spotlightsearch.search.infrastructure.window.WindowBlur
import com.paraskcd.spotlightsearch.search.presentation.overlay.LocalOverlayDrag
import com.paraskcd.spotlightsearch.search.presentation.overlay.LocalOverlayMotion
import com.paraskcd.spotlightsearch.search.presentation.overlay.rememberOverlayDrag
import com.paraskcd.spotlightsearch.search.presentation.overlay.rememberOverlayMotion
import com.paraskcd.spotlightsearch.search.presentation.screens.SearchScreen
import com.paraskcd.spotlightsearch.search.presentation.utils.LocalAppIcons
import com.paraskcd.spotlightsearch.search.presentation.utils.LocalIconPack
import com.paraskcd.spotlightsearch.search.presentation.viewmodels.SearchViewModel
import com.paraskcd.spotlightsearch.ui.theme.SpotlightAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val searchViewModel: SearchViewModel by viewModels()
    private val themeViewModel: ThemeViewModel by viewModels()

    @Inject
    lateinit var peekChannel: PeekChannel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.setDimAmount(0f)
        window.insetsController?.apply {
            show(WindowInsets.Type.statusBars())
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        val peekLaunch = intent.getBooleanExtra(PeekProtocol.EXTRA_PEEK, false)
        if (peekLaunch) peekChannel.begin()

        setContent {
            val theme by themeViewModel.state.collectAsState()
            val appIcons by themeViewModel.appIcons.collectAsState()
            val peek by peekChannel.state.collectAsState()
            val peekState = if (peekLaunch) peek else null
            val overlayMotion = rememberOverlayMotion(peekState, onCancelled = ::finishQuietly)
            val overlayDrag = rememberOverlayDrag(overlayMotion, onShown = peekChannel::shown, onDismissed = ::finishQuietly)
            val blurEnabled = remember(theme.enableBlur) { WindowBlur.isAvailable(this, theme.enableBlur) }
            SpotlightAppTheme(themeViewModel) {
                CompositionLocalProvider(
                    LocalIconPack provides theme.iconPack,
                    LocalAppIcons provides appIcons,
                    LocalOverlayMotion provides overlayMotion,
                    LocalOverlayDrag provides overlayDrag
                ) {
                    SearchScreen(
                        viewModel = searchViewModel,
                        blurEnabled = blurEnabled,
                        showBranding = theme.showBranding,
                        appLayout = theme.appLayout,
                        appName = stringResource(R.string.app_name),
                        onOpenSettings = { startActivity(Intent(this, SettingsActivity::class.java)) },
                        onClose = ::finish,
                        peekLaunch = peekLaunch,
                        barFocused = !peekLaunch || (peekState?.phase == PeekPhase.Committed && !overlayMotion.moving)
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        if (isFinishing) peekChannel.closed()
        super.onDestroy()
    }

    private fun finishQuietly() {
        overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0, 0)
        finish()
    }
}
