package com.paraskcd.spotlightsearch.sources.domain.model

data class QuickSearchPreference(
    val packageName: String,
    val enabled: Boolean,
    val sortOrder: Int
)
