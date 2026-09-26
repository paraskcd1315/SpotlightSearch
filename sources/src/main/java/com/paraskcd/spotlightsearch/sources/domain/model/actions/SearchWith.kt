package com.paraskcd.spotlightsearch.sources.domain.model.actions

import com.paraskcd.spotlightsearch.sources.domain.model.QuickSearchService

data class SearchWith(val service: QuickSearchService, val query: String) : HitAction
