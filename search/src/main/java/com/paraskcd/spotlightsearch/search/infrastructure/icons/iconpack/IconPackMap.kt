package com.paraskcd.spotlightsearch.search.infrastructure.icons.iconpack

data class IconPackMap(
    val items: Map<String, String> = emptyMap(),
    val calendars: Map<String, String> = emptyMap(),
    val backs: List<String> = emptyList(),
    val mask: String? = null,
    val upon: String? = null,
    val scale: Float = 1f
)
