package com.paraskcd.spotlightsearch.data.repo

import com.paraskcd.spotlightsearch.data.dao.UserThemeDao
import com.paraskcd.spotlightsearch.data.entities.UserThemeEntity
import com.paraskcd.spotlightsearch.enums.ColorOverrideKey
import com.paraskcd.spotlightsearch.enums.ThemeMode
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserThemeRepository @Inject constructor(
    private val dao: UserThemeDao
) {
    fun observe(): Flow<UserThemeEntity?> = dao.observe()
    suspend fun get(): UserThemeEntity? = dao.get()
    suspend fun setMode(mode: ThemeMode) {
        ensureRow()
        dao.setMode(mode)
    }
    suspend fun setBlur(enabled: Boolean) {
        ensureRow()
        dao.setBlur(enabled)
    }
    suspend fun setIconPack(pkg: String?) {
        ensureRow()
        dao.setIconPack(pkg)
    }

    suspend fun mergeIconPack(pkg: String?) = dao.merge(iconPack = pkg)

    suspend fun merge(
        mode: ThemeMode? = null,
        enableBlur: Boolean? = null,
        surface: Int? = null,
        surfaceBright: Int? = null,
        background: Int? = null,
        surfaceTint: Int? = null,
        onSurface: Int? = null,
        outline: Int? = null
    ) = dao.merge(
        mode = mode,
        enableBlur = enableBlur,
        surfaceColor = surface,
        surfaceBrightColor = surfaceBright,
        backgroundColor = background,
        surfaceTintColor = surfaceTint,
        onSurfaceColor = onSurface,
        outlineColor = outline
    )

    suspend fun resetColors() {
        ensureRow()
        dao.clearColors()
    }

    private suspend fun ensureRow() {
        if (dao.get() == null) {
            dao.upsert(UserThemeEntity())
        }
    }

    suspend fun clearSingle(key: ColorOverrideKey) {
        ensureRow()
        when (key) {
            ColorOverrideKey.surface ->
                dao.merge(clearSurface = true)
            ColorOverrideKey.surfaceBright ->
                dao.merge(clearSurfaceBright = true)
            ColorOverrideKey.background ->
                dao.merge(clearBackground = true)
            ColorOverrideKey.surfaceTint ->
                dao.merge(clearSurfaceTint = true)
            ColorOverrideKey.onSurface ->
                dao.merge(clearOnSurface = true)
            ColorOverrideKey.outline ->
                dao.merge(clearOutline = true)
        }
    }
}