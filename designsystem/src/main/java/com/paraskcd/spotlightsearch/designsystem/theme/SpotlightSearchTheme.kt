package com.paraskcd.spotlightsearch.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun SpotlightSearchTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    overrides: ColorOverrides = ColorOverrides(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = buildColorScheme(darkTheme, dynamicColor, overrides),
        typography = SpotlightTypography,
        content = content
    )
}
