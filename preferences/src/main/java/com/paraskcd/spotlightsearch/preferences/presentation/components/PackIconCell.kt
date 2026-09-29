package com.paraskcd.spotlightsearch.preferences.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.clickableQuiet
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme
import com.paraskcd.spotlightsearch.preferences.presentation.utils.SettingsMetrics
import com.paraskcd.spotlightsearch.search.infrastructure.icons.AppIconLoader

@Composable
fun PackIconCell(
    iconPack: String,
    drawable: String,
    icons: AppIconLoader,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bitmap by produceState(icons.cachedPackIcon(iconPack, drawable), iconPack, drawable) {
        value = icons.loadPackIcon(iconPack, drawable)
    }
    Box(
        modifier = modifier
            .size(SpMetrics.touchTargetMin)
            .clip(CircleShape)
            .then(
                if (selected) Modifier.border(SettingsMetrics.PackIconSelectedBorder, SpTheme.colors.brandText, CircleShape)
                else Modifier
            )
            .clickableQuiet(onClick),
        contentAlignment = Alignment.Center
    ) {
        bitmap?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = drawable, modifier = Modifier.size(SettingsMetrics.PackIconSize))
        }
    }
}
