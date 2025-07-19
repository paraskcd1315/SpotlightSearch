package com.paraskcd.spotlightsearch.ui.components

import androidx.compose.runtime.Composable
import com.paraskcd.spotlightsearch.types.SearchResult
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@Composable
fun SearchResultList(results: List<SearchResult>) {
    LazyColumn {
        items(results, key = { it.hashCode() }) { result ->
            SearchResultItem(result = result)
        }
    }
}