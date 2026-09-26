package com.paraskcd.spotlightsearch.sources.domain.repository

import com.paraskcd.spotlightsearch.sources.domain.model.actions.HitAction

interface ActionRunner {
    fun run(action: HitAction)
}
