package com.paraskcd.spotlightsearch.sources.domain.model.hits

data class ContactHit(
    val name: String,
    val number: String,
    val photoUri: String?,
    val hasWhatsApp: Boolean
) : SearchHit
