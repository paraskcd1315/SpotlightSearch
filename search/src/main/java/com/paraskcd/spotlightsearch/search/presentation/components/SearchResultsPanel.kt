package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.paraskcd.spotlightsearch.designsystem.ds.foundation.fadingEdges
import com.paraskcd.spotlightsearch.search.domain.model.AppResultsLayout
import com.paraskcd.spotlightsearch.search.domain.model.SearchLimits
import com.paraskcd.spotlightsearch.search.domain.model.SearchSection
import com.paraskcd.spotlightsearch.search.domain.model.SectionKind
import com.paraskcd.spotlightsearch.search.presentation.model.HitCallbacks
import com.paraskcd.spotlightsearch.search.presentation.model.IconSources
import com.paraskcd.spotlightsearch.search.presentation.utils.SearchMetrics
import com.paraskcd.spotlightsearch.sources.domain.model.hits.AppHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.SearchHit

@Composable
fun SearchResultsPanel(
    sections: List<SearchSection>,
    loading: Boolean,
    rowsPerSection: Int,
    appLayout: AppResultsLayout,
    blurEnabled: Boolean,
    icons: IconSources,
    callbacks: HitCallbacks,
    scrollKey: Any?,
    onShowAll: (SearchSection) -> Unit
) {
    val listState = rememberLazyListState()
    var userScrolled by remember { mutableStateOf(false) }
    LaunchedEffect(listState) {
        listState.interactionSource.interactions.collect { if (it is DragInteraction.Start) userScrolled = true }
    }
    LaunchedEffect(listState) {
        snapshotFlow { listState.canScrollBackward }.collect { if (!it) userScrolled = false }
    }
    LaunchedEffect(scrollKey) {
        userScrolled = false
        listState.scrollToItem(0)
    }
    LaunchedEffect(sections) { if (!userScrolled) listState.scrollToItem(0) }
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
            val grid = appLayout == AppResultsLayout.GRID && section.kind == SectionKind.APPS
            val cap = if (grid) gridCap(rowsPerSection) else rowsPerSection
            val capped = !single && section.hits.size > cap
            val shown = if (capped) section.hits.take(cap) else section.hits
            if (capped) {
                item(key = "${section.kind}:toggle", contentType = ToggleRow) {
                    SectionToggle(total = section.hits.size) { onShowAll(section) }
                }
            }
            if (grid) {
                gridRows(section.kind, shown.filterIsInstance<AppHit>(), blurEnabled, icons, callbacks)
            } else {
                listRows(section, shown, blurEnabled, icons, callbacks)
            }
            item(key = "${section.kind}:header", contentType = HeaderRow) {
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

private fun LazyListScope.listRows(
    section: SearchSection,
    shown: List<SearchHit>,
    blurEnabled: Boolean,
    icons: IconSources,
    callbacks: HitCallbacks
) {
    val count = shown.size
    for (index in count - 1 downTo 0) {
        val hit = shown[index]
        item(key = "${section.kind}:$index", contentType = hit::class) {
            GlassRow(index = index, count = count, blurEnabled = blurEnabled) {
                HitContent(hit, icons, callbacks)
            }
        }
    }
}

private fun LazyListScope.gridRows(
    kind: SectionKind,
    apps: List<AppHit>,
    blurEnabled: Boolean,
    icons: IconSources,
    callbacks: HitCallbacks
) {
    val rows = apps.chunked(SearchLimits.APPS_PER_ROW)
    val count = rows.size
    for (index in count - 1 downTo 0) {
        item(key = "$kind:grid:$index", contentType = GridRow) {
            GlassRow(index = index, count = count, blurEnabled = blurEnabled) {
                AppTileRow(rows[index], icons, callbacks)
            }
        }
    }
}

private fun gridCap(rowsPerSection: Int): Int {
    val perRow = SearchLimits.APPS_PER_ROW
    return ((rowsPerSection + perRow - 1) / perRow) * perRow
}

private const val HeaderRow = "header"
private const val ToggleRow = "toggle"
private const val LoadingRow = "loading"
private const val GridRow = "grid"
