package com.paraskcd.spotlightsearch.search.domain.ports

import com.paraskcd.spotlightsearch.search.domain.model.SearchConfig
import kotlinx.coroutines.flow.Flow

interface SearchConfigPort {
    fun config(): Flow<SearchConfig>
}
