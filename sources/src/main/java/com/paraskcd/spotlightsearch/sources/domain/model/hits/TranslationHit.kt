package com.paraskcd.spotlightsearch.sources.domain.model.hits

data class TranslationHit(
    val translation: String,
    val text: String,
    val sourceLanguage: String,
    val targetLanguage: String
) : SearchHit
