package com.paraskcd.spotlightsearch.search.domain.ports

import com.paraskcd.spotlightsearch.sources.domain.model.AppKey
import kotlinx.coroutines.flow.Flow

interface UsagePort {
    fun mostUsedApps(limit: Int): Flow<List<AppKey>>
    suspend fun recordLaunch(app: AppKey)
}
