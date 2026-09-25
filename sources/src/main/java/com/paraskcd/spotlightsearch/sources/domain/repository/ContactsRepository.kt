package com.paraskcd.spotlightsearch.sources.domain.repository

import com.paraskcd.spotlightsearch.sources.domain.model.hits.ContactHit

interface ContactsRepository {
    fun hasPermission(): Boolean
    suspend fun search(query: String): List<ContactHit>
}
