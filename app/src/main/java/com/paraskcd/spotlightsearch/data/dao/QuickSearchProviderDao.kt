package com.paraskcd.spotlightsearch.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.paraskcd.spotlightsearch.data.entities.QuickSearchProviderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuickSearchProviderDao {
    @Query("SELECT * FROM quick_search_provider ORDER BY sortOrder ASC")
    fun observeAll(): Flow<List<QuickSearchProviderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(list: List<QuickSearchProviderEntity>)

    @Query("UPDATE quick_search_provider SET enabled = :enabled WHERE packageName = :pkg")
    suspend fun setEnabled(pkg: String, enabled: Boolean)

    @Query("UPDATE quick_search_provider SET sortOrder = :order WHERE packageName = :pkg")
    suspend fun setOrder(pkg: String, order: Int)

    @Transaction
    suspend fun reorder(packagesInOrder: List<String>) {
        packagesInOrder.forEachIndexed { index, pkg ->
            setOrder(pkg, index)
        }
    }
}