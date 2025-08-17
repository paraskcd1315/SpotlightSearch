package com.paraskcd.spotlightsearch.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quick_search_provider")
data class QuickSearchProviderEntity(
    @PrimaryKey val packageName: String,
    val enabled: Boolean,
    val sortOrder: Int = 0
)
