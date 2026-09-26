package com.paraskcd.spotlightsearch.sources.domain.model.hits

import com.paraskcd.spotlightsearch.sources.domain.matching.MatchTier

data class ContactHit(
    val name: String,
    val number: String,
    val photoUri: String?,
    val hasWhatsApp: Boolean,
    val tier: MatchTier = MatchTier.EXACT,
    val matches: List<IntRange> = emptyList()
) : SearchHit
