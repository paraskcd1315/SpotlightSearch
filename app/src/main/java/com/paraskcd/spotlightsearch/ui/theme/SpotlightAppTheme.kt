package com.paraskcd.spotlightsearch.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.LocalSpPanelAlpha
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.LocalSpTextScale
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpTextScaled
import com.paraskcd.spotlightsearch.designsystem.theme.SpotlightSearchTheme
import com.paraskcd.spotlightsearch.preferences.presentation.utils.panelAlpha
import com.paraskcd.spotlightsearch.preferences.presentation.utils.scale
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.ThemeViewModel

@Composable
fun SpotlightAppTheme(
    viewModel: ThemeViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    val theme by viewModel.state.collectAsState()
    SpotlightSearchTheme(
        darkTheme = theme.isDark(isSystemInDarkTheme()),
        overrides = theme.colorOverrides
    ) {
        CompositionLocalProvider(
            LocalSpPanelAlpha provides theme.glassStrength.panelAlpha(),
            LocalSpTextScale provides theme.textSize.scale()
        ) {
            SpTextScaled(content)
        }
    }
}
