package com.paraskcd.spotlightsearch.sources.domain.model

enum class QuickSearchService(val packageName: String) {
    GOOGLE("com.google.android.googlequicksearchbox"),
    YOUTUBE("com.google.android.youtube"),
    YOUTUBE_MUSIC("com.google.android.apps.youtube.music"),
    MAPS("com.google.android.apps.maps"),
    PLAY_STORE("com.android.vending"),
    THREADS("com.instagram.barcelona"),
    LINKEDIN("com.linkedin.android"),
    X("com.twitter.android"),
    FACEBOOK("com.facebook.katana");

    companion object {
        fun fromPackage(packageName: String): QuickSearchService? =
            entries.firstOrNull { it.packageName == packageName }
    }
}
