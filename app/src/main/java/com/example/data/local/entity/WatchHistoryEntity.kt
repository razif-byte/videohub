package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watch_history")
data class WatchHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val videoId: String,
    val provider: String,
    val title: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val channelName: String,
    val duration: String,
    val durationSeconds: Long = 0L,
    val position: Long = 0L,
    val lastWatchedAt: Long = System.currentTimeMillis()
)
