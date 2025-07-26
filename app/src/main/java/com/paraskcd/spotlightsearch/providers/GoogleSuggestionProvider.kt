package com.paraskcd.spotlightsearch.providers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.net.URLEncoder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleSuggestionProvider @Inject constructor() {
    private val client = OkHttpClient()

    suspend fun fetchSuggestions(query: String): List<String> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()

        delay(250)

        val encoded = URLEncoder.encode(query, "UTF-8")
        val url = "https://suggestqueries.google.com/complete/search?client=firefox&q=$encoded"

        return@withContext try {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val body = response.body?.string() ?: return@withContext emptyList()
            val array = JSONArray(body)
            val suggestions = array.getJSONArray(1)
            List(5) { suggestions.getString(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }

}