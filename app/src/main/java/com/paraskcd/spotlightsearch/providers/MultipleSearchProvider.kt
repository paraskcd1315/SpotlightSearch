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
import com.paraskcd.spotlightsearch.data.repo.QuickSearchProviderRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Singleton
class MultipleSearchProvider @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val repo: QuickSearchProviderRepository
) {
    private data class ProviderDef(
        val packageName: String,
        val titleBuilder: (String) -> String,
        val subtitle: String,
        val intentBuilder: (String) -> Intent,
        val consumesQuery: Boolean = true
    )

    private val pm get() = context.packageManager

    private fun isInstalled(pkg: String) = try { pm.getApplicationInfo(pkg, 0); true } catch (_: Exception) { false }
    private fun iconOrNull(pkg: String): Drawable? = try { pm.getApplicationIcon(pkg) } catch (_: Exception) { null }

    private val providers = listOf(
        ProviderDef(
            "com.google.android.googlequicksearchbox",
            { q -> "Search \"$q\" on Google" },
            "Google",
            { q -> Intent(Intent.ACTION_WEB_SEARCH).apply {
                putExtra(SearchManager.QUERY, q)
                setPackage("com.google.android.googlequicksearchbox")
            }}
        ),
        ProviderDef(
            "com.google.android.youtube",
            { q -> "Search \"$q\" on YouTube" },
            "YouTube",
            { q -> Intent(Intent.ACTION_VIEW).apply {
                data = "https://www.youtube.com/results?search_query=${Uri.encode(q)}".toUri()
                setPackage("com.google.android.youtube")
            }}
        ),
        ProviderDef(
            "com.google.android.apps.youtube.music",
            { q -> "Search \"$q\" on YouTube Music" },
            "YouTube Music",
            { q -> Intent(Intent.ACTION_VIEW).apply {
                data = "https://music.youtube.com/search?q=${Uri.encode(q)}".toUri()
                setPackage("com.google.android.apps.youtube.music")
            }}
        ),
        ProviderDef(
            "com.google.android.apps.maps",
            { q -> "Search \"$q\" on Maps" },
            "Google Maps",
            { q -> Intent(Intent.ACTION_VIEW).apply {
                data = "geo:0,0?q=${Uri.encode(q)}".toUri()
                setPackage("com.google.android.apps.maps")
            }}
        ),
        ProviderDef(
            "com.android.vending",
            { q -> "Search \"$q\" on the Play Store" },
            "Play Store",
            { q -> Intent(Intent.ACTION_VIEW).apply {
                data = "https://play.google.com/store/search?q=${Uri.encode(q)}&c=apps".toUri()
                setPackage("com.android.vending")
            }}
        ),
        ProviderDef(
            "com.instagram.barcelona",
            { q -> "Search \"$q\" on Threads" },
            "Threads",
            { q -> Intent(Intent.ACTION_VIEW).apply {
                data = "https://www.threads.net/search?q=${Uri.encode(q)}".toUri()
                setPackage("com.instagram.barcelona")
            }}
        ),
        ProviderDef(
            "com.linkedin.android",
            { q -> "Search \"$q\" on LinkedIn" },
            "LinkedIn",
            { q -> Intent(Intent.ACTION_VIEW).apply {
                data = "https://www.linkedin.com/search/results/all/?keywords=${Uri.encode(q)}".toUri()
                setPackage("com.linkedin.android")
            }}
        ),
        ProviderDef(
            "com.twitter.android",
            { q -> "Search \"$q\" on X" },
            "X (Twitter)",
            { q -> Intent(Intent.ACTION_VIEW).apply {
                data = "https://twitter.com/search?q=${Uri.encode(q)}".toUri()
                setPackage("com.twitter.android")
            }}
        ),
        ProviderDef(
            "com.facebook.katana",
            { q -> "Search \"$q\" on Facebook" },
            "Facebook",
            { q -> Intent(Intent.ACTION_VIEW).apply {
                data = "https://www.facebook.com/search/top/?q=${Uri.encode(q)}".toUri()
                setPackage("com.facebook.katana")
            }}
        )
    ).associateBy { it.packageName }

    fun getSearchAppItems(query: String): List<SearchResult> {
        if (query.isBlank()) return emptyList()

        val configs = runBlocking {
            repo.ensureDefaults()
            repo.observe().first()
        }

        val list = configs
            .filter { it.enabled }
            .sortedBy { it.sortOrder }
            .mapNotNull { cfg ->
                val def = providers[cfg.packageName] ?: return@mapNotNull null
                if (!isInstalled(def.packageName)) return@mapNotNull null
                SearchResult(
                    title = def.titleBuilder(query),
                    subtitle = def.subtitle,
                    icon = iconOrNull(def.packageName),
                    onClick = {
                        try {
                            val intent = def.intentBuilder(query)
                            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                        } catch (_: Exception) {}
                    },
                    searchResultType = SearchResultType.WEB
                )
            }

        if (list.isEmpty()) return emptyList()
        return listOf(SearchResult("App Searches", onClick = {}, isHeader = true)) + list
    }
}