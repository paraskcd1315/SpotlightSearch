package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.paraskcd.spotlightsearch.preferences.presentation.utils.SettingsMetrics

@Composable
fun ValueText(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = SettingsMetrics.SectionTitleAlpha)
    )
}
