package com.paraskcd.spotlightsearch.sources.domain.model.actions

data class OpenAppInfo(val packageName: String, val profile: Long? = null) : HitAction
