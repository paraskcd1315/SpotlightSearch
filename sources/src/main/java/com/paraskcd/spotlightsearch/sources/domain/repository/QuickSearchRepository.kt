package com.paraskcd.spotlightsearch.sources.domain.repository

import com.paraskcd.spotlightsearch.sources.domain.model.hits.QuickSearchHit

interface QuickSearchRepository {
    suspend fun targets(query: String): List<QuickSearchHit>
}
