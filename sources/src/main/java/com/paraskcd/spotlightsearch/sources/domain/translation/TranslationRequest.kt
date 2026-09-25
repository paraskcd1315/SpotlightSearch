package com.paraskcd.spotlightsearch.sources.domain.translation

data class TranslationRequest(
    val text: String,
    val targetLanguage: String,
    val sourceLanguage: String? = null
)
