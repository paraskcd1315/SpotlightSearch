package com.paraskcd.spotlightsearch.preferences.data

import com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao.QuickSearchProviderDao
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity.QuickSearchProviderEntity
import com.paraskcd.spotlightsearch.sources.domain.model.QuickSearchPreference
import com.paraskcd.spotlightsearch.sources.domain.ports.QuickSearchOrderPort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomQuickSearchOrderPort @Inject constructor(
    private val dao: QuickSearchProviderDao
) : QuickSearchOrderPort {
    override fun preferences(): Flow<List<QuickSearchPreference>> = dao.observeAll().map { rows ->
        rows.map { QuickSearchPreference(it.packageName, it.enabled, it.sortOrder) }
    }

    override suspend fun ensureDefaults(packagesInOrder: List<String>) {
        if (dao.observeAll().first().isNotEmpty()) return
        dao.upsertAll(
            packagesInOrder.mapIndexed { index, packageName ->
                QuickSearchProviderEntity(packageName = packageName, enabled = true, sortOrder = index)
            }
        )
    }
}
