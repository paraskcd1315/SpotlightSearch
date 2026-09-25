package com.paraskcd.spotlightsearch.search.presentation.model

sealed interface HitOutcome {
    data object Close : HitOutcome
    data object Stay : HitOutcome
    data class ReplaceQuery(val query: String) : HitOutcome
}
