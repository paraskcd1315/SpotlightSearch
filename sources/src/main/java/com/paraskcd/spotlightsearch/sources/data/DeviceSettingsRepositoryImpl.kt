package com.paraskcd.spotlightsearch.sources.data

import android.content.Context
import android.content.Intent
import com.paraskcd.spotlightsearch.sources.domain.model.hits.DeviceSettingHit
import com.paraskcd.spotlightsearch.sources.domain.repository.DeviceSettingsRepository
import com.paraskcd.spotlightsearch.sources.infrastructure.devicesettings.DeviceSettingsCatalog
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceSettingsRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : DeviceSettingsRepository {
    override suspend fun search(query: String): List<DeviceSettingHit> {
        if (query.isBlank()) return emptyList()
        val lowerQuery = query.lowercase()
        return withContext(Dispatchers.IO) {
            DeviceSettingsCatalog.keywords
                .filterValues { keywords -> keywords.any { lowerQuery.contains(it) } }
                .keys
                .filter { Intent(DeviceSettingsCatalog.action(it)).resolveActivity(context.packageManager) != null }
                .map { DeviceSettingHit(it) }
        }
    }
}
