package com.paraskcd.spotlightsearch.providers

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import com.paraskcd.spotlightsearch.types.SearchResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayStoreSearchProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getPlayStoreSearchItem(query: String): SearchResult? {
        val pm: PackageManager = context.packageManager
        return try {
            val appInfo = pm.getApplicationInfo("com.android.vending", 0)
            val icon: Drawable = pm.getApplicationIcon(appInfo)

            SearchResult(
                title = "Search \"$query\" on the Play Store",
                subtitle = "Play Store",
                icon = icon,
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setData(Uri.parse("https://play.google.com/store/search?q=${Uri.encode(query)}&c=apps"))
                        setPackage("com.android.vending")
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(intent)
                }
            )
        } catch (e: Exception) {
            null // Play Store not installed or something failed
        }
    }
}