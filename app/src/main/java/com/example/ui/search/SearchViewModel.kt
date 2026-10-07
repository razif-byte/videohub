package com.example.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.VideoRepository
import com.example.domain.model.UiState
import com.example.domain.model.VideoItem
import com.example.domain.model.VideoProviderType
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val videoRepository: VideoRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _selectedFilter = MutableStateFlow<VideoProviderType?>(null)
    val selectedFilter: StateFlow<VideoProviderType?> = _selectedFilter.asStateFlow()

    private val _uiState = MutableStateFlow<UiState<List<VideoItem>>>(UiState.Empty("Type keywords or a video link above."))
    val uiState: StateFlow<UiState<List<VideoItem>>> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
        searchJob?.cancel()

        if (newQuery.isBlank()) {
            _uiState.value = UiState.Empty("Type keywords or a video link above.")
            return
        }

        searchJob = viewModelScope.launch {
            delay(350) // Debounce typing
            performSearch()
        }
    }

    fun onFilterChange(filter: VideoProviderType?) {
        _selectedFilter.value = filter
        if (_query.value.isNotBlank()) {
            performSearch()
        }
    }

    fun performSearch() {
        val q = _query.value.trim()
        if (q.isBlank()) return

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val results = videoRepository.searchVideos(q, _selectedFilter.value)
                if (results.isEmpty()) {
                    _uiState.value = UiState.Empty("No results found for \"$q\"")
                } else {
                    _uiState.value = UiState.Success(results)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Failed to perform search.")
            }
        }
    }
}
