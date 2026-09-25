package com.paraskcd.spotlightsearch.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ColorOverrides(
    val surface: Color? = null,
    val surfaceBright: Color? = null,
    val background: Color? = null,
    val surfaceTint: Color? = null,
    val onSurface: Color? = null,
    val outline: Color? = null
)
