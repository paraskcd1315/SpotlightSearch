package com.paraskcd.spotlightsearch.providers

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import com.paraskcd.spotlightsearch.enums.SearchResultType
import com.paraskcd.spotlightsearch.types.SearchResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.net.toUri

@Singleton
class MultipleSearchProvider @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    private val pm: PackageManager get() = context.packageManager

    private fun isInstalled(pkg: String): Boolean =
        try { pm.getApplicationInfo(pkg, 0); true } catch (_: Exception) { false }

    private fun iconOrNull(pkg: String): Drawable? =
        try { pm.getApplicationIcon(pkg) } catch (_: Exception) { null }

    private fun builder(
        packageName: String,
        titleBuilder: (String) -> String,
        subtitle: String,
        intentBuilder: (String) -> Intent,
        consumesQuery: Boolean = true
    ): (String) -> SearchResult? {
        return { q ->
            if (!isInstalled(packageName)) {
                null
            } else {
                val title = titleBuilder(q)
                val icon = iconOrNull(packageName)
                SearchResult(
                    title = title,
                    subtitle = subtitle,
                    icon = icon,
                    onClick = {
                        try {
                            val intent = if (consumesQuery) intentBuilder(q) else intentBuilder("")
                            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                        } catch (_: Exception) { /* ignorar */ }
                    },
                    searchResultType = SearchResultType.WEB
                )
            }
        }
    }

    val searchResultFactories: List<(String) -> SearchResult?> = listOf(
        // Google Search
        builder(
            packageName = "com.google.android.googlequicksearchbox",
            titleBuilder = { q -> "Search \"$q\" on Google" },
            subtitle = "Google",
            intentBuilder = { q ->
                Intent(Intent.ACTION_WEB_SEARCH).apply {
                    putExtra(SearchManager.QUERY, q)
                    setPackage("com.google.android.googlequicksearchbox")
                }
            }
        ),
        // YouTube
        builder(
            packageName = "com.google.android.youtube",
            titleBuilder = { q -> "Search \"$q\" on YouTube" },
            subtitle = "YouTube",
            intentBuilder = { q ->
                Intent(Intent.ACTION_VIEW).apply {
                    data = "https://www.youtube.com/results?search_query=${Uri.encode(q)}".toUri()
                    setPackage("com.google.android.youtube")
                }
            }
        ),
        // YouTube Music
        builder(
            packageName = "com.google.android.apps.youtube.music",
            titleBuilder = { q -> "Search \"$q\" on YouTube Music" },
            subtitle = "YouTube Music",
            intentBuilder = { q ->
                Intent(Intent.ACTION_VIEW).apply {
                    data = "https://music.youtube.com/search?q=${Uri.encode(q)}".toUri()
                    setPackage("com.google.android.apps.youtube.music")
                }
            }
        ),
        // Maps
        builder(
            packageName = "com.google.android.apps.maps",
            titleBuilder = { q -> "Search \"$q\" on Maps" },
            subtitle = "Google Maps",
            intentBuilder = { q ->
                Intent(Intent.ACTION_VIEW).apply {
                    data = "geo:0,0?q=${Uri.encode(q)}".toUri()
                    setPackage("com.google.android.apps.maps")
                }
            }
        ),
        // Play Store
        builder(
            packageName = "com.android.vending",
            titleBuilder = { q -> "Search \"$q\" on the Play Store" },
            subtitle = "Play Store",
            intentBuilder = { q ->
                Intent(Intent.ACTION_VIEW).apply {
                    data = "https://play.google.com/store/search?q=${Uri.encode(q)}&c=apps".toUri()
                    setPackage("com.android.vending")
                }
            }
        ),
        // Threads
        builder(
            packageName = "com.instagram.barcelona",
            titleBuilder = { q -> "Search \"$q\" on Threads" },
            subtitle = "Threads",
            intentBuilder = { q ->
                Intent(Intent.ACTION_VIEW).apply {
                    data = "https://www.threads.net/search?q=${Uri.encode(q)}".toUri()
                    setPackage("com.instagram.barcelona")
                }
            }
        ),
        // LinkedIn
        builder(
            packageName = "com.linkedin.android",
            titleBuilder = { q -> "Search \"$q\" on LinkedIn" },
            subtitle = "LinkedIn",
            intentBuilder = { q ->
                Intent(Intent.ACTION_VIEW).apply {
                    data =
                        "https://www.linkedin.com/search/results/all/?keywords=${Uri.encode(q)}".toUri()
                    setPackage("com.linkedin.android")
                }
            }
        ),
        // X (Twitter)
        builder(
            packageName = "com.twitter.android",
            titleBuilder = { q -> "Search \"$q\" on X" },
            subtitle = "X (Twitter)",
            intentBuilder = { q ->
                Intent(Intent.ACTION_VIEW).apply {
                    data = "https://twitter.com/search?q=${Uri.encode(q)}".toUri()
                    setPackage("com.twitter.android")
                }
            }
        ),
        // Facebook
        builder(
            packageName = "com.facebook.katana",
            titleBuilder = { q -> "Search \"$q\" on Facebook" },
            subtitle = "Facebook",
            intentBuilder = { q ->
                Intent(Intent.ACTION_VIEW).apply {
                    data = "https://www.facebook.com/search/top/?q=${Uri.encode(q)}".toUri()
                    setPackage("com.facebook.katana")
                }
            }
        )
    )

    fun getSearchAppItems(query: String): List<SearchResult> {
        if (query.isBlank()) return emptyList()
        val items = searchResultFactories.mapNotNull { it(query) }
        if (items.isEmpty()) return emptyList()
        return listOf(
            SearchResult(title = "App Searches", onClick = {}, isHeader = true)
        ) + items
    }
}