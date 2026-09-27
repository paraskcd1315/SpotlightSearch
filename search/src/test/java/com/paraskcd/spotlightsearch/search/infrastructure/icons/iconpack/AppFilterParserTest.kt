package com.paraskcd.spotlightsearch.search.infrastructure.icons.iconpack

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.kxml2.io.KXmlParser
import java.io.StringReader

class AppFilterParserTest {
    private fun parse(xml: String): IconPackMap =
        AppFilterParser.parse(KXmlParser().apply { setInput(StringReader(xml)) })

    @Test
    fun readsItemsAndCalendars() {
        val map = parse(
            """
            <resources>
                <item component="ComponentInfo{com.clock/com.clock.Main}" drawable="clock" />
                <calendar component="ComponentInfo{com.cal/com.cal.Main}" prefix="calendar_" />
            </resources>
            """
        )
        assertEquals("clock", map.items["com.clock/com.clock.Main"])
        assertEquals("calendar_", map.calendars["com.cal/com.cal.Main"])
    }

    @Test
    fun theFirstEntryForAComponentWins() {
        val map = parse(
            """
            <resources>
                <item component="ComponentInfo{com.a/com.a.Main}" drawable="first" />
                <item component="ComponentInfo{com.a/com.a.Main}" drawable="second" />
            </resources>
            """
        )
        assertEquals("first", map.items["com.a/com.a.Main"])
    }

    @Test
    fun readsTheDecorationTags() {
        val map = parse(
            """
            <resources>
                <iconback img1="back_a" img2="back_b" />
                <iconmask img1="mask" />
                <iconupon img1="upon" />
                <scale factor="0.8" />
            </resources>
            """
        )
        assertEquals(listOf("back_a", "back_b"), map.backs)
        assertEquals("mask", map.mask)
        assertEquals("upon", map.upon)
        assertEquals(0.8f, map.scale)
    }

    @Test
    fun acceptsDoubledBracesAndShortActivityNames() {
        assertEquals("org.a/org.a.Cal", AppFilterParser.component("ComponentInfo{{org.a/org.a.Cal}"))
        assertEquals("org.a/org.a.Main", AppFilterParser.component("ComponentInfo{org.a/.Main}"))
    }

    @Test
    fun skipsCategoryPlaceholdersAndEntriesWithoutActivity() {
        assertNull(AppFilterParser.component(":BROWSER"))
        assertNull(AppFilterParser.component("ComponentInfo{org.a}"))
        assertNull(AppFilterParser.component("ComponentInfo{org.a/}"))
    }
}
