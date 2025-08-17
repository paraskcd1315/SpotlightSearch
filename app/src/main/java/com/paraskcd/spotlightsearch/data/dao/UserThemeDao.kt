package com.paraskcd.spotlightsearch.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.paraskcd.spotlightsearch.data.entities.UserThemeEntity
import com.paraskcd.spotlightsearch.enums.ThemeMode
import kotlinx.coroutines.flow.Flow

@Dao
interface UserThemeDao {
    @Query("SELECT * FROM user_theme WHERE id = 0")
    suspend fun get(): UserThemeEntity?

    @Query("SELECT * FROM user_theme WHERE id = 0")
    fun observe(): Flow<UserThemeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: UserThemeEntity)

    @Query("UPDATE user_theme SET theme = :mode WHERE id = 0")
    suspend fun setMode(mode: ThemeMode)

    @Query("UPDATE user_theme SET enableBlur = :enabled WHERE id = 0")
    suspend fun setBlur(enabled: Boolean)

    @Query("UPDATE user_theme SET iconPack = :pkg WHERE id = 0")
    suspend fun setIconPack(pkg: String?)

    @Query("""
        UPDATE user_theme SET
            surfaceColor = NULL,
            surfaceBrightColor = NULL,
            backgroundColor = NULL,
            surfaceTintColor = NULL,
            onSurfaceColor = NULL,
            outlineColor = NULL
        WHERE id = 0
    """)
    suspend fun clearColors()

    @Transaction
    suspend fun merge(
        mode: ThemeMode? = null,
        enableBlur: Boolean? = null,
        iconPack: String? = null,
        surfaceColor: Int? = null,
        surfaceBrightColor: Int? = null,
        backgroundColor: Int? = null,
        surfaceTintColor: Int? = null,
        onSurfaceColor: Int? = null,
        outlineColor: Int? = null,
        clearSurface: Boolean = false,
        clearSurfaceBright: Boolean = false,
        clearBackground: Boolean = false,
        clearSurfaceTint: Boolean = false,
        clearOnSurface: Boolean = false,
        clearOutline: Boolean = false
    ) {
        val current = get() ?: UserThemeEntity()
        val updated = current.copy(
            theme = mode ?: current.theme,
            enableBlur = enableBlur ?: current.enableBlur,
            iconPack = iconPack ?: current.iconPack,
            surfaceColor = when {
                clearSurface -> null
                surfaceColor != null -> surfaceColor
                else -> current.surfaceColor
            },
            surfaceBrightColor = when {
                clearSurfaceBright -> null
                surfaceBrightColor != null -> surfaceBrightColor
                else -> current.surfaceBrightColor
            },
            backgroundColor = when {
                clearBackground -> null
                backgroundColor != null -> backgroundColor
                else -> current.backgroundColor
            },
            surfaceTintColor = when {
                clearSurfaceTint -> null
                surfaceTintColor != null -> surfaceTintColor
                else -> current.surfaceTintColor
            },
            onSurfaceColor = when {
                clearOnSurface -> null
                onSurfaceColor != null -> onSurfaceColor
                else -> current.onSurfaceColor
            },
            outlineColor = when {
                clearOutline -> null
                outlineColor != null -> outlineColor
                else -> current.outlineColor
            }
        )
        upsert(updated)
    }
}