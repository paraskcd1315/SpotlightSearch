package com.paraskcd.spotlightsearch.sources.domain.translation

import javax.inject.Inject

class TranslationQueryParser @Inject constructor() {
    private val fromTo = Regex("""translate\s+from\s+(\w+)\s+to\s+(\w+)\s*[:\-]?\s*(.+)""", RegexOption.IGNORE_CASE)
    private val toThenText = Regex("""translate\s+to\s+(\w+)\s*[:\-]?\s*(.+)""", RegexOption.IGNORE_CASE)
    private val translateTextTo = Regex("""translate\s+(.+)\s+to\s+(\w+)""", RegexOption.IGNORE_CASE)
    private val textToLanguage = Regex("""(.+)\s+to\s+(\w+)""", RegexOption.IGNORE_CASE)
    private val languageThenText = Regex("""(\w+)\s*[:\-]?\s+(.+)""", RegexOption.IGNORE_CASE)
    private val textInLanguage = Regex("""(.+)\s+in\s+(\w+)""", RegexOption.IGNORE_CASE)

    fun parse(raw: String): TranslationRequest? {
        val input = raw.trim().lowercase()

        fromTo.find(input)?.let { match ->
            return request(match.groupValues[3], match.groupValues[2], match.groupValues[1])
        }
        toThenText.find(input)?.let { match -> return request(match.groupValues[2], match.groupValues[1]) }
        translateTextTo.find(input)?.let { match -> return request(match.groupValues[1], match.groupValues[2]) }
        textToLanguage.find(input)?.let { match -> return request(match.groupValues[1], match.groupValues[2]) }
        languageThenText.find(input)?.let { match -> return request(match.groupValues[2], match.groupValues[1]) }
        textInLanguage.find(input)?.let { match -> return request(match.groupValues[1], match.groupValues[2]) }
        return null
    }

    private fun request(text: String, target: String, source: String? = null) = TranslationRequest(
        text = text,
        targetLanguage = LanguageNames.code(target),
        sourceLanguage = source?.let(LanguageNames::code)
    )
}
