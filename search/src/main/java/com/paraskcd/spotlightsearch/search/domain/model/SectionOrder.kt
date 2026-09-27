package com.paraskcd.spotlightsearch.search.domain.model

object SectionOrder {
    val configurable: List<SectionKind> = listOf(
        SectionKind.CALCULATOR,
        SectionKind.TRANSLATION,
        SectionKind.TOP_HIT,
        SectionKind.SETTINGS,
        SectionKind.DICTIONARY,
        SectionKind.WEB,
        SectionKind.APPS,
        SectionKind.CONTACTS,
        SectionKind.SUGGESTIONS,
        SectionKind.QUICK_SEARCH
    )

    fun normalize(saved: List<SectionKind>): List<SectionKind> =
        (saved.filter { it in configurable } + configurable).distinct()

    fun rank(order: List<SectionKind>, kind: SectionKind): Int = when (kind) {
        SectionKind.FREQUENT -> -1
        SectionKind.PERMISSIONS -> order.size
        else -> order.indexOf(kind).takeIf { it >= 0 } ?: order.size
    }
}
