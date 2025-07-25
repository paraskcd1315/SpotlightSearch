package com.paraskcd.spotlightsearch.providers


import android.util.Log
import com.google.ai.edge.aicore.Content
import com.google.ai.edge.aicore.GenerativeModel
import com.google.ai.edge.aicore.TextPart
import com.paraskcd.spotlightsearch.enums.SearchResultType
import com.paraskcd.spotlightsearch.types.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TranslationProvider @Inject constructor() {
    suspend fun getTranslationResult(
        model: GenerativeModel,
        query: String
    ): SearchResult? = withContext(Dispatchers.IO) {
        val regex = Regex("""translate\s+(\w+)\s+(to|into|a|en)\s+(\w+)\s*:\s*(.+)""", RegexOption.IGNORE_CASE)
        val match = regex.find(query.lowercase()) ?: return@withContext null

        val (fromLang, _, toLang, message) = match.destructured

        val prompt = """
            You are a well-known translator of any language I give you.
            You have to translate from $fromLang to $toLang.
            Your job is to only translate the message and respond me with the translated message.
            After this Prompt Translate me this message - $message
        """.trimIndent()

        return@withContext try {
            val inputResponse = model.generateContent(prompt)
            Log.d("[Debug] InputResponse", inputResponse.text?.trim().orEmpty())
            val translated = inputResponse.text?.trim().orEmpty()

            if (translated.isBlank()) return@withContext null

            SearchResult(
                title = translated,
                subtitle = "Translation ($fromLang → $toLang)",
                searchResultType = SearchResultType.TRANSLATOR,
                onClick = {},
                isHeader = false
            )
        } catch (e: Exception) {
            Log.e("TranslatorProvider", "Translation failed", e)
            null
        }
    }
}