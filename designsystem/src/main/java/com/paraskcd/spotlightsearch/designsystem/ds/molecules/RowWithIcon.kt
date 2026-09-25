package com.paraskcd.spotlightsearch.designsystem.ds.molecules

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.DsMetrics

@Composable
fun RowWithIcon(
    text: String,
    icon: ImageVector? = null,
    iconPainter: Painter? = null,
    subtext: String? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(DsMetrics.IconSpacing)
    ) {
        icon?.let {
            Surface(
                modifier = Modifier
                    .padding(end = DsMetrics.IconSpacing)
                    .size(DsMetrics.IconCircleSize),
                color = MaterialTheme.colorScheme.surfaceTint.copy(alpha = DsMetrics.TintAlpha),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = it,
                    contentDescription = text,
                    modifier = Modifier
                        .padding(DsMetrics.IconCirclePadding)
                        .fillMaxSize()
                )
            }
        }
        iconPainter?.let {
            Image(
                painter = it,
                contentDescription = text,
                modifier = Modifier
                    .size(DsMetrics.IconTileSize)
                    .padding(DsMetrics.IconTilePadding)
                    .border(
                        width = DsMetrics.OutlineWidth,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = DsMetrics.OutlineAlpha),
                        shape = CircleShape
                    )
                    .clip(CircleShape)
            )
        }
        Column {
            Text(text, style = MaterialTheme.typography.titleMedium)
            subtext?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
        }
    }
}
