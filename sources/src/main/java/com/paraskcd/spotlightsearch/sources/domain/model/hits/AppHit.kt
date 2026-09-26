package com.paraskcd.spotlightsearch.sources.domain.model.hits

import com.paraskcd.spotlightsearch.sources.domain.matching.MatchTier

data class AppHit(
    val packageName: String,
    val label: String,
    val tier: MatchTier = MatchTier.EXACT,
    val matches: List<IntRange> = emptyList()
) : SearchHit
