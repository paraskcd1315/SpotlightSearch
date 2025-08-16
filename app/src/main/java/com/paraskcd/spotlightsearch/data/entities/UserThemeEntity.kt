package com.paraskcd.spotlightsearch.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.paraskcd.spotlightsearch.enums.ThemeMode

@Entity(tableName = "user_theme")
data class UserThemeEntity(
    @PrimaryKey val id: Int = 0,
    val theme: ThemeMode = ThemeMode.AUTO,
    val enableBlur: Boolean = false,
    val surfaceColor: Long? = null,
    val surfaceBrightColor: Long? = null,
    val backgroundColor: Long? = null,
    val surfaceTintColor: Long? = null,
    val onSurfaceColor: Long? = null,
    val outlineColor: Long? = null,
)
