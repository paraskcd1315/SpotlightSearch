package com.paraskcd.spotlightsearch.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paraskcd.spotlightsearch.data.entities.BlacklistAppsEntity
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