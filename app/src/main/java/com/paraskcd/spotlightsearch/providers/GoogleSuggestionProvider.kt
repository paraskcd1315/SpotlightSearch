package com.paraskcd.spotlightsearch.providers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URLEncoder
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleSuggestionProvider @Inject constructor() {
    suspend fun fetchSuggestions(query: String): List<String> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()

        val encoded = URLEncoder.encode(query, "UTF-8")
        val url = "https://suggestqueries.google.com/complete/search?client=firefox&q=$encoded"

        return@withContext try {
            val response = URL(url).readText()
            val array = JSONArray(response)
            val suggestions = array.getJSONArray(1)
            List(suggestions.length()) { suggestions.getString(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}