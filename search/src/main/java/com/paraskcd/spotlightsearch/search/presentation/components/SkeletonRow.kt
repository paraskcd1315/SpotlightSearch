package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.paraskcd.spotlightsearch.designsystem.ds.atoms.SkeletonBlock
import com.paraskcd.spotlightsearch.search.presentation.utils.SearchMetrics

@Composable
fun SkeletonRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SearchMetrics.RowPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SearchMetrics.RowIconSpacing)
    ) {
        SkeletonBlock(modifier = Modifier.size(SearchMetrics.RowIconSize), shape = CircleShape)
        Column(verticalArrangement = Arrangement.spacedBy(SearchMetrics.SkeletonLineSpacing)) {
            SkeletonBlock(
                modifier = Modifier
                    .fillMaxWidth(SearchMetrics.SkeletonTitleFraction)
                    .height(SearchMetrics.SkeletonTitleHeight)
            )
            SkeletonBlock(
                modifier = Modifier
                    .fillMaxWidth(SearchMetrics.SkeletonSubtitleFraction)
                    .height(SearchMetrics.SkeletonSubtitleHeight)
            )
        }
    }
}
