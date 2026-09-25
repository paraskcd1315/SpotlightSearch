package com.paraskcd.spotlightsearch.sources.data

import com.paraskcd.spotlightsearch.sources.domain.model.hits.ContactHit
import com.paraskcd.spotlightsearch.sources.domain.repository.ContactsRepository
import com.paraskcd.spotlightsearch.sources.infrastructure.contacts.ContactsSource
import com.paraskcd.spotlightsearch.sources.infrastructure.contacts.PhoneContact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsRepositoryImpl @Inject constructor(
    private val source: ContactsSource
) : ContactsRepository {
    private val mutex = Mutex()
    @Volatile private var cache: List<PhoneContact>? = null
    private var observing = false

    override fun hasPermission(): Boolean = source.hasPermission()

    override suspend fun search(query: String): List<ContactHit> {
        if (query.isBlank() || !source.hasPermission()) return emptyList()
        return withContext(Dispatchers.IO) {
            val matches = contacts().filter { it.name.contains(query, ignoreCase = true) }
            if (matches.isEmpty()) return@withContext emptyList()
            val hasWhatsApp = source.hasWhatsApp()
            matches.map { ContactHit(it.name, it.number, it.photoUri, hasWhatsApp) }
        }
    }

    private suspend fun contacts(): List<PhoneContact> = cache ?: mutex.withLock {
        cache ?: source.load().also { loaded ->
            cache = loaded
            if (!observing) {
                observing = true
                withContext(Dispatchers.Main) { source.observeChanges { cache = null } }
            }
        }
    }
}
