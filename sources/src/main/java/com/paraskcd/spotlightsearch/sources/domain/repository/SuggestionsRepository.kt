package com.paraskcd.spotlightsearch.sources.domain.repository

interface SuggestionsRepository {
    suspend fun suggest(query: String): List<String>
}
