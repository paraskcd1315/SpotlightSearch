package com.paraskcd.spotlightsearch.search.domain.usecase

import com.paraskcd.spotlightsearch.search.domain.model.RankedHits
import com.paraskcd.spotlightsearch.sources.domain.matching.MatchTier
import com.paraskcd.spotlightsearch.sources.domain.model.hits.AppHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.ContactHit

internal object TopHit {
    private val eligible = MatchTier.EXACT..MatchTier.WORD_PREFIX

    fun pick(apps: List<AppHit>, contacts: List<ContactHit>): RankedHits {
        val app = apps.firstOrNull()?.takeIf { it.tier in eligible }
        val contact = contacts.firstOrNull()?.takeIf { it.tier in eligible }
        return when {
            app != null && (contact == null || app.tier <= contact.tier) -> RankedHits(app, apps.drop(1), contacts)
            contact != null -> RankedHits(contact, apps, contacts.drop(1))
            else -> RankedHits(null, apps, contacts)
        }
    }
}
