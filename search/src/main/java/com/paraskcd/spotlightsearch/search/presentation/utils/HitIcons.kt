package com.paraskcd.spotlightsearch.search.presentation.utils

import androidx.compose.ui.graphics.vector.ImageVector
import com.composables.icons.lucide.Calculator
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.Languages
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Search
import com.composables.icons.lucide.Settings
import com.composables.icons.lucide.SpellCheck
import com.composables.icons.lucide.User
import com.paraskcd.spotlightsearch.sources.domain.model.hits.AppHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.CalculationHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.ContactHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.ContactsPermissionHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.DeviceSettingHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.QuickSearchHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.SearchHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.SpellingHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.SuggestionHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.TranslationHit
import com.paraskcd.spotlightsearch.sources.domain.model.hits.WebSearchHit

fun hitIcon(hit: SearchHit): ImageVector = when (hit) {
    is CalculationHit -> Lucide.Calculator
    is TranslationHit -> Lucide.Languages
    is DeviceSettingHit -> Lucide.Settings
    ContactsPermissionHit -> Lucide.CircleAlert
    is ContactHit -> Lucide.User
    is SpellingHit -> Lucide.SpellCheck
    is AppHit, is QuickSearchHit, is SuggestionHit, is WebSearchHit -> Lucide.Search
}
