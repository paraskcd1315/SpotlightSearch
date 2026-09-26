package com.paraskcd.spotlightsearch.sources.data

import android.content.Context
import android.content.Intent
import com.paraskcd.spotlightsearch.sources.domain.matching.SearchThresholds
import com.paraskcd.spotlightsearch.sources.domain.matching.foldForSearch
import com.paraskcd.spotlightsearch.sources.domain.model.DeviceSetting
import com.paraskcd.spotlightsearch.sources.domain.model.hits.DeviceSettingHit
import com.paraskcd.spotlightsearch.sources.domain.repository.DeviceSettingsRepository
import com.paraskcd.spotlightsearch.sources.infrastructure.devicesettings.DeviceSettingsCatalog
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceSettingsRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : DeviceSettingsRepository {
    private val resolvable = ConcurrentHashMap<DeviceSetting, Boolean>()

    override suspend fun search(query: String): List<DeviceSettingHit> {
        val folded = query.trim().foldForSearch()
        if (folded.isEmpty()) return emptyList()
        return withContext(Dispatchers.IO) {
            DeviceSettingsCatalog.keywords
                .filterValues { keywords -> keywords.any { matches(it.foldForSearch(), folded) } }
                .keys
                .filter { setting -> resolvable.getOrPut(setting) { resolves(setting) } }
                .map { DeviceSettingHit(it) }
        }
    }

    private fun matches(keyword: String, query: String): Boolean =
        query.contains(keyword) || (query.length >= SearchThresholds.SHORT_QUERY_LENGTH && keyword.startsWith(query))

    private fun resolves(setting: DeviceSetting): Boolean =
        Intent(DeviceSettingsCatalog.action(setting)).resolveActivity(context.packageManager) != null
}
