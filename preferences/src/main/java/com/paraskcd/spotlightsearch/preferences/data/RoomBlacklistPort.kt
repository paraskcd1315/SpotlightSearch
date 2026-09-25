package com.paraskcd.spotlightsearch.preferences.data

import com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao.BlacklistAppsDao
import com.paraskcd.spotlightsearch.sources.domain.ports.BlacklistPort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomBlacklistPort @Inject constructor(private val dao: BlacklistAppsDao) : BlacklistPort {
    override fun blacklistedPackages(): Flow<Set<String>> =
        dao.observe().map { rows -> rows.map { it.packageName }.toSet() }
}
