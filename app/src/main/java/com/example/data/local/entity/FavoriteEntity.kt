package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val videoId: String,
    val provider: String,
    val title: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val channelName: String,
    val duration: String,
    val createdAt: Long = System.currentTimeMillis()
)
