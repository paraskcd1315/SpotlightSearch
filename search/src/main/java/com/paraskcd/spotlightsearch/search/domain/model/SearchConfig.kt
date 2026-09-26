package com.paraskcd.spotlightsearch.search.domain.model

import com.paraskcd.spotlightsearch.sources.domain.model.WebSearchEngine

data class SearchConfig(
    val appsEnabled: Boolean = true,
    val contactsEnabled: Boolean = true,
    val webSuggestionsEnabled: Boolean = true,
    val sectionOrder: List<SectionKind> = SectionOrder.configurable,
    val hiddenSections: Set<SectionKind> = emptySet(),
    val rowsPerSection: Int = SearchLimits.DEFAULT_ROWS_PER_SECTION,
    val frequentRows: Int = SearchLimits.DEFAULT_FREQUENT_ROWS,
    val searchEngine: WebSearchEngine = WebSearchEngine.SYSTEM
) {
    fun shows(kind: SectionKind): Boolean = kind !in hiddenSections
}
