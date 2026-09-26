package com.paraskcd.spotlightsearch.preferences.presentation.utils

import com.paraskcd.spotlightsearch.preferences.domain.model.GlassStrength
import com.paraskcd.spotlightsearch.preferences.domain.model.TextSize

fun GlassStrength.panelAlpha(): Float = when (this) {
    GlassStrength.LIGHT -> SettingsMetrics.GlassLightAlpha
    GlassStrength.MEDIUM -> SettingsMetrics.GlassMediumAlpha
    GlassStrength.STRONG -> SettingsMetrics.GlassStrongAlpha
}

fun TextSize.scale(): Float = when (this) {
    TextSize.SMALL -> SettingsMetrics.TextSmallScale
    TextSize.DEFAULT -> SettingsMetrics.TextDefaultScale
    TextSize.LARGE -> SettingsMetrics.TextLargeScale
}
