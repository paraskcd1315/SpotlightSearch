package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.DsMetrics
import com.paraskcd.spotlightsearch.search.domain.model.SectionKind
import com.paraskcd.spotlightsearch.search.presentation.utils.SearchMetrics
import com.paraskcd.spotlightsearch.search.presentation.utils.surfaceAlpha

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FrequentAppsCard(
    blurEnabled: Boolean,
    modifier: Modifier = Modifier,
    tiles: @Composable FlowRowScope.() -> Unit
) {
    val shape = RoundedCornerShape(DsMetrics.CornerLarge)
    Surface(
        color = MaterialTheme.colorScheme.surfaceBright.copy(alpha = surfaceAlpha(blurEnabled)),
        shape = shape,
        modifier = modifier
            .fillMaxWidth()
            .border(
                DsMetrics.OutlineWidth,
                MaterialTheme.colorScheme.outline.copy(alpha = DsMetrics.OutlineAlpha),
                shape
            )
    ) {
        Column(modifier = Modifier.padding(bottom = SearchMetrics.ListPadding)) {
            SectionHeader(
                SectionKind.FREQUENT,
                PaddingValues(
                    start = SearchMetrics.SectionHeaderPadding,
                    top = SearchMetrics.SectionHeaderPadding,
                    end = SearchMetrics.SectionHeaderPadding
                )
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = SearchMetrics.TilesPerRow,
                horizontalArrangement = Arrangement.SpaceEvenly,
                content = tiles
            )
        }
    }
}
