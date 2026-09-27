package com.paraskcd.spotlightsearch.preferences.data

import com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao.AppUsageDao
import com.paraskcd.spotlightsearch.search.domain.ports.UsagePort
import com.paraskcd.spotlightsearch.sources.domain.model.AppKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomUsagePort @Inject constructor(private val dao: AppUsageDao) : UsagePort {
    override fun mostUsedApps(limit: Int): Flow<List<AppKey>> =
        dao.observeTopAppUsages(limit).map { rows -> rows.map { AppKey(it.packageName, ProfileColumn.profileOf(it.profile)) } }

    override suspend fun recordLaunch(app: AppKey) {
        dao.increment(app.packageName, ProfileColumn.of(app.profile), System.currentTimeMillis())
    }
}
