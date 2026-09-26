package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.DsMetrics
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.groupShape
import com.paraskcd.spotlightsearch.search.presentation.utils.SearchMetrics
import com.paraskcd.spotlightsearch.search.presentation.utils.surfaceAlpha

@Composable
fun ResultsSkeleton(blurEnabled: Boolean) {
    Column(modifier = Modifier.padding(SearchMetrics.ListPadding)) {
        repeat(SearchMetrics.SkeletonRows) { index ->
            val shape = groupShape(index, SearchMetrics.SkeletonRows)
            Surface(
                color = MaterialTheme.colorScheme.surfaceBright.copy(alpha = surfaceAlpha(blurEnabled)),
                shape = shape,
                modifier = Modifier
                    .padding(vertical = SearchMetrics.RowGap)
                    .fillMaxWidth()
                    .border(
                        DsMetrics.OutlineWidth,
                        MaterialTheme.colorScheme.outline.copy(alpha = DsMetrics.OutlineAlpha),
                        shape
                    )
            ) {
                SkeletonRow()
            }
        }
    }
}
