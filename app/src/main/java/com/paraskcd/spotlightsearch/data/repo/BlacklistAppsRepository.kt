package com.paraskcd.spotlightsearch.data.repo

import com.paraskcd.spotlightsearch.data.dao.BlacklistAppsDao
import com.paraskcd.spotlightsearch.data.entities.BlacklistAppsEntity
import javax.inject.Singleton

@Singleton
class BlacklistAppsRepository(dao: BlacklistAppsDao) {
    private val blacklistAppsDao = dao

    fun observe() = blacklistAppsDao.observe()

    suspend fun insert(packageName: String) {
        blacklistAppsDao.insert(BlacklistAppsEntity(packageName = packageName))
    }

    suspend fun delete(packageName: String) {
        blacklistAppsDao.delete(packageName)
    }
}