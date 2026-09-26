package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    callbacks: HitCallbacks,
    scrollKey: Any?,
    onShowAll: (SearchSection) -> Unit
) {
    val listState = rememberLazyListState()
    LaunchedEffect(scrollKey) { listState.scrollToItem(0) }
    val single = sections.size == 1
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fadingEdges(listState),
        state = listState,
        reverseLayout = true,
        contentPadding = PaddingValues(SearchMetrics.ListPadding)
    ) {
        sections.forEach { section ->
            val kind = section.kind
            val capped = !single && section.hits.size > SearchMetrics.SectionCap
            val shown = if (capped) section.hits.take(SearchMetrics.SectionCap) else section.hits
            if (capped) {
                item(key = "$kind:toggle", contentType = ToggleRow) {
                    SectionToggle(total = section.hits.size) { onShowAll(section) }
                }
            }
            val count = shown.size
            for (index in count - 1 downTo 0) {
                val hit = shown[index]
                item(key = "$kind:$index", contentType = hit::class) {
                    GlassRow(index = index, count = count, blurEnabled = blurEnabled) {
                        HitContent(hit, icons, callbacks)
                    }
                }
            }
            item(key = "$kind:header", contentType = HeaderRow) {
                SectionHeader(kind)
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

private const val HeaderRow = "header"
private const val ToggleRow = "toggle"
private const val LoadingRow = "loading"
