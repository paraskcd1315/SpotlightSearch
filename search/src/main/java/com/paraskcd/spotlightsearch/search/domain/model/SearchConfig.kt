package com.paraskcd.spotlightsearch.search.domain.model

data class SearchConfig(
    val appsEnabled: Boolean = true,
    val contactsEnabled: Boolean = true,
    val webSuggestionsEnabled: Boolean = true
)
