package com.paraskcd.spotlightsearch.preferences.data

import com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao.AppIconDao
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity.AppIconEntity
import com.paraskcd.spotlightsearch.search.domain.model.AppIconChoice
import com.paraskcd.spotlightsearch.search.domain.ports.AppIconPort
import com.paraskcd.spotlightsearch.sources.domain.model.AppKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomAppIconPort @Inject constructor(private val dao: AppIconDao) : AppIconPort {
    override fun choices(): Flow<Map<AppKey, AppIconChoice>> =
        dao.observe().map { rows ->
            rows.associate { AppKey(it.packageName, ProfileColumn.profileOf(it.profile)) to AppIconChoice(it.iconPack, it.drawable) }
        }

    override suspend fun choose(app: AppKey, choice: AppIconChoice?) {
        val profile = ProfileColumn.of(app.profile)
        if (choice == null) dao.delete(app.packageName, profile)
        else dao.upsert(AppIconEntity(app.packageName, profile, choice.iconPack, choice.drawable))
    }
}
