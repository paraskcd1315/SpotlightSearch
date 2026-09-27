package com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity.AppIconEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppIconDao {
    @Query("SELECT * FROM app_icons")
    fun observe(): Flow<List<AppIconEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: AppIconEntity)

    @Query("DELETE FROM app_icons WHERE packageName = :pkg AND profile = :profile")
    suspend fun delete(pkg: String, profile: Long)
}
