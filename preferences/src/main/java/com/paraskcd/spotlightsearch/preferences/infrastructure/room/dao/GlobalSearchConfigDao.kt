package com.paraskcd.spotlightsearch.preferences.infrastructure.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paraskcd.spotlightsearch.preferences.infrastructure.room.entity.GlobalSearchConfigEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GlobalSearchConfigDao {
    @Query("SELECT * FROM global_search_config WHERE id = 0")
    fun observe(): Flow<GlobalSearchConfigEntity?>

    @Query("SELECT * FROM global_search_config WHERE id = 0")
    suspend fun get(): GlobalSearchConfigEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: GlobalSearchConfigEntity)

    suspend fun ensureDefault() {
        if (get() == null) insert(GlobalSearchConfigEntity())
    }
}