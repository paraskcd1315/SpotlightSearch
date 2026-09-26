package com.paraskcd.spotlightsearch.sources.domain.ports

import com.paraskcd.spotlightsearch.sources.domain.model.QuickSearchPreference
import kotlinx.coroutines.flow.Flow

interface QuickSearchOrderPort {
    fun preferences(): Flow<List<QuickSearchPreference>>
    suspend fun ensureDefaults(packagesInOrder: List<String>)
}
