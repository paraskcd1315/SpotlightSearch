package com.paraskcd.spotlightsearch.sources.infrastructure.apps

import android.content.Context
import android.content.pm.LauncherApps
import android.os.UserHandle
import android.util.Log
import com.paraskcd.spotlightsearch.sources.domain.model.InstalledApp
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PackageAppsSource @Inject constructor(
    @ApplicationContext context: Context,
    private val profiles: UserProfiles
) {
    private val launcherApps = context.getSystemService(LauncherApps::class.java)

    fun load(): List<InstalledApp> = profiles.all().flatMap(::appsOf).distinct()

    private fun appsOf(user: UserHandle): List<InstalledApp> {
        val profile = profiles.profileOf(user)
        return runCatching { launcherApps.getActivityList(null, user) }
            .onFailure { Log.w(TAG, "No app list for $user", it) }
            .getOrDefault(emptyList())
            .map { info -> InstalledApp(info.applicationInfo.packageName, info.label.toString(), profile) }
    }

    private companion object {
        const val TAG = "PackageAppsSource"
    }
}
