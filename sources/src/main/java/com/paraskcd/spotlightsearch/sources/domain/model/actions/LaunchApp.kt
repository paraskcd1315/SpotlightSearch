package com.paraskcd.spotlightsearch.sources.domain.model.actions

data class LaunchApp(val packageName: String, val profile: Long? = null) : HitAction
