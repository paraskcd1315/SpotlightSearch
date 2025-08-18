package com.paraskcd.spotlightsearch.data.repo

import com.paraskcd.spotlightsearch.data.dao.GlobalSearchConfigDao
import com.paraskcd.spotlightsearch.data.entities.GlobalSearchConfigEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalSearchConfigRepository @Inject constructor(
    private val dao: GlobalSearchConfigDao
) {
    val config: Flow<GlobalSearchConfigEntity> =
        dao.observe().map { it ?: GlobalSearchConfigEntity() }

    suspend fun ensure() = dao.ensureDefault()

    private suspend fun update(block: (GlobalSearchConfigEntity) -> GlobalSearchConfigEntity) {
        val current = dao.get() ?: GlobalSearchConfigEntity()
        dao.insert(block(current))
    }

    suspend fun setAppsEnabled(v: Boolean) = update { it.copy(appsEnabled = v) }
    suspend fun setContactsEnabled(v: Boolean) = update { it.copy(contactsEnabled = v) }
    suspend fun setWebSuggestionsEnabled(v: Boolean) = update { it.copy(webSuggestionsEnabled = v) }
}