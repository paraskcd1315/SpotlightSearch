package com.paraskcd.spotlightsearch.preferences.data

import com.paraskcd.spotlightsearch.preferences.domain.repository.SearchSettingsRepository
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao.BlacklistAppsDao
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao.GlobalSearchConfigDao
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao.QuickSearchProviderDao
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity.BlacklistAppsEntity
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity.GlobalSearchConfigEntity
import com.paraskcd.spotlightsearch.search.domain.model.SearchConfig
import com.paraskcd.spotlightsearch.search.domain.model.SearchLimits
import com.paraskcd.spotlightsearch.search.domain.model.SectionKind
import com.paraskcd.spotlightsearch.search.domain.model.SectionOrder
import com.paraskcd.spotlightsearch.sources.domain.model.WebSearchEngine
import com.paraskcd.spotlightsearch.search.domain.ports.SearchConfigPort
import com.paraskcd.spotlightsearch.sources.domain.model.QuickSearchPreference
import com.paraskcd.spotlightsearch.sources.domain.model.QuickSearchService
import com.paraskcd.spotlightsearch.sources.domain.ports.BlacklistPort
import com.paraskcd.spotlightsearch.sources.domain.ports.QuickSearchOrderPort
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomSearchSettingsRepository @Inject constructor(
    private val configDao: GlobalSearchConfigDao,
    private val quickSearchDao: QuickSearchProviderDao,
    private val blacklistDao: BlacklistAppsDao,
    private val configPort: SearchConfigPort,
    private val quickSearchPort: QuickSearchOrderPort,
    private val blacklistPort: BlacklistPort
) : SearchSettingsRepository {
    override fun config(): Flow<SearchConfig> = configPort.config()

    override suspend fun setAppsEnabled(enabled: Boolean) = updateConfig { it.copy(appsEnabled = enabled) }

    override suspend fun setContactsEnabled(enabled: Boolean) = updateConfig { it.copy(contactsEnabled = enabled) }

    override suspend fun setWebSuggestionsEnabled(enabled: Boolean) =
        updateConfig { it.copy(webSuggestionsEnabled = enabled) }

    override suspend fun setSectionOrder(order: List<SectionKind>) =
        updateConfig { it.copy(sectionOrder = SectionOrder.normalize(order).encodeKinds()) }

    override suspend fun setSectionVisible(kind: SectionKind, visible: Boolean) = updateConfig { entity ->
        val hidden = entity.toSearchConfig().hiddenSections
        entity.copy(hiddenSections = (if (visible) hidden - kind else hidden + kind).encodeKinds())
    }

    override suspend fun setRowsPerSection(rows: Int) = updateConfig {
        it.copy(rowsPerSection = rows.coerceIn(SearchLimits.MIN_ROWS_PER_SECTION, SearchLimits.MAX_ROWS_PER_SECTION))
    }

    override suspend fun setFrequentRows(rows: Int) =
        updateConfig { it.copy(frequentRows = rows.coerceIn(0, SearchLimits.MAX_FREQUENT_ROWS)) }

    override suspend fun setSearchEngine(engine: WebSearchEngine) = updateConfig { it.copy(searchEngine = engine.name) }

    override fun quickSearch(): Flow<List<QuickSearchPreference>> = quickSearchPort.preferences()

    override suspend fun ensureQuickSearchDefaults() =
        quickSearchPort.ensureDefaults(QuickSearchService.entries.map { it.packageName })

    override suspend fun setQuickSearchEnabled(packageName: String, enabled: Boolean) =
        quickSearchDao.setEnabled(packageName, enabled)

    override suspend fun reorderQuickSearch(packagesInOrder: List<String>) = quickSearchDao.reorder(packagesInOrder)

    override fun blacklist(): Flow<Set<String>> = blacklistPort.blacklistedPackages()

    override suspend fun setBlacklisted(packageName: String, blacklisted: Boolean) {
        if (blacklisted) blacklistDao.insert(BlacklistAppsEntity(packageName = packageName))
        else blacklistDao.delete(packageName)
    }

    private suspend fun updateConfig(transform: (GlobalSearchConfigEntity) -> GlobalSearchConfigEntity) {
        configDao.insert(transform(configDao.get() ?: GlobalSearchConfigEntity()))
    }
}
