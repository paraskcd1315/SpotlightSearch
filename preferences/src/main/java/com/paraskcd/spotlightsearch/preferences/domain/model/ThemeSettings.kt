package com.paraskcd.spotlightsearch.preferences.domain.model

import com.paraskcd.spotlightsearch.search.domain.model.AppResultsLayout

data class ThemeSettings(
    val mode: ThemeMode = ThemeMode.AUTO,
    val blurEnabled: Boolean = true,
    val showBranding: Boolean = true,
    val glassStrength: GlassStrength = GlassStrength.MEDIUM,
    val textSize: TextSize = TextSize.DEFAULT,
    val appLayout: AppResultsLayout = AppResultsLayout.LIST,
    val colors: Map<ColorOverrideKey, Int> = emptyMap()
)
