package com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity

import androidx.room.Entity

@Entity(tableName = "app_icons", primaryKeys = ["packageName", "profile"])
data class AppIconEntity(
    val packageName: String,
    val profile: Long,
    val iconPack: String,
    val drawable: String
)
