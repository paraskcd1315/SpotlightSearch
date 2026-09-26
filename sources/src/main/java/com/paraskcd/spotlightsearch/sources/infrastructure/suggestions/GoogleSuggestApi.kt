package com.paraskcd.spotlightsearch.sources.infrastructure.suggestions

import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.resume

class GoogleSuggestApi @Inject constructor(private val client: OkHttpClient) {
    suspend fun fetch(query: String): List<String> {
        val url = SUGGEST_URL.toHttpUrl().newBuilder()
            .addQueryParameter("client", "firefox")
            .addQueryParameter("q", query)
            .build()
        val call = client.newCall(Request.Builder().url(url).build())

        val body = suspendCancellableCoroutine<String?> { continuation ->
            continuation.invokeOnCancellation { call.cancel() }
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) = continuation.resume(null)
                override fun onResponse(call: Call, response: Response) {
                    continuation.resume(response.use { if (it.isSuccessful) it.body.string() else null })
                }
            })
        } ?: return emptyList()

        return runCatching {
            val suggestions = JSONArray(body).getJSONArray(1)
            List(suggestions.length()) { suggestions.getString(it) }.take(MAX_SUGGESTIONS)
        }.getOrDefault(emptyList())
    }

    private companion object {
        const val SUGGEST_URL = "https://suggestqueries.google.com/complete/search"
        const val MAX_SUGGESTIONS = 5
    }
}
