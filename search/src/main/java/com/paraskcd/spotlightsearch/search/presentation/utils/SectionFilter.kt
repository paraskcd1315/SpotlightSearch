package com.paraskcd.spotlightsearch.search.presentation.utils

import com.paraskcd.spotlightsearch.search.domain.model.SearchSection
import com.paraskcd.spotlightsearch.search.domain.model.SectionKind
import com.paraskcd.spotlightsearch.sources.domain.model.hits.AppHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.ContactHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.SearchHit

fun List<SearchSection>.filterKinds(): List<SectionKind> {
    val kinds = map { it.kind }.filter { it != SectionKind.TOP_HIT }
    val topHitKind = topHit()?.homeKind()
    return if (topHitKind != null && topHitKind !in kinds) kinds + topHitKind else kinds
}

fun List<SearchSection>.filteredBy(kind: SectionKind?): List<SearchSection> {
    if (kind == null) return this
    val top = topHit()?.takeIf { it.homeKind() == kind }
    val section = firstOrNull { it.kind == kind }
    val hits = listOfNotNull(top) + section?.hits.orEmpty()
    return if (hits.isEmpty()) emptyList() else listOf(SearchSection(kind, hits))
}

private fun List<SearchSection>.topHit(): SearchHit? =
    firstOrNull { it.kind == SectionKind.TOP_HIT }?.hits?.firstOrNull()

private fun SearchHit.homeKind(): SectionKind? = when (this) {
    is AppHit -> SectionKind.APPS
    is ContactHit -> SectionKind.CONTACTS
    else -> null
}
