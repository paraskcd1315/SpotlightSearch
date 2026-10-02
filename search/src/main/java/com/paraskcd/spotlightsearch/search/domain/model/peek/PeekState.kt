package com.paraskcd.spotlightsearch.search.domain.model.peek

data class PeekState(val phase: PeekPhase, val progress: Float, val distancePx: Float = 0f)
