package com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.paraskcd.spotlightsearch.preferences.data.ProfileColumn

@Entity(tableName = "blacklist_apps")
data class BlacklistAppsEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val packageName: String,
    @ColumnInfo(defaultValue = ProfileColumn.OWN_SQL) val profile: Long = ProfileColumn.OWN
)
