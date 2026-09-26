package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.paraskcd.spotlightsearch.search.R
import com.paraskcd.spotlightsearch.search.presentation.overlay.OverlayMetrics

@Composable
fun SettingsPill() {
    Row(
        modifier = Modifier.padding(
            horizontal = OverlayMetrics.SettingsButtonPaddingHorizontal,
            vertical = OverlayMetrics.SettingsButtonPaddingVertical
        ),
        horizontalArrangement = Arrangement.spacedBy(OverlayMetrics.SettingsButtonIconSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Settings,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(OverlayMetrics.SettingsButtonIconSize)
        )
        Text(
            stringResource(R.string.search_open_settings),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
