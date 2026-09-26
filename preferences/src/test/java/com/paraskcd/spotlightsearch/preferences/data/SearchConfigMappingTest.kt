package com.paraskcd.spotlightsearch.preferences.data

import com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity.GlobalSearchConfigEntity
import com.paraskcd.spotlightsearch.search.domain.model.SearchLimits
import com.paraskcd.spotlightsearch.search.domain.model.SectionKind
import com.paraskcd.spotlightsearch.search.domain.model.SectionOrder
import com.paraskcd.spotlightsearch.sources.domain.model.WebSearchEngine
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchConfigMappingTest {
    @Test
    fun emptyRowGivesTheDefaults() {
        val config = GlobalSearchConfigEntity().toSearchConfig()
        assertEquals(SectionOrder.configurable, config.sectionOrder)
        assertEquals(emptySet<SectionKind>(), config.hiddenSections)
        assertEquals(SearchLimits.DEFAULT_ROWS_PER_SECTION, config.rowsPerSection)
        assertEquals(SearchLimits.DEFAULT_FREQUENT_ROWS, config.frequentRows)
        assertEquals(WebSearchEngine.SYSTEM, config.searchEngine)
    }

    @Test
    fun storedValuesRoundTrip() {
        val entity = GlobalSearchConfigEntity(
            sectionOrder = listOf(SectionKind.APPS, SectionKind.WEB).encodeKinds(),
            hiddenSections = setOf(SectionKind.SUGGESTIONS).encodeKinds(),
            rowsPerSection = 8,
            frequentRows = 0,
            searchEngine = WebSearchEngine.DUCKDUCKGO.name
        )
        val config = entity.toSearchConfig()
        assertEquals(listOf(SectionKind.APPS, SectionKind.WEB), config.sectionOrder.take(2))
        assertEquals(setOf(SectionKind.SUGGESTIONS), config.hiddenSections)
        assertEquals(8, config.rowsPerSection)
        assertEquals(0, config.frequentRows)
        assertEquals(WebSearchEngine.DUCKDUCKGO, config.searchEngine)
    }

    @Test
    fun outOfRangeAndUnknownValuesFallBack() {
        val config = GlobalSearchConfigEntity(
            sectionOrder = "NOT_A_KIND,APPS",
            rowsPerSection = 99,
            frequentRows = -4,
            searchEngine = "ALTAVISTA"
        ).toSearchConfig()
        assertEquals(SectionKind.APPS, config.sectionOrder.first())
        assertEquals(SearchLimits.MAX_ROWS_PER_SECTION, config.rowsPerSection)
        assertEquals(0, config.frequentRows)
        assertEquals(WebSearchEngine.SYSTEM, config.searchEngine)
    }
}
