package com.paraskcd.spotlightsearch.data.repo

import com.paraskcd.spotlightsearch.data.dao.QuickSearchProviderDao
import com.paraskcd.spotlightsearch.data.entities.QuickSearchProviderEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuickSearchProviderRepository @Inject constructor(
    private val dao: QuickSearchProviderDao
) {
    private val defaults = listOf(
        "com.google.android.googlequicksearchbox",
        "com.google.android.youtube",
        "com.google.android.apps.youtube.music",
        "com.google.android.apps.maps",
        "com.android.vending",
        "com.instagram.barcelona",
        "com.linkedin.android",
        "com.twitter.android",
        "com.facebook.katana"
    )

    fun observe(): Flow<List<QuickSearchProviderEntity>> = dao.observeAll()

    suspend fun ensureDefaults() {
        val current = dao.observeAll().first()
        if (current.isEmpty()) {
            dao.upsertAll(
                defaults.mapIndexed { idx, pkg ->
                    QuickSearchProviderEntity(packageName = pkg, enabled = true, sortOrder = idx)
                }
            )
        }
    }

    suspend fun toggle(pkg: String, enabled: Boolean) = dao.setEnabled(pkg, enabled)

    suspend fun reorder(newOrder: List<String>) = dao.reorder(newOrder)
}