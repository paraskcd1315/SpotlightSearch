package com.paraskcd.spotlightsearch.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.paraskcd.spotlightsearch.designsystem.theme.SpotlightSearchTheme
import com.paraskcd.spotlightsearch.preferences.presentation.viewmodels.ThemeViewModel

@Composable
fun SpotlightAppTheme(
    viewModel: ThemeViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    val theme by viewModel.state.collectAsState()
    SpotlightSearchTheme(
        darkTheme = theme.isDark(isSystemInDarkTheme()),
        overrides = theme.colorOverrides,
        content = content
    )
}
