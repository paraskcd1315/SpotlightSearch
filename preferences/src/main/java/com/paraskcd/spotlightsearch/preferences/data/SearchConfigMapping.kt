package com.paraskcd.spotlightsearch.preferences.data

import com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity.GlobalSearchConfigEntity
import com.paraskcd.spotlightsearch.search.domain.model.SearchConfig
import com.paraskcd.spotlightsearch.search.domain.model.SearchLimits
import com.paraskcd.spotlightsearch.search.domain.model.SectionKind
import com.paraskcd.spotlightsearch.search.domain.model.SectionOrder
import com.paraskcd.spotlightsearch.sources.domain.model.WebSearchEngine

private const val Separator = ","

fun GlobalSearchConfigEntity.toSearchConfig(): SearchConfig = SearchConfig(
    appsEnabled = appsEnabled,
    contactsEnabled = contactsEnabled,
    webSuggestionsEnabled = webSuggestionsEnabled,
    sectionOrder = SectionOrder.normalize(decodeKinds(sectionOrder)),
    hiddenSections = decodeKinds(hiddenSections).toSet(),
    rowsPerSection = (rowsPerSection ?: SearchLimits.DEFAULT_ROWS_PER_SECTION)
        .coerceIn(SearchLimits.MIN_ROWS_PER_SECTION, SearchLimits.MAX_ROWS_PER_SECTION),
    frequentRows = (frequentRows ?: SearchLimits.DEFAULT_FREQUENT_ROWS).coerceIn(0, SearchLimits.MAX_FREQUENT_ROWS),
    searchEngine = WebSearchEngine.entries.firstOrNull { it.name == searchEngine } ?: WebSearchEngine.SYSTEM
)

fun Collection<SectionKind>.encodeKinds(): String = joinToString(Separator) { it.name }

private fun decodeKinds(raw: String?): List<SectionKind> =
    raw.orEmpty().split(Separator).mapNotNull { name -> SectionKind.entries.firstOrNull { it.name == name } }
