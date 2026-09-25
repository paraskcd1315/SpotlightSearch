package com.paraskcd.spotlightsearch.sources.domain.model.hits

import com.paraskcd.spotlightsearch.sources.domain.model.CalculationKind

data class CalculationHit(
    val answer: String,
    val kind: CalculationKind,
    val detail: String? = null
) : SearchHit
