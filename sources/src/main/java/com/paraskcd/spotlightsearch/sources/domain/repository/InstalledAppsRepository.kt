package com.paraskcd.spotlightsearch.sources.domain.repository

import com.paraskcd.spotlightsearch.sources.domain.model.InstalledApp
import com.paraskcd.spotlightsearch.sources.domain.model.hits.AppHit
import kotlinx.coroutines.flow.StateFlow

interface InstalledAppsRepository {
    val apps: StateFlow<List<InstalledApp>>
    val allApps: StateFlow<List<InstalledApp>?>
    fun warmUp()
    suspend fun search(query: String): List<AppHit>
}
