package com.paraskcd.spotlightsearch.sources.domain.model.actions

data class ChangeAppIcon(val packageName: String, val profile: Long? = null) : HitAction
