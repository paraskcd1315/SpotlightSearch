package com.paraskcd.spotlightsearch.search.presentation.utils

import androidx.compose.runtime.staticCompositionLocalOf
import com.paraskcd.spotlightsearch.search.domain.model.AppIconChoice
import com.paraskcd.spotlightsearch.sources.domain.model.AppKey

val LocalAppIcons = staticCompositionLocalOf<Map<AppKey, AppIconChoice>> { emptyMap() }
