package com.paraskcd.spotlightsearch.search.presentation.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import com.paraskcd.spotlightsearch.designsystem.icons.Calculate
import com.paraskcd.spotlightsearch.designsystem.icons.Translate
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
    is CalculationHit -> Calculate
    is TranslationHit -> Translate
    is DeviceSettingHit -> Icons.Filled.Settings
    ContactsPermissionHit -> Icons.Filled.Warning
    is ContactHit -> Icons.Filled.Person
    is AppHit, is QuickSearchHit, is SuggestionHit, is SpellingHit, is WebSearchHit -> Icons.Filled.Search
}
