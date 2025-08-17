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
        outlineColor: Int? = null
    ) {
        val current = get() ?: UserThemeEntity()
        val updated = current.copy(
            theme = mode ?: current.theme,
            enableBlur = enableBlur ?: current.enableBlur,
            iconPack = iconPack ?: current.iconPack,
            surfaceColor = surfaceColor ?: current.surfaceColor,
            surfaceBrightColor = surfaceBrightColor ?: current.surfaceBrightColor,
            backgroundColor = backgroundColor ?: current.backgroundColor,
            surfaceTintColor = surfaceTintColor ?: current.surfaceTintColor,
            onSurfaceColor = onSurfaceColor ?: current.onSurfaceColor,
            outlineColor = outlineColor ?: current.outlineColor
        )
        upsert(updated)
    }
}