package com.paraskcd.spotlightsearch.search.presentation.utils

import com.paraskcd.spotlightsearch.search.domain.model.SearchSection
import com.paraskcd.spotlightsearch.search.domain.model.SectionKind
import com.paraskcd.spotlightsearch.sources.domain.model.hits.AppHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.ContactHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.SearchHit

fun List<SearchSection>.filterKinds(): List<SectionKind> {
    val kinds = map { it.kind.filterKind() }.filterNotNull().distinct()
    val topHitKind = topHit()?.homeKind()
    return if (topHitKind != null && topHitKind !in kinds) kinds + topHitKind else kinds
}

fun List<SearchSection>.filteredBy(kind: SectionKind?): List<SearchSection> {
    if (kind == null) return this
    val top = listOfNotNull(topHit()?.takeIf { it.homeKind() == kind })
    val hits = top + filter { it.kind.filterKind() == kind }.flatMap { it.hits }
    return if (hits.isEmpty()) emptyList() else listOf(SearchSection(kind, hits))
}

private fun SectionKind.filterKind(): SectionKind? = when (this) {
    SectionKind.TOP_HIT -> null
    SectionKind.WEB -> SectionKind.QUICK_SEARCH
    else -> this
}

private fun List<SearchSection>.topHit(): SearchHit? =
    firstOrNull { it.kind == SectionKind.TOP_HIT }?.hits?.firstOrNull()

private fun SearchHit.homeKind(): SectionKind? = when (this) {
    is AppHit -> SectionKind.APPS
    is ContactHit -> SectionKind.CONTACTS
    else -> null
}
