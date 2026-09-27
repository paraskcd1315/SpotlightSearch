package com.paraskcd.spotlightsearch.preferences.presentation.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class IconNameRankingTest {
    private val names = listOf("alarm", "calculator", "calendar_google", "clock", "google_calculator")

    @Test
    fun withoutAQueryIconsNamedLikeTheAppComeFirst() {
        assertEquals(
            listOf("calculator", "google_calculator", "alarm", "calendar_google", "clock"),
            IconNameRanking.rank(names, "Calculator", "")
        )
    }

    @Test
    fun aQueryFiltersIgnoringCaseAndPunctuation() {
        assertEquals(listOf("calendar_google"), IconNameRanking.rank(names, "Calculator", "Calendar G"))
    }

    @Test
    fun anEmptyLabelKeepsTheOrder() {
        assertEquals(names, IconNameRanking.rank(names, "", ""))
    }
}
