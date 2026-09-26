package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Settings
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme
import com.paraskcd.spotlightsearch.search.R
import com.paraskcd.spotlightsearch.search.presentation.overlay.OverlayMetrics

@Composable
fun SettingsPill() {
    val colors = SpTheme.colors
    Row(
        modifier = Modifier
            .heightIn(min = OverlayMetrics.SettingsButtonMinHeight)
            .padding(
                horizontal = OverlayMetrics.SettingsButtonPaddingHorizontal,
                vertical = OverlayMetrics.SettingsButtonPaddingVertical
            ),
        horizontalArrangement = Arrangement.spacedBy(OverlayMetrics.SettingsButtonIconSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Lucide.Settings,
            contentDescription = null,
            tint = colors.textPrimary,
            modifier = Modifier.size(OverlayMetrics.SettingsButtonIconSize)
        )
        Text(
            stringResource(R.string.search_open_settings),
            color = colors.textPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = SpMetrics.settingsItemTextSize
        )
    }
}
