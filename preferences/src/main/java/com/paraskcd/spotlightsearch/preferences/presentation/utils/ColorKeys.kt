package com.paraskcd.spotlightsearch.preferences.presentation.utils

import androidx.annotation.StringRes
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.domain.model.ColorOverrideKey

@StringRes
fun ColorOverrideKey.titleRes(): Int = when (this) {
    ColorOverrideKey.background -> R.string.color_background
    ColorOverrideKey.surfaceBright -> R.string.color_surface_bright
    ColorOverrideKey.surfaceTint -> R.string.color_surface_tint
    ColorOverrideKey.onSurface -> R.string.color_on_surface
    ColorOverrideKey.outline -> R.string.color_outline
}

fun ColorOverrideKey.schemeColor(scheme: ColorScheme): Color = when (this) {
    ColorOverrideKey.background -> scheme.background
    ColorOverrideKey.surfaceBright -> scheme.surfaceBright
    ColorOverrideKey.surfaceTint -> scheme.surfaceTint
    ColorOverrideKey.onSurface -> scheme.onSurface
    ColorOverrideKey.outline -> scheme.outline
}

fun ColorOverrideKey.swatchFallback(scheme: ColorScheme): Color = when (this) {
    ColorOverrideKey.surfaceBright -> scheme.surfaceVariant
    else -> schemeColor(scheme)
}

fun ColorOverrideKey.applyTo(scheme: ColorScheme, color: Color): ColorScheme = when (this) {
    ColorOverrideKey.background -> scheme.copy(background = color)
    ColorOverrideKey.surfaceBright -> scheme.copy(surfaceBright = color)
    ColorOverrideKey.surfaceTint -> scheme.copy(surfaceTint = color)
    ColorOverrideKey.onSurface -> scheme.copy(onSurface = color)
    ColorOverrideKey.outline -> scheme.copy(outline = color)
}
