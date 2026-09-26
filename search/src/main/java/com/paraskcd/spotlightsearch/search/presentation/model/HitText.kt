package com.paraskcd.spotlightsearch.search.presentation.model

data class HitText(val title: String, val subtitle: String?, val matches: List<IntRange> = emptyList())
