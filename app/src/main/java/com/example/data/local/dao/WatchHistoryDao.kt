package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.WatchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(history: WatchHistoryEntity): Long

    @Query("SELECT * FROM watch_history ORDER BY lastWatchedAt DESC")
    fun getAllHistory(): Flow<List<WatchHistoryEntity>>

    @Query("SELECT * FROM watch_history ORDER BY lastWatchedAt DESC LIMIT :limit")
    fun getRecentHistory(limit: Int = 10): Flow<List<WatchHistoryEntity>>

    @Query("SELECT * FROM watch_history WHERE videoId = :videoId AND provider = :provider LIMIT 1")
    suspend fun getHistoryItem(videoId: String, provider: String): WatchHistoryEntity?

    @Query("UPDATE watch_history SET position = :position, lastWatchedAt = :timestamp WHERE videoId = :videoId AND provider = :provider")
    suspend fun updatePosition(videoId: String, provider: String, position: Long, timestamp: Long = System.currentTimeMillis())

    @Query("DELETE FROM watch_history WHERE videoId = :videoId AND provider = :provider")
    suspend fun deleteItem(videoId: String, provider: String)

    @Query("DELETE FROM watch_history")
    suspend fun clearAllHistory()

    @Query("DELETE FROM watch_history WHERE id NOT IN (SELECT id FROM watch_history ORDER BY lastWatchedAt DESC LIMIT :maxItems)")
    suspend fun trimHistory(maxItems: Int = 1000)
}
