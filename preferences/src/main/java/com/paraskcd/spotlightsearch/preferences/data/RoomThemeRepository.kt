package com.paraskcd.spotlightsearch.preferences.data

import com.paraskcd.spotlightsearch.preferences.domain.model.ColorOverrideKey
import com.paraskcd.spotlightsearch.preferences.domain.model.GlassStrength
import com.paraskcd.spotlightsearch.preferences.domain.model.TextSize
import com.paraskcd.spotlightsearch.search.domain.model.AppResultsLayout
import com.paraskcd.spotlightsearch.preferences.domain.model.ThemeMode
import com.paraskcd.spotlightsearch.preferences.domain.model.ThemeSettings
import com.paraskcd.spotlightsearch.preferences.domain.repository.ThemeRepository
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao.UserThemeDao
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity.UserThemeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomThemeRepository @Inject constructor(private val dao: UserThemeDao) : ThemeRepository {
    override fun settings(): Flow<ThemeSettings> = dao.observe().map { it.toSettings() }

    override suspend fun setMode(mode: ThemeMode) = dao.merge(mode = mode)

    override suspend fun setBlur(enabled: Boolean) = dao.merge(enableBlur = enabled)

    override suspend fun setBranding(visible: Boolean) = dao.merge(showBranding = visible)

    override suspend fun setGlassStrength(strength: GlassStrength) = dao.merge(glassStrength = strength.name)

    override suspend fun setTextSize(size: TextSize) = dao.merge(textSize = size.name)

    override suspend fun setAppLayout(layout: AppResultsLayout) = dao.merge(appLayout = layout.name)

    override suspend fun setColor(key: ColorOverrideKey, argb: Int) = when (key) {
        ColorOverrideKey.background -> dao.merge(backgroundColor = argb)
        ColorOverrideKey.surfaceBright -> dao.merge(surfaceBrightColor = argb)
        ColorOverrideKey.surfaceTint -> dao.merge(surfaceTintColor = argb)
        ColorOverrideKey.onSurface -> dao.merge(onSurfaceColor = argb)
        ColorOverrideKey.outline -> dao.merge(outlineColor = argb)
    }

    override suspend fun clearColor(key: ColorOverrideKey) = when (key) {
        ColorOverrideKey.background -> dao.merge(clearBackground = true)
        ColorOverrideKey.surfaceBright -> dao.merge(clearSurfaceBright = true)
        ColorOverrideKey.surfaceTint -> dao.merge(clearSurfaceTint = true)
        ColorOverrideKey.onSurface -> dao.merge(clearOnSurface = true)
        ColorOverrideKey.outline -> dao.merge(clearOutline = true)
    }

    override suspend fun clearColors() {
        if (dao.get() == null) dao.upsert(UserThemeEntity()) else dao.clearColors()
    }

    private fun UserThemeEntity?.toSettings(): ThemeSettings {
        if (this == null) return ThemeSettings()
        val colors = buildMap {
            backgroundColor?.let { put(ColorOverrideKey.background, it) }
            surfaceBrightColor?.let { put(ColorOverrideKey.surfaceBright, it) }
            surfaceTintColor?.let { put(ColorOverrideKey.surfaceTint, it) }
            onSurfaceColor?.let { put(ColorOverrideKey.onSurface, it) }
            outlineColor?.let { put(ColorOverrideKey.outline, it) }
        }
        return ThemeSettings(
            mode = theme,
            blurEnabled = enableBlur ?: true,
            showBranding = showBranding ?: true,
            glassStrength = GlassStrength.entries.firstOrNull { it.name == glassStrength } ?: GlassStrength.MEDIUM,
            textSize = TextSize.entries.firstOrNull { it.name == textSize } ?: TextSize.DEFAULT,
            appLayout = AppResultsLayout.entries.firstOrNull { it.name == appLayout } ?: AppResultsLayout.LIST,
            colors = colors
        )
    }
}
