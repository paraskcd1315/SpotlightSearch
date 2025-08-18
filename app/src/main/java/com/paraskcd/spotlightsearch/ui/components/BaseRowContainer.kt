package com.paraskcd.spotlightsearch.ui.components

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

@Composable
fun BaseRowContainer(
    shape: RoundedCornerShape,
    isDragging: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        tonalElevation = if (isDragging) 4.dp else 0.dp,
        onClick = { onClick?.invoke() },
        shape = shape,
        color = MaterialTheme.colorScheme.surfaceBright,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}