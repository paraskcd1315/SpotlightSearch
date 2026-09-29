package com.paraskcd.spotlightsearch.sources.domain.ports

import com.paraskcd.spotlightsearch.sources.domain.model.AppKey
import kotlinx.coroutines.flow.Flow

interface BlacklistPort {
    fun blacklistedApps(): Flow<Set<AppKey>>
}
