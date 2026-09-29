package com.paraskcd.spotlightsearch.sources.domain.model.hits

import com.paraskcd.spotlightsearch.sources.domain.model.TranslationStatus

data class TranslationHit(
    val translation: String,
    val text: String,
    val sourceLanguage: String,
    val targetLanguage: String,
    val status: TranslationStatus = TranslationStatus.READY
) : SearchHit
