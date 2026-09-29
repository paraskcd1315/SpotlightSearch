package com.paraskcd.spotlightsearch.sources.domain.model.actions

data class OpenContact(val number: String, val workLookupUri: String? = null) : HitAction
