package com.paraskcd.spotlightsearch.designsystem.signature.theme

import androidx.compose.runtime.staticCompositionLocalOf

private val DefaultRamp = brandRampOf(SpBrandSeed)

val LocalSpColors = staticCompositionLocalOf { spDarkColors(DefaultRamp) }
val LocalSpGradients = staticCompositionLocalOf { spGradients(DefaultRamp, DefaultRamp) }
val LocalSpBrandRamp = staticCompositionLocalOf { DefaultRamp }
