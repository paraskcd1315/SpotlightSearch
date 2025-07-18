package com.paraskcd.spotlightsearch

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.paraskcd.spotlightsearch.types.SearchResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(@ApplicationContext val context: Context) {
    private val packageManager: PackageManager = context.packageManager

    private val cachedApps: List<SearchResult> by lazy {
        val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        apps.mapNotNull { appInfo ->
            val launchIntent = packageManager.getLaunchIntentForPackage(appInfo.packageName)
            if (launchIntent != null) {
                val label = appInfo.loadLabel(packageManager).toString()
                val icon = appInfo.loadIcon(packageManager)

                SearchResult(
                    title = label,
                    subtitle = appInfo.packageName,
                    icon = icon,
                    onClick = {
                        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(launchIntent)
                    }
                )
            } else null
        }
    }

    fun searchInstalledApp(query: String): List<SearchResult> {
        if (query.isBlank()) return emptyList()
        return cachedApps.filter {
            it.title.contains(query, ignoreCase = true)
        }
    }
}