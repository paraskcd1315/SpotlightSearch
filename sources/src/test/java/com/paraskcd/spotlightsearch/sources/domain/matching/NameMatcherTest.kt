package com.paraskcd.spotlightsearch.sources.domain.matching

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NameMatcherTest {
    @Test
    fun exactNameIsTheBestTier() {
        assertEquals(MatchTier.EXACT, NameMatcher.match("Clock", "clock")?.tier)
    }

    @Test
    fun startOfNameMatchesFromOneLetter() {
        val match = NameMatcher.match("Telegram", "t")
        assertEquals(MatchTier.PREFIX, match?.tier)
        assertEquals(listOf(0..0), match?.ranges)
    }

    @Test
    fun startOfLaterWordMatches() {
        val match = NameMatcher.match("Google Maps", "ma")
        assertEquals(MatchTier.WORD_PREFIX, match?.tier)
        assertEquals(listOf(7..8), match?.ranges)
    }

    @Test
    fun initialsMatchAcrossWords() {
        val match = NameMatcher.match("Google Maps", "gm")
        assertEquals(MatchTier.INITIALS, match?.tier)
        assertEquals(listOf(0..0, 7..7), match?.ranges)
    }

    @Test
    fun camelCaseCountsAsWordStart() {
        assertEquals(MatchTier.WORD_PREFIX, NameMatcher.match("WhatsApp", "app")?.tier)
    }

    @Test
    fun letterInsideAWordIsIgnoredForShortQueries() {
        assertNull(NameMatcher.match("Settings", "t"))
        assertNull(NameMatcher.match("Settings", "tt"))
    }

    @Test
    fun letterInsideAWordMatchesFromThreeLetters() {
        assertEquals(MatchTier.CONTAINS, NameMatcher.match("Settings", "tti")?.tier)
    }

    @Test
    fun accentsAreIgnoredBothWays() {
        assertEquals(MatchTier.PREFIX, NameMatcher.match("Música", "musi")?.tier)
        assertEquals(MatchTier.EXACT, NameMatcher.match("Musica", "música".foldForSearch())?.tier)
    }
}
