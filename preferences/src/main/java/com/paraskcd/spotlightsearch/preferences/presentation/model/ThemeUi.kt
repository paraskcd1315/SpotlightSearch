package com.paraskcd.spotlightsearch.preferences.presentation.model

import androidx.compose.ui.graphics.Color
import com.paraskcd.spotlightsearch.designsystem.theme.ColorOverrides
import com.paraskcd.spotlightsearch.preferences.domain.model.ColorOverrideKey
import com.paraskcd.spotlightsearch.preferences.domain.model.ThemeMode

data class ThemeUi(
    val mode: ThemeMode = ThemeMode.AUTO,
    val enableBlur: Boolean = true,
    val showBranding: Boolean = true,
    val colors: Map<ColorOverrideKey, Color> = emptyMap()
) {
    val colorOverrides: ColorOverrides
        get() = ColorOverrides(
            surfaceBright = colors[ColorOverrideKey.surfaceBright],
            background = colors[ColorOverrideKey.background],
            surfaceTint = colors[ColorOverrideKey.surfaceTint],
            onSurface = colors[ColorOverrideKey.onSurface],
            outline = colors[ColorOverrideKey.outline]
        )

    fun isDark(systemDark: Boolean): Boolean = when (mode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.AUTO -> systemDark
    }
}
