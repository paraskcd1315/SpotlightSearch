package com.paraskcd.spotlightsearch.ui.components

import androidx.compose.runtime.Composable
import com.paraskcd.spotlightsearch.types.SearchResult
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchResultList(results: List<SearchResult>) {
    LazyColumn {
        items(results) { result ->
            SearchResultItem(result = result)
        }
        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}