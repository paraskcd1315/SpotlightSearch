package com.paraskcd.spotlightsearch.designsystem.signature.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.Dp
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.theme.SpTheme

@Composable
fun SpIconDisc(
    icon: ImageVector,
    size: Dp,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = SpTheme.colors.brandText,
    background: Color = SpTheme.colors.glassStrongBg,
    ring: Color = Color(SpMetrics.iconDiscRingArgb)
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(background)
            .border(size * SpMetrics.iconDiscRingFraction, ring, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(size * SpMetrics.iconDiscGlyphFraction)
        )
    }
}
