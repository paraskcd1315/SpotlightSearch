package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paraskcd.spotlightsearch.preferences.presentation.utils.SettingsMetrics

@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier.padding(SettingsMetrics.SectionTitlePadding)) {
    Text(
        text,
        modifier = modifier,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = SettingsMetrics.SectionTitleAlpha)
    )
}
