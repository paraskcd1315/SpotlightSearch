package com.paraskcd.spotlightsearch.sources.data

import com.paraskcd.spotlightsearch.sources.domain.repository.SuggestionsRepository
import com.paraskcd.spotlightsearch.sources.infrastructure.suggestions.GoogleSuggestApi
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SuggestionsRepositoryImpl @Inject constructor(
    private val api: GoogleSuggestApi
) : SuggestionsRepository {
    override suspend fun suggest(query: String): List<String> {
        if (query.isBlank()) return emptyList()
        delay(TYPING_PAUSE_MS)
        return api.fetch(query)
    }

    private companion object {
        const val TYPING_PAUSE_MS = 250L
    }
}
