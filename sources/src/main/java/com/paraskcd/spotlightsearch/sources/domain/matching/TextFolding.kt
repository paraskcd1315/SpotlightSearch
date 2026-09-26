package com.paraskcd.spotlightsearch.sources.domain.matching

import java.text.Normalizer
import java.util.Locale

private val CombiningMarks = Regex("\\p{Mn}+")

fun String.foldForSearch(): String =
    CombiningMarks.replace(Normalizer.normalize(this, Normalizer.Form.NFD), "").lowercase(Locale.ROOT)
