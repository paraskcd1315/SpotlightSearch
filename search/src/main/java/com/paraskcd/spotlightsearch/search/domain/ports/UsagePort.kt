package com.paraskcd.spotlightsearch.search.domain.ports

import kotlinx.coroutines.flow.Flow

interface UsagePort {
    fun mostUsedPackages(limit: Int): Flow<List<String>>
    suspend fun recordLaunch(packageName: String)
}
