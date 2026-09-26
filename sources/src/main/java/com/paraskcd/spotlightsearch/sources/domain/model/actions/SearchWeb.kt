package com.paraskcd.spotlightsearch.sources.domain.model.actions

import com.paraskcd.spotlightsearch.sources.domain.model.WebSearchEngine

data class SearchWeb(val query: String, val engine: WebSearchEngine = WebSearchEngine.SYSTEM) : HitAction
