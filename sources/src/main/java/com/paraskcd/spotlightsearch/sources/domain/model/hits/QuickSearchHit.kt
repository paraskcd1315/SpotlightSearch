package com.paraskcd.spotlightsearch.sources.domain.model.hits

import com.paraskcd.spotlightsearch.sources.domain.model.QuickSearchService

data class QuickSearchHit(val service: QuickSearchService, val query: String) : SearchHit
