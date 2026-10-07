package com.example.data.repository

import com.example.data.local.dao.FavoriteDao
import com.example.data.local.dao.WatchHistoryDao
import com.example.data.local.entity.FavoriteEntity
import com.example.data.local.entity.WatchHistoryEntity
import com.example.domain.model.VideoItem
import com.example.domain.model.VideoProviderType
import com.example.provider.ProviderManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VideoRepository(
    private val providerManager: ProviderManager,
    private val favoriteDao: FavoriteDao,
    private val watchHistoryDao: WatchHistoryDao
) {

    suspend fun getFeaturedVideos(): List<VideoItem> {
        return providerManager.getFeaturedVideos()
    }

    suspend fun getTrendingVideos(): Map<VideoProviderType, List<VideoItem>> {
        return providerManager.getTrendingByProvider()
    }

    suspend fun searchVideos(query: String, filter: VideoProviderType? = null): List<VideoItem> {
        return providerManager.search(query, filter)
    }

    fun getProvider(type: VideoProviderType) = providerManager.getProvider(type)

    // Favorites
    fun getFavorites(): Flow<List<VideoItem>> {
        return favoriteDao.getAllFavorites().map { entities ->
            entities.map { entity ->
                val type = runCatching { VideoProviderType.valueOf(entity.provider) }
                    .getOrDefault(VideoProviderType.WEB)
                VideoItem(
                    id = entity.videoId,
                    title = entity.title,
                    description = "",
                    thumbnailUrl = entity.thumbnailUrl,
                    provider = type,
                    videoUrl = entity.videoUrl,
                    duration = entity.duration,
                    publishedAt = "",
                    channelName = entity.channelName,
                    isFavorite = true
                )
            }
        }
    }

    fun isFavorite(videoId: String, provider: String): Flow<Boolean> {
        return favoriteDao.isFavorite(videoId, provider)
    }

    suspend fun toggleFavorite(video: VideoItem) {
        val isFav = favoriteDao.isFavoriteSync(video.id, video.provider.name)
        if (isFav) {
            favoriteDao.deleteFavorite(video.id, video.provider.name)
        } else {
            favoriteDao.insertFavorite(
                FavoriteEntity(
                    videoId = video.id,
                    provider = video.provider.name,
                    title = video.title,
                    thumbnailUrl = video.thumbnailUrl,
                    videoUrl = video.videoUrl,
                    channelName = video.channelName,
                    duration = video.duration
                )
            )
        }
    }

    suspend fun removeFavorite(videoId: String, provider: String) {
        favoriteDao.deleteFavorite(videoId, provider)
    }

    // Watch History & Continue Watching
    fun getWatchHistory(): Flow<List<VideoItem>> {
        return watchHistoryDao.getAllHistory().map { list ->
            list.map { entity -> entity.toVideoItem() }
        }
    }

    fun getContinueWatching(limit: Int = 10): Flow<List<VideoItem>> {
        return watchHistoryDao.getRecentHistory(limit).map { list ->
            list.map { entity -> entity.toVideoItem() }
        }
    }

    suspend fun recordWatch(video: VideoItem, position: Long = 0L) {
        watchHistoryDao.insertOrUpdate(
            WatchHistoryEntity(
                videoId = video.id,
                provider = video.provider.name,
                title = video.title,
                thumbnailUrl = video.thumbnailUrl,
                videoUrl = video.videoUrl,
                channelName = video.channelName,
                duration = video.duration,
                durationSeconds = video.durationSeconds,
                position = position,
                lastWatchedAt = System.currentTimeMillis()
            )
        )
        // Keep within 1000 items as specified in requirement
        watchHistoryDao.trimHistory(1000)
    }

    suspend fun updatePlaybackPosition(videoId: String, provider: String, position: Long) {
        watchHistoryDao.updatePosition(videoId, provider, position)
    }

    suspend fun removeHistoryItem(videoId: String, provider: String) {
        watchHistoryDao.deleteItem(videoId, provider)
    }

    suspend fun clearWatchHistory() {
        watchHistoryDao.clearAllHistory()
    }

    suspend fun clearAllLocalData() {
        favoriteDao.clearAllFavorites()
        watchHistoryDao.clearAllHistory()
    }

    private fun WatchHistoryEntity.toVideoItem(): VideoItem {
        val type = runCatching { VideoProviderType.valueOf(provider) }
            .getOrDefault(VideoProviderType.WEB)
        return VideoItem(
            id = videoId,
            title = title,
            description = "",
            thumbnailUrl = thumbnailUrl,
            provider = type,
            videoUrl = videoUrl,
            duration = duration,
            publishedAt = "",
            channelName = channelName,
            lastPosition = position,
            durationSeconds = durationSeconds
        )
    }
}
