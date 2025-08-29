package com.paraskcd.spotlightsearch.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blacklist_apps")
data class BlacklistAppsEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val packageName: String
)