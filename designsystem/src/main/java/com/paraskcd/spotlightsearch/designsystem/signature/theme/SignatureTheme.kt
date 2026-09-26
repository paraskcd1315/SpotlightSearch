package com.paraskcd.spotlightsearch.designsystem.signature.theme

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun SignatureTheme(
    seed: Color,
    darkTheme: Boolean,
    adjust: (SpColors) -> SpColors = { it },
    content: @Composable () -> Unit
) {
    val ramp = remember(seed) { brandRampOf(seed) }
    val surfaceRamp = remember(seed) { brandRampOf(seed, SurfaceRampMaxSaturation) }
    val colors = remember(ramp, darkTheme, adjust) {
        adjust(if (darkTheme) spDarkColors(ramp) else spLightColors(ramp))
    }
    val gradients = remember(ramp, surfaceRamp) { spGradients(ramp, surfaceRamp) }
    val scheme = remember(colors) {
        val base = if (colors.isLight) lightColorScheme() else darkColorScheme()
        base.copy(
            primary = colors.brand,
            onPrimary = Color.White,
            surfaceTint = colors.brand,
            background = colors.bgBase,
            surface = colors.surface,
            surfaceBright = colors.surfaceContainerHigh,
            surfaceContainer = colors.surfaceContainer,
            surfaceContainerHigh = colors.surfaceContainerHigh,
            onSurface = colors.textPrimary,
            onBackground = colors.textPrimary,
            onSurfaceVariant = colors.textSecondary,
            outline = colors.border,
            error = colors.danger
        )
    }
    CompositionLocalProvider(
        LocalSpColors provides colors,
        LocalSpGradients provides gradients,
        LocalSpBrandRamp provides ramp
    ) {
        MaterialTheme(colorScheme = scheme, typography = SpTypography) {
            CompositionLocalProvider(LocalTextStyle provides TextStyle(fontFamily = Quicksand)) {
                content()
            }
        }
    }
}
