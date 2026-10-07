package com.example.domain.model

data class VideoItem(
    val id: String,
    val title: String,
    val description: String,
    val thumbnailUrl: String,
    val provider: VideoProviderType,
    val videoUrl: String,
    val duration: String,
    val publishedAt: String,
    val channelName: String,
    val channelId: String? = null,
    val isFavorite: Boolean = false,
    val lastPosition: Long = 0L,
    val durationSeconds: Long = 0L
) {
    val progressPercentage: Float
        get() = if (durationSeconds > 0) {
            (lastPosition.toFloat() / durationSeconds.toFloat()).coerceIn(0f, 1f)
        } else 0f
}
