package com.paraskcd.spotlightsearch.preferences.data

import com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao.BlacklistAppsDao
import com.paraskcd.spotlightsearch.sources.domain.model.AppKey
import com.paraskcd.spotlightsearch.sources.domain.ports.BlacklistPort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomBlacklistPort @Inject constructor(private val dao: BlacklistAppsDao) : BlacklistPort {
    override fun blacklistedApps(): Flow<Set<AppKey>> =
        dao.observe().map { rows -> rows.map { AppKey(it.packageName, ProfileColumn.profileOf(it.profile)) }.toSet() }
}
