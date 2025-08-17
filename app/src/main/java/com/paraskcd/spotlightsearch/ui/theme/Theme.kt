package com.paraskcd.spotlightsearch.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SpotlightSearchTheme(
    viewModel: ThemeViewModel = hiltViewModel(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val scheme = buildColorScheme(viewModel, dynamicColor)
    MaterialTheme(
        colorScheme = scheme,
        typography = Typography,
        content = content
    )
}