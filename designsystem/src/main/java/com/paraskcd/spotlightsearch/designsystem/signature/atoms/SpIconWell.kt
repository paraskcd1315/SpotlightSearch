package com.paraskcd.spotlightsearch.designsystem.signature.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme

@Composable
fun SpIconWell(icon: ImageVector, modifier: Modifier = Modifier, tint: Color = SpTheme.colors.brandText) {
    Box(
        modifier = modifier
            .size(SpMetrics.settingsIconWellSize)
            .clip(CircleShape)
            .background(tint.copy(alpha = SpMetrics.settingsIconWellAlpha)),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(SpMetrics.settingsIconSize))
    }
}
