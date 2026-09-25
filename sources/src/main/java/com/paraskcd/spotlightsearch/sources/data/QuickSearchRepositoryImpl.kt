package com.paraskcd.spotlightsearch.sources.data

import android.content.Context
import com.paraskcd.spotlightsearch.sources.domain.model.QuickSearchService
import com.paraskcd.spotlightsearch.sources.domain.model.hits.QuickSearchHit
import com.paraskcd.spotlightsearch.sources.domain.ports.QuickSearchOrderPort
import com.paraskcd.spotlightsearch.sources.domain.repository.QuickSearchRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuickSearchRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val order: QuickSearchOrderPort
) : QuickSearchRepository {
    private val defaults = QuickSearchService.entries.map { it.packageName }
    @Volatile private var defaultsEnsured = false

    override suspend fun targets(query: String): List<QuickSearchHit> {
        if (query.isBlank()) return emptyList()
        return withContext(Dispatchers.IO) {
            if (!defaultsEnsured) {
                order.ensureDefaults(defaults)
                defaultsEnsured = true
            }
            order.preferences().first()
                .filter { it.enabled }
                .sortedBy { it.sortOrder }
                .mapNotNull { QuickSearchService.fromPackage(it.packageName) }
                .filter { isInstalled(it.packageName) }
                .map { QuickSearchHit(it, query) }
        }
    }

    private fun isInstalled(packageName: String): Boolean =
        runCatching { context.packageManager.getApplicationInfo(packageName, 0) }.isSuccess
}
