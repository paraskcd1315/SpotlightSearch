package com.paraskcd.spotlightsearch.search.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class SectionOrderTest {
    @Test
    fun savedOrderComesFirstAndMissingKindsFollowInDefaultOrder() {
        val normalized = SectionOrder.normalize(listOf(SectionKind.APPS, SectionKind.CONTACTS))
        assertEquals(listOf(SectionKind.APPS, SectionKind.CONTACTS), normalized.take(2))
        assertEquals(SectionOrder.configurable.size, normalized.size)
        assertEquals(SectionKind.CALCULATOR, normalized[2])
    }

    @Test
    fun unknownAndDuplicateKindsAreDropped() {
        val normalized = SectionOrder.normalize(listOf(SectionKind.FREQUENT, SectionKind.APPS, SectionKind.APPS))
        assertEquals(SectionKind.APPS, normalized.first())
        assertEquals(SectionOrder.configurable.toSet(), normalized.toSet())
    }

    @Test
    fun frequentRanksFirstAndPermissionsLast() {
        val order = SectionOrder.configurable
        assertEquals(-1, SectionOrder.rank(order, SectionKind.FREQUENT))
        assertEquals(order.size, SectionOrder.rank(order, SectionKind.PERMISSIONS))
        assertEquals(0, SectionOrder.rank(order, SectionKind.CALCULATOR))
    }
}
