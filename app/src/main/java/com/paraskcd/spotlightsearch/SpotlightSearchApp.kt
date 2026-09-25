package com.paraskcd.spotlightsearch

import android.app.Application
import com.paraskcd.spotlightsearch.sources.domain.repository.InstalledAppsRepository
import com.paraskcd.spotlightsearch.sources.domain.repository.SpellingRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SpotlightSearchApp : Application() {
    @Inject lateinit var installedApps: InstalledAppsRepository
    @Inject lateinit var spelling: SpellingRepository

    override fun onCreate() {
        super.onCreate()
        installedApps.warmUp()
        spelling.warmUp()
    }
}
