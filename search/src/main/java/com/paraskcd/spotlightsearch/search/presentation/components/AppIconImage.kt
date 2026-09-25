package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.DsMetrics
import com.paraskcd.spotlightsearch.search.infrastructure.icons.AppIconLoader

@Composable
fun AppIconImage(
    packageName: String,
    loader: AppIconLoader,
    themed: Boolean,
    size: Dp,
    modifier: Modifier = Modifier
) {
    val tint = if (themed) MaterialTheme.colorScheme.primary.toArgb() else null
    val bitmap by produceState(loader.cached(packageName, tint), packageName, tint) {
        value = loader.load(packageName, tint)
    }
    Box(
        modifier = modifier
            .size(size)
            .border(
                width = DsMetrics.OutlineWidth,
                color = MaterialTheme.colorScheme.outline.copy(alpha = DsMetrics.OutlineAlpha),
                shape = CircleShape
            )
            .clip(CircleShape)
    ) {
        bitmap?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = null, modifier = Modifier.size(size))
        }
    }
}
