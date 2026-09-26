package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.fadingEdges
import com.paraskcd.spotlightsearch.search.domain.model.SearchSection
import com.paraskcd.spotlightsearch.search.presentation.model.HitCallbacks
import com.paraskcd.spotlightsearch.search.presentation.model.IconSources
import com.paraskcd.spotlightsearch.search.presentation.utils.SearchMetrics

@Composable
fun SearchResultsPanel(
    sections: List<SearchSection>,
    loading: Boolean,
    blurEnabled: Boolean,
    icons: IconSources,
    callbacks: HitCallbacks
) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fadingEdges(listState),
        state = listState,
        reverseLayout = true,
        contentPadding = PaddingValues(SearchMetrics.ListPadding)
    ) {
        sections.forEach { section ->
            val count = section.hits.size
            for (index in count - 1 downTo 0) {
                val hit = section.hits[index]
                item(key = "${section.kind}:$index", contentType = hit::class) {
                    GlassRow(index = index, count = count, blurEnabled = blurEnabled) {
                        HitContent(hit, icons, callbacks)
                    }
                }
            }
            item(key = "${section.kind}:header", contentType = SectionKindHeader) {
                SectionHeader(section.kind)
            }
        }
        if (loading) {
            val count = SearchMetrics.LoadingMoreRows
            for (index in count - 1 downTo 0) {
                item(key = "loading:$index", contentType = LoadingRow) {
                    GlassRow(index = index, count = count, blurEnabled = blurEnabled) { SkeletonRow() }
                }
            }
        }
    }
}

private const val SectionKindHeader = "header"
private const val LoadingRow = "loading"
