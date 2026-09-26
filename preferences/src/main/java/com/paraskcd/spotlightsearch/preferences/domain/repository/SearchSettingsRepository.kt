package com.paraskcd.spotlightsearch.preferences.domain.repository

import com.paraskcd.spotlightsearch.search.domain.model.SearchConfig
import com.paraskcd.spotlightsearch.search.domain.model.SectionKind
import com.paraskcd.spotlightsearch.sources.domain.model.WebSearchEngine
import com.paraskcd.spotlightsearch.sources.domain.model.QuickSearchPreference
import kotlinx.coroutines.flow.Flow

interface SearchSettingsRepository {
    fun config(): Flow<SearchConfig>
    suspend fun setAppsEnabled(enabled: Boolean)
    suspend fun setContactsEnabled(enabled: Boolean)
    suspend fun setWebSuggestionsEnabled(enabled: Boolean)
    suspend fun setSectionOrder(order: List<SectionKind>)
    suspend fun setSectionVisible(kind: SectionKind, visible: Boolean)
    suspend fun setRowsPerSection(rows: Int)
    suspend fun setFrequentRows(rows: Int)
    suspend fun setSearchEngine(engine: WebSearchEngine)

    fun quickSearch(): Flow<List<QuickSearchPreference>>
    suspend fun ensureQuickSearchDefaults()
    suspend fun setQuickSearchEnabled(packageName: String, enabled: Boolean)
    suspend fun reorderQuickSearch(packagesInOrder: List<String>)

    fun blacklist(): Flow<Set<String>>
    suspend fun setBlacklisted(packageName: String, blacklisted: Boolean)
}
