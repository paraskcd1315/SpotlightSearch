package com.paraskcd.spotlightsearch.preferences.presentation.utils

object IconNameRanking {
    private val NON_ALPHANUMERIC = Regex("[^a-z0-9]")

    fun rank(names: List<String>, appLabel: String, query: String): List<String> {
        val needle = normalize(query)
        if (needle.isNotEmpty()) return names.filter { needle in normalize(it) }
        val label = normalize(appLabel)
        if (label.isEmpty()) return names
        val (matching, rest) = names.partition { name -> normalize(name).let { label in it || (it.isNotEmpty() && it in label) } }
        return matching + rest
    }

    private fun normalize(value: String) = value.lowercase().replace(NON_ALPHANUMERIC, "")
}
