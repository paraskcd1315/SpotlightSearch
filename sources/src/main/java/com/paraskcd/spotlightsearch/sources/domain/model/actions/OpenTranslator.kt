package com.paraskcd.spotlightsearch.sources.domain.model.actions

data class OpenTranslator(
    val text: String,
    val sourceLanguage: String?,
    val targetLanguage: String
) : HitAction
