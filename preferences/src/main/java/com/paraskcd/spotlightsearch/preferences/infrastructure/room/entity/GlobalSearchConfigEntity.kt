package com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "global_search_config")
data class GlobalSearchConfigEntity(
    @PrimaryKey val id: Int = 0,
    val appsEnabled: Boolean = true,
    val contactsEnabled: Boolean = true,
    val webSuggestionsEnabled: Boolean = true
)
