package com.example.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.VideoRepository
import com.example.domain.model.UiState
import com.example.domain.model.VideoItem
import com.example.domain.model.VideoProviderType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeData(
    val featuredVideos: List<VideoItem> = emptyList(),
    val trendingByProvider: Map<VideoProviderType, List<VideoItem>> = emptyMap(),
    val selectedFilter: VideoProviderType? = null
)

class HomeViewModel(
    private val videoRepository: VideoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<HomeData>>(UiState.Loading)
    val uiState: StateFlow<UiState<HomeData>> = _uiState.asStateFlow()

    val continueWatching: StateFlow<List<VideoItem>> = videoRepository.getContinueWatching(limit = 10)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private var rawFeatured: List<VideoItem> = emptyList()
    private var rawTrending: Map<VideoProviderType, List<VideoItem>> = emptyMap()
    private var currentFilter: VideoProviderType? = null

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                rawFeatured = videoRepository.getFeaturedVideos()
                rawTrending = videoRepository.getTrendingVideos()

                updateFilteredData()
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Unable to load videos.")
            }
        }
    }

    fun setFilter(filter: VideoProviderType?) {
        currentFilter = filter
        updateFilteredData()
    }

    private fun updateFilteredData() {
        val filteredFeatured = if (currentFilter == null) {
            rawFeatured
        } else {
            rawFeatured.filter { it.provider == currentFilter }
        }

        val filteredTrending = if (currentFilter == null) {
            rawTrending
        } else {
            rawTrending.filterKeys { it == currentFilter }
        }

        if (filteredFeatured.isEmpty() && filteredTrending.values.all { it.isEmpty() }) {
            _uiState.value = UiState.Empty("No videos found for selected source.")
        } else {
            _uiState.value = UiState.Success(
                HomeData(
                    featuredVideos = filteredFeatured,
                    trendingByProvider = filteredTrending,
                    selectedFilter = currentFilter
                )
            )
        }
    }
}
