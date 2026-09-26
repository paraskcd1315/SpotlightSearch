package com.paraskcd.spotlightsearch.search.domain.usecase

import com.paraskcd.spotlightsearch.search.domain.model.SearchLimits
import com.paraskcd.spotlightsearch.search.domain.ports.SearchConfigPort
import com.paraskcd.spotlightsearch.search.domain.ports.UsagePort
import com.paraskcd.spotlightsearch.sources.domain.model.hits.AppHit
import com.paraskcd.spotlightsearch.sources.domain.repository.InstalledAppsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class ObserveFrequentAppsUseCase @Inject constructor(
    private val config: SearchConfigPort,
    private val usage: UsagePort,
    private val apps: InstalledAppsRepository
) {
    operator fun invoke(): Flow<List<AppHit>> = config.config()
        .map { it.frequentRows * SearchLimits.APPS_PER_ROW }
        .distinctUntilChanged()
        .flatMapLatest { limit -> if (limit <= 0) flowOf(emptyList()) else observe(limit) }

    private fun observe(limit: Int): Flow<List<AppHit>> =
        combine(usage.mostUsedPackages(limit), apps.apps) { packages, installed ->
            val byPackage = installed.associateBy { it.packageName }
            packages.mapNotNull { packageName ->
                byPackage[packageName]?.let { AppHit(it.packageName, it.label) }
            }
        }
}
