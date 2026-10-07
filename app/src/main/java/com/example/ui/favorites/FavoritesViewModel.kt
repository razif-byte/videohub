package com.example.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.VideoRepository
import com.example.domain.model.VideoItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val videoRepository: VideoRepository
) : ViewModel() {

    val favorites: StateFlow<List<VideoItem>> = videoRepository.getFavorites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun removeFavorite(video: VideoItem) {
        viewModelScope.launch {
            videoRepository.removeFavorite(video.id, video.provider.name)
        }
    }
}
