package com.paraskcd.spotlightsearch.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun buildColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    overrides: ColorOverrides
): ColorScheme {
    val context = LocalContext.current
    val base = when {
        dynamicColor && darkTheme -> dynamicDarkColorScheme(context)
        dynamicColor -> dynamicLightColorScheme(context)
        darkTheme -> darkColorScheme()
        else -> lightColorScheme()
    }

    return base.copy(
        surface = overrides.surface ?: base.surface,
        surfaceBright = overrides.surfaceBright ?: base.surfaceBright,
        background = overrides.background ?: base.background,
        surfaceTint = overrides.surfaceTint ?: base.surfaceTint,
        onSurface = overrides.onSurface ?: base.onSurface,
        outline = overrides.outline ?: base.outline
    )
}
