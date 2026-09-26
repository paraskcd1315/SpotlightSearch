package com.paraskcd.spotlightsearch.sources.domain.matching

object NameMatcher {
    fun match(name: String, foldedQuery: String): NameMatch? {
        if (foldedQuery.isEmpty()) return null
        val folded = name.foldForSearch()
        val aligned = folded.length == name.length
        fun highlight(ranges: List<IntRange>) = if (aligned) ranges else emptyList()

        if (folded == foldedQuery) return NameMatch(MatchTier.EXACT, highlight(listOf(folded.indices)))
        if (folded.startsWith(foldedQuery)) {
            return NameMatch(MatchTier.PREFIX, highlight(listOf(0 until foldedQuery.length)))
        }
        val starts = wordStarts(if (aligned) name else folded)
        starts.firstOrNull { folded.startsWith(foldedQuery, it) }?.let { start ->
            return NameMatch(MatchTier.WORD_PREFIX, highlight(listOf(start until start + foldedQuery.length)))
        }
        if (foldedQuery.length >= SearchThresholds.INITIALS_MIN_LENGTH) {
            initials(folded, starts, foldedQuery)?.let { return NameMatch(MatchTier.INITIALS, highlight(it)) }
        }
        if (foldedQuery.length >= SearchThresholds.CONTAINS_MIN_LENGTH) {
            val index = folded.indexOf(foldedQuery)
            if (index >= 0) return NameMatch(MatchTier.CONTAINS, highlight(listOf(index until index + foldedQuery.length)))
        }
        return null
    }

    private fun initials(folded: String, starts: List<Int>, query: String): List<IntRange>? {
        if (starts.size < query.length) return null
        val matched = mutableListOf<IntRange>()
        var next = 0
        for (start in starts) {
            if (next == query.length) break
            if (folded[start] == query[next]) {
                matched += start..start
                next++
            }
        }
        return matched.takeIf { next == query.length }
    }

    private fun wordStarts(text: String): List<Int> = text.indices.filter { index ->
        val current = text[index]
        if (!current.isLetterOrDigit()) return@filter false
        if (index == 0) return@filter true
        val previous = text[index - 1]
        !previous.isLetterOrDigit() || (previous.isLowerCase() && current.isUpperCase())
    }
}
