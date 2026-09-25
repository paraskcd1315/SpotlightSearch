package com.paraskcd.spotlightsearch.preferences.data

import com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao.BlacklistAppsDao
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity.BlacklistAppsEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlacklistAppsRepository @Inject constructor(private val dao: BlacklistAppsDao) {
    fun observe() = dao.observe()

    suspend fun insert(packageName: String) {
        dao.insert(BlacklistAppsEntity(packageName = packageName))
    }

    suspend fun delete(packageName: String) {
        dao.delete(packageName)
    }
}
