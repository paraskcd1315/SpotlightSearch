package com.paraskcd.spotlightsearch.sources.domain.translation

import javax.inject.Inject

class TranslationQueryParser @Inject constructor() {
    private val fromTo = Regex("""translate\s+from\s+(\w+)\s+to\s+(\w+)\s*[:\-]?\s*(.+)""", RegexOption.IGNORE_CASE)
    private val toThenText = Regex("""translate\s+(?:to|into)\s+(\w+)\s*[:\-]?\s*(.+)""", RegexOption.IGNORE_CASE)
    private val translateTextTo = Regex("""translate\s+(.+)\s+(?:to|into|in)\s+(\w+)""", RegexOption.IGNORE_CASE)
    private val textToLanguage = Regex("""(.+)\s+(?:to|into|in)\s+(\w+)""", RegexOption.IGNORE_CASE)
    private val languageThenText = Regex("""(\w+)\s*:\s*(.+)""", RegexOption.IGNORE_CASE)

    fun parse(raw: String): TranslationRequest? {
        val input = raw.trim()

        fromTo.matchEntire(input)?.let { match ->
            return request(match.groupValues[3], match.groupValues[2], LanguageNames::fromNameOrCode, match.groupValues[1])
        }
        toThenText.matchEntire(input)?.let { match ->
            return request(match.groupValues[2], match.groupValues[1], LanguageNames::fromNameOrCode)
        }
        translateTextTo.matchEntire(input)?.let { match ->
            return request(match.groupValues[1], match.groupValues[2], LanguageNames::fromNameOrCode)
        }
        textToLanguage.matchEntire(input)?.let { match ->
            return request(match.groupValues[1], match.groupValues[2], LanguageNames::fromName)
        }
        languageThenText.matchEntire(input)?.let { match ->
            return request(match.groupValues[2], match.groupValues[1], LanguageNames::fromName)
        }
        return null
    }

    private fun request(
        text: String,
        target: String,
        resolve: (String) -> String?,
        source: String? = null
    ): TranslationRequest? {
        val targetCode = resolve(target) ?: return null
        val sourceCode = source?.let { resolve(it) ?: return null }
        val trimmed = text.trim().takeIf { it.isNotEmpty() } ?: return null
        return TranslationRequest(text = trimmed, targetLanguage = targetCode, sourceLanguage = sourceCode)
    }
}
