package com.paraskcd.spotlightsearch.preferences.domain.repository

import com.paraskcd.spotlightsearch.preferences.domain.model.ColorOverrideKey
import com.paraskcd.spotlightsearch.preferences.domain.model.ThemeMode
import com.paraskcd.spotlightsearch.preferences.domain.model.ThemeSettings
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    fun settings(): Flow<ThemeSettings>
    suspend fun setMode(mode: ThemeMode)
    suspend fun setBlur(enabled: Boolean)
    suspend fun setColor(key: ColorOverrideKey, argb: Int)
    suspend fun clearColor(key: ColorOverrideKey)
    suspend fun clearColors()
}
