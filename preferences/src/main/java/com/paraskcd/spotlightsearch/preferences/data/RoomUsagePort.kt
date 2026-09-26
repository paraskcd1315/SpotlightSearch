package com.paraskcd.spotlightsearch.preferences.data

import com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao.AppUsageDao
import com.paraskcd.spotlightsearch.search.domain.ports.UsagePort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomUsagePort @Inject constructor(private val dao: AppUsageDao) : UsagePort {
    override fun mostUsedPackages(limit: Int): Flow<List<String>> =
        dao.observeTopAppUsages(limit).map { rows -> rows.map { it.packageName } }

    override suspend fun recordLaunch(packageName: String) {
        dao.increment(packageName, System.currentTimeMillis())
    }
}
