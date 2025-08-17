package com.paraskcd.spotlightsearch.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.paraskcd.spotlightsearch.enums.ThemeMode

@Entity(tableName = "user_theme")
data class UserThemeEntity(
    @PrimaryKey val id: Int = 0,
    val theme: ThemeMode = ThemeMode.AUTO,
    val enableBlur: Boolean? = null,
    val surfaceColor: Int? = null,
    val surfaceBrightColor: Int? = null,
    val backgroundColor: Int? = null,
    val surfaceTintColor: Int? = null,
    val onSurfaceColor: Int? = null,
    val outlineColor: Int? = null,
    val iconPack: String? = null
)
