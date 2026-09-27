package com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity

import androidx.room.Entity

@Entity(tableName = "app_usage", primaryKeys = ["packageName", "profile"])
data class AppUsageEntity(
    val packageName: String,
    val profile: Long,
    val openCount: Long,
    val lastOpenedAt: Long
)
