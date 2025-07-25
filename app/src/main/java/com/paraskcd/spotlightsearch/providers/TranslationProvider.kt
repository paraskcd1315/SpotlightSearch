package com.paraskcd.spotlightsearch.providers


import android.util.Log
import com.google.ai.edge.aicore.GenerativeModel
import com.paraskcd.spotlightsearch.enums.SearchResultType
import com.paraskcd.spotlightsearch.types.SearchResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TranslationProvider @Inject constructor() {
    suspend fun translate(query: String, model: GenerativeModel): SearchResult? {
        val prompt = buildPrompt(query) ?: return null

        return try {
            val response = model.generateContent(prompt)
            val translated = response.text?.trim()
            if (!translated.isNullOrBlank()) {
                SearchResult(
                    title = translated,
                    subtitle = "Translated result",
                    searchResultType = SearchResultType.TRANSLATOR,
                    onClick = {}
                )
            } else null
        } catch (e: Exception) {
            Log.e("TranslationProvider", "Translation failed", e)
            null
        }
    }

    private fun buildPrompt(query: String): String? {
        val regex = Regex("translate (.+?) (into|to|en|a) ([a-zA-Z]+)", RegexOption.IGNORE_CASE)
        val match = regex.find(query)
        return if (match != null) {
            val text = match.groupValues[1]
            val language = match.groupValues[3]
            "Translate the following text to $language:\n$text"
        } else null
    }
}