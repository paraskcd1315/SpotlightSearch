package com.paraskcd.spotlightsearch.sources.domain.model

enum class WebSearchEngine(val urlTemplate: String?) {
    SYSTEM(null),
    GOOGLE("https://www.google.com/search?q=%s"),
    DUCKDUCKGO("https://duckduckgo.com/?q=%s"),
    BING("https://www.bing.com/search?q=%s"),
    BRAVE("https://search.brave.com/search?q=%s"),
    ECOSIA("https://www.ecosia.org/search?q=%s"),
    STARTPAGE("https://www.startpage.com/do/search?q=%s")
}
