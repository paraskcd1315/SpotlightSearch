package com.paraskcd.spotlightsearch.search.domain.usecase

import com.paraskcd.spotlightsearch.search.domain.ports.UsagePort
import com.paraskcd.spotlightsearch.sources.domain.model.hits.AppHit
import com.paraskcd.spotlightsearch.sources.domain.repository.InstalledAppsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveFrequentAppsUseCase @Inject constructor(
    private val usage: UsagePort,
    private val apps: InstalledAppsRepository
) {
    operator fun invoke(limit: Int = DEFAULT_LIMIT): Flow<List<AppHit>> =
        combine(usage.mostUsedPackages(limit), apps.apps) { packages, installed ->
            val byPackage = installed.associateBy { it.packageName }
            packages.mapNotNull { packageName ->
                byPackage[packageName]?.let { AppHit(it.packageName, it.label) }
            }
        }

    private companion object {
        const val DEFAULT_LIMIT = 5
    }
}
