package com.paraskcd.spotlightsearch.preferences.data

import com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao.AppUsageDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppUsageRepository @Inject constructor(private val dao: AppUsageDao) {
    fun observeTop(limit: Int) = dao.observeTopAppUsages(limit)

    suspend fun increment(packageName: String) {
        dao.increment(packageName, System.currentTimeMillis())
    }
}