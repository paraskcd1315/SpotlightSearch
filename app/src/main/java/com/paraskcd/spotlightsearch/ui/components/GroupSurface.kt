package com.paraskcd.spotlightsearch.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun GroupSurface(
    count: Int,
    content: @Composable (index: Int, shape: RoundedCornerShape) -> Unit
) {
    Column {
        repeat(count) { i ->
            val shape = when {
                count == 1 -> RoundedCornerShape(24.dp)
                i == 0 -> RoundedCornerShape(
                    topStart = 24.dp, topEnd = 24.dp,
                    bottomStart = 8.dp, bottomEnd = 8.dp
                )
                i == count - 1 -> RoundedCornerShape(
                    topStart = 8.dp, topEnd = 8.dp,
                    bottomStart = 24.dp, bottomEnd = 24.dp
                )
                else -> RoundedCornerShape(8.dp)
            }
            content(i, shape)
        }
    }
}