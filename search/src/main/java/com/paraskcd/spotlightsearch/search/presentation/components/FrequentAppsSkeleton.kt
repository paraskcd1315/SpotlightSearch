package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paraskcd.spotlightsearch.search.presentation.utils.SearchMetrics

@Composable
fun FrequentAppsSkeleton(blurEnabled: Boolean, modifier: Modifier = Modifier) {
    FrequentAppsCard(blurEnabled, modifier) {
        repeat(SearchMetrics.TilesPerRow) { SkeletonTile() }
    }
}
