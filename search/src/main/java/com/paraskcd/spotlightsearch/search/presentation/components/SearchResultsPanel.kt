package com.paraskcd.spotlightsearch.search.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    blurEnabled: Boolean,
    icons: IconSources,
    callbacks: HitCallbacks
) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .fadingEdges(listState),
        state = listState,
        reverseLayout = true,
        contentPadding = PaddingValues(SearchMetrics.ListPadding)
    ) {
        items(sections, key = { it.kind }) { section ->
            SectionBlock(section, blurEnabled, icons, callbacks)
        }
    }
}
