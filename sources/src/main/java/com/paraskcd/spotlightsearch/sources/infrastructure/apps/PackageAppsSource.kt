package com.paraskcd.spotlightsearch.sources.infrastructure.apps

import android.content.Context
import android.content.Intent
import com.paraskcd.spotlightsearch.sources.domain.model.InstalledApp
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PackageAppsSource @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    fun load(): List<InstalledApp> {
        val packageManager = context.packageManager
        val launcherIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        return packageManager.queryIntentActivities(launcherIntent, 0)
            .map { info ->
                InstalledApp(
                    packageName = info.activityInfo.packageName,
                    label = info.activityInfo.loadLabel(packageManager).toString()
                )
            }
            .distinct()
    }
}
