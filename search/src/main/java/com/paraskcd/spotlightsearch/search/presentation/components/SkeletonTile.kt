package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.paraskcd.spotlightsearch.designsystem.ds.atoms.SkeletonBlock
import com.paraskcd.spotlightsearch.search.presentation.utils.SearchMetrics

@Composable
fun SkeletonTile() {
    Column(
        modifier = Modifier
            .width(SearchMetrics.TileWidth)
            .padding(SearchMetrics.TilePadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(SearchMetrics.TileSpacing)
    ) {
        SkeletonBlock(
            modifier = Modifier
                .padding(SearchMetrics.TileIconInset)
                .size(SearchMetrics.TileIconSize),
            shape = CircleShape
        )
        SkeletonBlock(
            modifier = Modifier
                .fillMaxWidth(SearchMetrics.SkeletonTileLabelFraction)
                .height(SearchMetrics.SkeletonSubtitleHeight)
        )
    }
}
