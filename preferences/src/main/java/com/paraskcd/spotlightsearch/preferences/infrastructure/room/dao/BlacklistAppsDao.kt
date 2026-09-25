package com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity.BlacklistAppsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BlacklistAppsDao {
    @Query("SELECT * FROM blacklist_apps")
    fun observe(): Flow<List<BlacklistAppsEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: BlacklistAppsEntity)

    @Query("DELETE FROM blacklist_apps WHERE packageName = :pkg")
    suspend fun delete(pkg: String)
}