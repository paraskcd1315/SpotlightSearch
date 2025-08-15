package com.paraskcd.spotlightsearch.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_usage")
data class AppUsageEntity(
    @PrimaryKey val packageName: String,
    val openCount: Long,
    val lastOpenedAt: Long
)
