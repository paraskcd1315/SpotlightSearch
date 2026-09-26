package com.paraskcd.spotlightsearch.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SignatureTheme

@Composable
fun SpotlightSearchTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    overrides: ColorOverrides = ColorOverrides(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val wallpaper = remember(context, darkTheme) {
        if (darkTheme) dynamicDarkColorScheme(context).primary else dynamicLightColorScheme(context).primary
    }
    SignatureTheme(
        seed = overrides.surfaceTint ?: wallpaper,
        darkTheme = darkTheme,
        adjust = remember(overrides) { { colors -> overrides.applyTo(colors) } },
        content = content
    )
}
