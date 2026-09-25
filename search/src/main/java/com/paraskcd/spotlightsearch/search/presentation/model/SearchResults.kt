package com.paraskcd.spotlightsearch.search.presentation.model

import com.paraskcd.spotlightsearch.search.domain.model.SearchSection

data class SearchResults(
    val sections: List<SearchSection> = emptyList(),
    val loading: Boolean = true
)
