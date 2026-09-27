package com.paraskcd.spotlightsearch.sources.domain.model.hits

import com.paraskcd.spotlightsearch.sources.domain.matching.MatchTier
import com.paraskcd.spotlightsearch.sources.domain.model.AppKey

data class AppHit(
    val packageName: String,
    val label: String,
    val tier: MatchTier = MatchTier.EXACT,
    val matches: List<IntRange> = emptyList(),
    val profile: Long? = null
) : SearchHit {
    val key: AppKey get() = AppKey(packageName, profile)
}
