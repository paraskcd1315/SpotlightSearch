package com.paraskcd.spotlightsearch.ui.theme

import androidx.compose.ui.graphics.Color
import com.paraskcd.spotlightsearch.designsystem.theme.ColorOverrides
import com.paraskcd.spotlightsearch.enums.ThemeMode

data class ThemeUi(
    val mode: ThemeMode,
    val enableBlur: Boolean,
    val surface: Color? = null,
    val surfaceBright: Color? = null,
    val background: Color? = null,
    val surfaceTint: Color? = null,
    val onSurface: Color? = null,
    val outline: Color? = null
) {
    val colorOverrides: ColorOverrides
        get() = ColorOverrides(
            surface = surface,
            surfaceBright = surfaceBright,
            background = background,
            surfaceTint = surfaceTint,
            onSurface = onSurface,
            outline = outline
        )

    fun isDark(systemDark: Boolean): Boolean = when (mode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.AUTO -> systemDark
    }
}
