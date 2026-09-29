package com.paraskcd.spotlightsearch.search.domain.ports

import com.paraskcd.spotlightsearch.search.domain.model.AppIconChoice
import com.paraskcd.spotlightsearch.sources.domain.model.AppKey
import kotlinx.coroutines.flow.Flow

interface AppIconPort {
    fun choices(): Flow<Map<AppKey, AppIconChoice>>
    suspend fun choose(app: AppKey, choice: AppIconChoice?)
}
