package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.DsMetrics
import com.paraskcd.spotlightsearch.preferences.presentation.utils.SettingsMetrics

@Composable
fun ColorSwatch(color: Color) {
    Box(
        Modifier
            .size(width = SettingsMetrics.SwatchWidth, height = SettingsMetrics.SwatchHeight)
            .clip(RoundedCornerShape(DsMetrics.CornerSmall))
            .background(color)
    )
}
