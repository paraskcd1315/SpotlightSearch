package com.paraskcd.spotlightsearch.sources.infrastructure.contacts

data class PhoneContact(
    val name: String,
    val number: String,
    val photoUri: String?,
    val workLookupUri: String? = null
)
