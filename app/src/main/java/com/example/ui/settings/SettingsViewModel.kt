package com.example.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppSettings
import com.example.data.local.AppThemeMode
import com.example.data.repository.SettingsRepository
import com.example.data.repository.VideoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val videoRepository: VideoRepository
) : ViewModel() {

    val settings: StateFlow<AppSettings> = settingsRepository.settings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings()
        )

    fun setTheme(theme: AppThemeMode) {
        viewModelScope.launch {
            settingsRepository.setThemeMode(theme)
        }
    }

    fun setAutoFullscreen(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setAutoFullscreen(enabled)
        }
    }

    fun setRememberPosition(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setRememberPosition(enabled)
        }
    }

    fun setAutoplay(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setAutoplay(enabled)
        }
    }

    fun setWifiOnly(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setWifiOnly(enabled)
        }
    }

    fun setMobileDataAllowed(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setMobileDataAllowed(enabled)
        }
    }

    fun setFreeReelsBaseUrl(url: String) {
        viewModelScope.launch {
            settingsRepository.setFreeReelsBaseUrl(url)
        }
    }

    fun setCustomYoutubeApiKey(key: String) {
        viewModelScope.launch {
            settingsRepository.setCustomYoutubeApiKey(key)
        }
    }

    fun addCustomSource(url: String) {
        viewModelScope.launch {
            settingsRepository.addCustomSource(url)
        }
    }

    fun removeCustomSource(url: String) {
        viewModelScope.launch {
            settingsRepository.removeCustomSource(url)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            videoRepository.clearWatchHistory()
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            videoRepository.clearAllLocalData()
            settingsRepository.clearAllSettings()
        }
    }

    fun logout() {
        viewModelScope.launch {
            settingsRepository.logout()
        }
    }
}
