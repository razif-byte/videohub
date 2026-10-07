package com.example.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.VideoRepository
import com.example.domain.model.VideoItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val videoRepository: VideoRepository
) : ViewModel() {

    val history: StateFlow<List<VideoItem>> = videoRepository.getWatchHistory()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun clearHistory() {
        viewModelScope.launch {
            videoRepository.clearWatchHistory()
        }
    }

    fun removeHistoryItem(video: VideoItem) {
        viewModelScope.launch {
            videoRepository.removeHistoryItem(video.id, video.provider.name)
        }
    }
}
