package com.paraskcd.spotlightsearch.search.domain.usecase

import com.paraskcd.spotlightsearch.sources.domain.matching.MatchTier
import com.paraskcd.spotlightsearch.sources.domain.model.hits.AppHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.ContactHit
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TopHitTest {
    private val clock = AppHit("com.clock", "Clock", MatchTier.PREFIX)
    private val calendar = AppHit("com.calendar", "Calendar", MatchTier.INITIALS)
    private val carla = ContactHit("Carla", "600", null, false, MatchTier.EXACT)

    @Test
    fun bestAppMovesOutOfItsSection() {
        val ranked = TopHit.pick(listOf(clock, calendar), emptyList())
        assertEquals(clock, ranked.top)
        assertEquals(listOf(calendar), ranked.apps)
    }

    @Test
    fun betterContactBeatsApp() {
        val ranked = TopHit.pick(listOf(clock), listOf(carla))
        assertEquals(carla, ranked.top)
        assertEquals(listOf(clock), ranked.apps)
        assertEquals(emptyList<ContactHit>(), ranked.contacts)
    }

    @Test
    fun weakMatchesGiveNoTopHit() {
        val ranked = TopHit.pick(listOf(calendar), emptyList())
        assertNull(ranked.top)
        assertEquals(listOf(calendar), ranked.apps)
    }
}
