package com.paraskcd.spotlightsearch.preferences.data

import com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao.GlobalSearchConfigDao
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity.GlobalSearchConfigEntity
import com.paraskcd.spotlightsearch.search.domain.model.SearchConfig
import com.paraskcd.spotlightsearch.search.domain.ports.SearchConfigPort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomSearchConfigPort @Inject constructor(
    private val dao: GlobalSearchConfigDao
) : SearchConfigPort {
    override fun config(): Flow<SearchConfig> = dao.observe().map { row ->
        val config = row ?: GlobalSearchConfigEntity()
        SearchConfig(
            appsEnabled = config.appsEnabled,
            contactsEnabled = config.contactsEnabled,
            webSuggestionsEnabled = config.webSuggestionsEnabled
        )
    }
}
