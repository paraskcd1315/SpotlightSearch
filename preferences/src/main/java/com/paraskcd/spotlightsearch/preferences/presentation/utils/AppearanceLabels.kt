package com.paraskcd.spotlightsearch.preferences.presentation.utils

import androidx.annotation.StringRes
import com.paraskcd.spotlightsearch.preferences.R
import com.paraskcd.spotlightsearch.preferences.domain.model.GlassStrength
import com.paraskcd.spotlightsearch.preferences.domain.model.TextSize
import com.paraskcd.spotlightsearch.search.domain.model.AppResultsLayout

@StringRes
fun GlassStrength.labelRes(): Int = when (this) {
    GlassStrength.LIGHT -> R.string.glass_light
    GlassStrength.MEDIUM -> R.string.glass_medium
    GlassStrength.STRONG -> R.string.glass_strong
}

@StringRes
fun TextSize.labelRes(): Int = when (this) {
    TextSize.SMALL -> R.string.text_small
    TextSize.DEFAULT -> R.string.text_default
    TextSize.LARGE -> R.string.text_large
}

@StringRes
fun AppResultsLayout.labelRes(): Int = when (this) {
    AppResultsLayout.LIST -> R.string.layout_list
    AppResultsLayout.GRID -> R.string.layout_grid
}
