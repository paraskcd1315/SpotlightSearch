package com.paraskcd.spotlightsearch.preferences.domain.repository

import com.paraskcd.spotlightsearch.preferences.domain.model.ColorOverrideKey
import com.paraskcd.spotlightsearch.preferences.domain.model.GlassStrength
import com.paraskcd.spotlightsearch.preferences.domain.model.TextSize
import com.paraskcd.spotlightsearch.search.domain.model.AppResultsLayout
import com.paraskcd.spotlightsearch.preferences.domain.model.ThemeMode
import com.paraskcd.spotlightsearch.preferences.domain.model.ThemeSettings
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    fun settings(): Flow<ThemeSettings>
    suspend fun setMode(mode: ThemeMode)
    suspend fun setBlur(enabled: Boolean)
    suspend fun setBranding(visible: Boolean)
    suspend fun setGlassStrength(strength: GlassStrength)
    suspend fun setTextSize(size: TextSize)
    suspend fun setAppLayout(layout: AppResultsLayout)
    suspend fun setColor(key: ColorOverrideKey, argb: Int)
    suspend fun clearColor(key: ColorOverrideKey)
    suspend fun clearColors()
}
