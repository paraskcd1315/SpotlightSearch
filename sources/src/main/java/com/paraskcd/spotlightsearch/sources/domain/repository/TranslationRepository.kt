package com.paraskcd.spotlightsearch.sources.domain.repository

import com.paraskcd.spotlightsearch.sources.domain.model.hits.TranslationHit

interface TranslationRepository {
    suspend fun translate(query: String): TranslationHit?
}
