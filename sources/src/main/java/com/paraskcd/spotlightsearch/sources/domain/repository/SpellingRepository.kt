package com.paraskcd.spotlightsearch.sources.domain.repository

interface SpellingRepository {
    fun warmUp()
    suspend fun correction(query: String): String?
}
