package com.example.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.VideoRepository
import com.example.domain.model.VideoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val videoRepository: VideoRepository
) : ViewModel() {

    private val _currentVideo = MutableStateFlow<VideoItem?>(null)
    val currentVideo: StateFlow<VideoItem?> = _currentVideo.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    fun setVideo(video: VideoItem) {
        _currentVideo.value = video

        viewModelScope.launch {
            // Check favorite status
            videoRepository.isFavorite(video.id, video.provider.name).collect { fav ->
                _isFavorite.value = fav
            }
        }

        viewModelScope.launch {
            // Record watch history automatically
            videoRepository.recordWatch(video, video.lastPosition)
        }
    }

    fun toggleFavorite() {
        val video = _currentVideo.value ?: return
        viewModelScope.launch {
            videoRepository.toggleFavorite(video)
        }
    }

    fun updatePosition(position: Long) {
        val video = _currentVideo.value ?: return
        viewModelScope.launch {
            videoRepository.updatePlaybackPosition(video.id, video.provider.name, position)
        }
    }

    fun canEmbed(video: VideoItem): Boolean {
        val provider = videoRepository.getProvider(video.provider)
        return provider?.canEmbed(video.videoUrl) ?: false
    }

    fun getEmbedUrl(video: VideoItem): String {
        val provider = videoRepository.getProvider(video.provider)
        return provider?.getEmbedUrl(video) ?: video.videoUrl
    }

    fun getOriginalUrl(video: VideoItem): String {
        val provider = videoRepository.getProvider(video.provider)
        return provider?.getOriginalUrl(video) ?: video.videoUrl
    }
}
