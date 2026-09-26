package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpGlass
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpShapes
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme
import com.paraskcd.spotlightsearch.preferences.presentation.utils.SettingsMetrics

@Composable
fun ColorSwatch(color: Color) {
    Box(
        Modifier
            .size(width = SettingsMetrics.SwatchWidth, height = SettingsMetrics.SwatchHeight)
            .clip(SpShapes.sm)
            .background(color)
            .border(SpGlass.borderWidth, SpTheme.colors.border, SpShapes.sm)
    )
}
