package com.paraskcd.spotlightsearch.search.presentation.utils

import androidx.annotation.StringRes
import com.paraskcd.spotlightsearch.search.R
import com.paraskcd.spotlightsearch.sources.domain.model.WebSearchEngine

@StringRes
fun WebSearchEngine.nameRes(): Int = when (this) {
    WebSearchEngine.SYSTEM -> R.string.engine_system
    WebSearchEngine.GOOGLE -> R.string.engine_google
    WebSearchEngine.DUCKDUCKGO -> R.string.engine_duckduckgo
    WebSearchEngine.BING -> R.string.engine_bing
    WebSearchEngine.BRAVE -> R.string.engine_brave
    WebSearchEngine.ECOSIA -> R.string.engine_ecosia
    WebSearchEngine.STARTPAGE -> R.string.engine_startpage
}
