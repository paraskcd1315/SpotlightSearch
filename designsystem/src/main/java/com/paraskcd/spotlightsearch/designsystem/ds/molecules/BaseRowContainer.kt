package com.paraskcd.spotlightsearch.designsystem.ds.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.DsMetrics

@Composable
fun BaseRowContainer(
    shape: RoundedCornerShape,
    isDragging: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        tonalElevation = if (isDragging) DsMetrics.DraggingElevation else 0.dp,
        onClick = { onClick?.invoke() },
        shape = shape,
        color = MaterialTheme.colorScheme.surfaceBright,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DsMetrics.RowHorizontalPadding, vertical = DsMetrics.RowGap)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DsMetrics.RowContentPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}
