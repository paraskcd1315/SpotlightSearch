package com.paraskcd.spotlightsearch.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.paraskcd.spotlightsearch.data.entities.AppUsageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppUsageDao {
    @Query("SELECT * FROM app_usage ORDER BY openCount DESC, lastOpenedAt DESC LIMIT :limit")
    suspend fun getTopAppUsages(limit: Int): List<AppUsageEntity>

    @Query("SELECT * FROM app_usage ORDER BY openCount DESC, lastOpenedAt DESC LIMIT :limit")
    fun observeTopAppUsages(limit: Int): Flow<List<AppUsageEntity>>

    @Query("SELECT * FROM app_usage WHERE packageName = :pkg LIMIT 1")
    suspend fun get(pkg: String): AppUsageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: AppUsageEntity)

    @Transaction
    suspend fun increment(pkg: String, now: Long) {
        val current = get(pkg)
        val updated = if (current == null) {
            AppUsageEntity(packageName = pkg, openCount = 1, lastOpenedAt = now)
        } else {
            current.copy(openCount = current.openCount + 1, lastOpenedAt = now)
        }
        upsert(updated)
    }
}