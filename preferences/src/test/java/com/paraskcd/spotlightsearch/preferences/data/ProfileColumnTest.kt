package com.paraskcd.spotlightsearch.preferences.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProfileColumnTest {
    @Test
    fun theOwnProfileIsStoredAsTheSentinelAndReadBackAsNull() {
        assertEquals(ProfileColumn.OWN, ProfileColumn.of(null))
        assertNull(ProfileColumn.profileOf(ProfileColumn.OWN))
    }

    @Test
    fun anotherProfileKeepsItsSerialNumber() {
        assertEquals(10L, ProfileColumn.of(10L))
        assertEquals(10L, ProfileColumn.profileOf(10L))
    }

    @Test
    fun serialNumberZeroIsNotTheOwnProfile() {
        assertEquals(0L, ProfileColumn.profileOf(0L))
    }
}
