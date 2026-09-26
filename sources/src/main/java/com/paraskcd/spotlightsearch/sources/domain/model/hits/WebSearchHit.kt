package com.paraskcd.spotlightsearch.sources.domain.model.hits

import com.paraskcd.spotlightsearch.sources.domain.model.WebSearchEngine

data class WebSearchHit(val query: String, val engine: WebSearchEngine = WebSearchEngine.SYSTEM) : SearchHit
