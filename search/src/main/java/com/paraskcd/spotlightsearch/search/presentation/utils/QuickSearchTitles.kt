package com.paraskcd.spotlightsearch.search.presentation.utils

import androidx.annotation.StringRes
import com.paraskcd.spotlightsearch.search.R
import com.paraskcd.spotlightsearch.sources.domain.model.QuickSearchService

@StringRes
fun QuickSearchService.titleRes(): Int = when (this) {
    QuickSearchService.GOOGLE -> R.string.quick_search_google
    QuickSearchService.YOUTUBE -> R.string.quick_search_youtube
    QuickSearchService.YOUTUBE_MUSIC -> R.string.quick_search_youtube_music
    QuickSearchService.MAPS -> R.string.quick_search_maps
    QuickSearchService.PLAY_STORE -> R.string.quick_search_play_store
    QuickSearchService.THREADS -> R.string.quick_search_threads
    QuickSearchService.LINKEDIN -> R.string.quick_search_linkedin
    QuickSearchService.X -> R.string.quick_search_x
    QuickSearchService.FACEBOOK -> R.string.quick_search_facebook
}

@StringRes
fun QuickSearchService.nameRes(): Int = when (this) {
    QuickSearchService.GOOGLE -> R.string.service_google
    QuickSearchService.YOUTUBE -> R.string.service_youtube
    QuickSearchService.YOUTUBE_MUSIC -> R.string.service_youtube_music
    QuickSearchService.MAPS -> R.string.service_maps
    QuickSearchService.PLAY_STORE -> R.string.service_play_store
    QuickSearchService.THREADS -> R.string.service_threads
    QuickSearchService.LINKEDIN -> R.string.service_linkedin
    QuickSearchService.X -> R.string.service_x
    QuickSearchService.FACEBOOK -> R.string.service_facebook
}
