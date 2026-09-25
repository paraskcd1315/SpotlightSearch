package com.paraskcd.spotlightsearch.sources.domain.repository

import com.paraskcd.spotlightsearch.sources.domain.model.hits.DeviceSettingHit

interface DeviceSettingsRepository {
    suspend fun search(query: String): List<DeviceSettingHit>
}
