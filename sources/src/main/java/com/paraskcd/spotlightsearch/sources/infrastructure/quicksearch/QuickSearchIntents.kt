package com.paraskcd.spotlightsearch.sources.infrastructure.quicksearch

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import com.paraskcd.spotlightsearch.sources.domain.model.QuickSearchService

object QuickSearchIntents {
    fun build(service: QuickSearchService, query: String): Intent {
        val encoded = Uri.encode(query)
        val intent = when (service) {
            QuickSearchService.GOOGLE -> Intent(Intent.ACTION_WEB_SEARCH).putExtra(SearchManager.QUERY, query)
            QuickSearchService.YOUTUBE -> view("https://www.youtube.com/results?search_query=$encoded")
            QuickSearchService.YOUTUBE_MUSIC -> view("https://music.youtube.com/search?q=$encoded")
            QuickSearchService.MAPS -> view("geo:0,0?q=$encoded")
            QuickSearchService.PLAY_STORE -> view("https://play.google.com/store/search?q=$encoded&c=apps")
            QuickSearchService.THREADS -> view("https://www.threads.net/search?q=$encoded")
            QuickSearchService.LINKEDIN -> view("https://www.linkedin.com/search/results/all/?keywords=$encoded")
            QuickSearchService.X -> view("https://twitter.com/search?q=$encoded")
            QuickSearchService.FACEBOOK -> view("https://www.facebook.com/search/top/?q=$encoded")
        }
        return intent.setPackage(service.packageName)
    }

    private fun view(uri: String) = Intent(Intent.ACTION_VIEW, uri.toUri())
}
