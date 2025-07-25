package com.paraskcd.spotlightsearch.types

data class TranslationRequest(val to: String, val text: String, val from: String? = null)