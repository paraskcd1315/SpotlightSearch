package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paraskcd.spotlightsearch.search.presentation.utils.SearchMetrics

@Composable
fun ResultsSkeleton(blurEnabled: Boolean) {
    Column(modifier = Modifier.padding(SearchMetrics.ListPadding)) {
        repeat(SearchMetrics.SkeletonRows) { index ->
            GlassRow(index = index, count = SearchMetrics.SkeletonRows, blurEnabled = blurEnabled) {
                SkeletonRow()
            }
        }
    }
}
