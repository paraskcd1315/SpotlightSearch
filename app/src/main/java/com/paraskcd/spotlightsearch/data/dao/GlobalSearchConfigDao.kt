package com.paraskcd.spotlightsearch.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paraskcd.spotlightsearch.data.entities.GlobalSearchConfigEntity
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