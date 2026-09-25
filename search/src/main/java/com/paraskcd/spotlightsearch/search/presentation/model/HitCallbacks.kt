package com.paraskcd.spotlightsearch.search.presentation.model

import com.paraskcd.spotlightsearch.sources.domain.model.actions.HitAction
import com.paraskcd.spotlightsearch.sources.domain.model.hits.SearchHit

data class HitCallbacks(
    val onHitClick: (SearchHit) -> Unit,
    val onAction: (HitAction) -> Unit
)
