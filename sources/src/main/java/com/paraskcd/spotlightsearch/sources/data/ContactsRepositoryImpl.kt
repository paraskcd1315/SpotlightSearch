package com.paraskcd.spotlightsearch.sources.data

import com.paraskcd.spotlightsearch.sources.domain.matching.NameMatch
import com.paraskcd.spotlightsearch.sources.domain.matching.NameMatcher
import com.paraskcd.spotlightsearch.sources.domain.matching.foldForSearch
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
        val folded = query.trim().foldForSearch()
        if (folded.isEmpty() || !source.hasPermission()) return emptyList()
        return withContext(Dispatchers.IO) {
            val matches = contacts().mapNotNull { contact -> NameMatcher.match(contact.name, folded)?.let { contact to it } }
            if (matches.isEmpty()) return@withContext emptyList()
            val hasWhatsApp = source.hasWhatsApp()
            matches
                .sortedWith(compareBy<Pair<PhoneContact, NameMatch>> { it.second.tier }.thenBy { it.first.name.foldForSearch() })
                .map { (contact, match) ->
                    ContactHit(contact.name, contact.number, contact.photoUri, hasWhatsApp, match.tier, match.ranges)
                }
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
