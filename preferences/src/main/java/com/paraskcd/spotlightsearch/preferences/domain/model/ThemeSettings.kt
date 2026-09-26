package com.paraskcd.spotlightsearch.preferences.domain.model

data class ThemeSettings(
    val mode: ThemeMode = ThemeMode.AUTO,
    val blurEnabled: Boolean = true,
    val colors: Map<ColorOverrideKey, Int> = emptyMap()
)
