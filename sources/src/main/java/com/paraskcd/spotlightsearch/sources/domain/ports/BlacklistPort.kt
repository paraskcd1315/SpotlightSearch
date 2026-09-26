package com.paraskcd.spotlightsearch.sources.domain.ports

import kotlinx.coroutines.flow.Flow

interface BlacklistPort {
    fun blacklistedPackages(): Flow<Set<String>>
}
