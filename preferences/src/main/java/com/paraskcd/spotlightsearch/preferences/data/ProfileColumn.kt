package com.paraskcd.spotlightsearch.preferences.data

object ProfileColumn {
    const val OWN = -1L

    fun of(profile: Long?): Long = profile ?: OWN

    fun profileOf(column: Long): Long? = column.takeIf { it != OWN }
}
