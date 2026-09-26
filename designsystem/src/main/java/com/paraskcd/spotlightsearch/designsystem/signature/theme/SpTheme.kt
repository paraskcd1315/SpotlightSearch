package com.paraskcd.spotlightsearch.designsystem.signature.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

object SpTheme {
    val colors: SpColors
        @Composable @ReadOnlyComposable get() = LocalSpColors.current

    val gradients: SpGradients
        @Composable @ReadOnlyComposable get() = LocalSpGradients.current

    val ramp: BrandRamp
        @Composable @ReadOnlyComposable get() = LocalSpBrandRamp.current
}
