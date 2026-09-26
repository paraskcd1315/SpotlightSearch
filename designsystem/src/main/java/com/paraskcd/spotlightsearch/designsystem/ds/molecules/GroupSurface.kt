package com.paraskcd.spotlightsearch.designsystem.ds.molecules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.groupShape

@Composable
fun GroupSurface(
    count: Int,
    content: @Composable (index: Int, shape: RoundedCornerShape) -> Unit
) {
    Column {
        repeat(count) { index ->
            content(index, groupShape(index, count))
        }
    }
}
