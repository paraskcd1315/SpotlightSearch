package com.paraskcd.spotlightsearch.designsystem.signature.organisms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.paraskcd.spotlightsearch.designsystem.signature.foundation.SpMetrics
import com.paraskcd.spotlightsearch.designsystem.signature.molecules.SpGroupedRow

@Composable
fun SpGroupedList(
    count: Int,
    modifier: Modifier = Modifier,
    inset: Dp = SpMetrics.groupedRowInset,
    row: @Composable (Int) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        repeat(count) { index ->
            SpGroupedRow(index = index, count = count, inset = inset) { row(index) }
        }
    }
}
