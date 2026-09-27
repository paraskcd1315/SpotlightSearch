package com.paraskcd.spotlightsearch.sources.domain.model

data class InstalledApp(val packageName: String, val label: String, val profile: Long? = null) {
    val key: AppKey get() = AppKey(packageName, profile)
}
