package com.paraskcd.spotlightsearch.designsystem.ds.atoms

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.DsMetrics

@Composable
fun IconCircle(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    size: Dp = DsMetrics.IconCircleSize,
    padding: Dp = DsMetrics.IconCirclePadding
) {
    Surface(
        modifier = modifier
            .size(size)
            .border(
                width = DsMetrics.OutlineWidth,
                color = MaterialTheme.colorScheme.outline.copy(alpha = DsMetrics.OutlineAlpha),
                shape = CircleShape
            )
            .clip(CircleShape),
        color = MaterialTheme.colorScheme.surfaceTint.copy(alpha = DsMetrics.TintAlpha)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        )
    }
}
