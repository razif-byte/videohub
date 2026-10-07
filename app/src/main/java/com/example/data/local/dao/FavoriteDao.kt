package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity): Long

    @Query("DELETE FROM favorites WHERE videoId = :videoId AND provider = :provider")
    suspend fun deleteFavorite(videoId: String, provider: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE videoId = :videoId AND provider = :provider)")
    fun isFavorite(videoId: String, provider: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE videoId = :videoId AND provider = :provider)")
    suspend fun isFavoriteSync(videoId: String, provider: String): Boolean

    @Query("SELECT * FROM favorites ORDER BY createdAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites WHERE id = :id LIMIT 1")
    suspend fun getFavoriteById(id: Long): FavoriteEntity?

    @Query("DELETE FROM favorites")
    suspend fun clearAllFavorites()
}
