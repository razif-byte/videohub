package com.example.data.repository

import com.example.data.local.AppSettings
import com.example.data.local.AppThemeMode
import com.example.data.local.PreferencesManager
import kotlinx.coroutines.flow.Flow

class SettingsRepository(
    private val preferencesManager: PreferencesManager
) {

    val settings: Flow<AppSettings> = preferencesManager.settingsFlow

    suspend fun setThemeMode(mode: AppThemeMode) {
        preferencesManager.setThemeMode(mode)
    }

    suspend fun setAutoFullscreen(enabled: Boolean) {
        preferencesManager.setAutoFullscreen(enabled)
    }

    suspend fun setRememberPosition(enabled: Boolean) {
        preferencesManager.setRememberPosition(enabled)
    }

    suspend fun setAutoplay(enabled: Boolean) {
        preferencesManager.setAutoplay(enabled)
    }

    suspend fun setWifiOnly(enabled: Boolean) {
        preferencesManager.setWifiOnly(enabled)
    }

    suspend fun setMobileDataAllowed(enabled: Boolean) {
        preferencesManager.setMobileDataAllowed(enabled)
    }

    suspend fun setFreeReelsBaseUrl(url: String) {
        preferencesManager.setFreeReelsBaseUrl(url)
    }

    suspend fun setCustomYoutubeApiKey(key: String) {
        preferencesManager.setCustomYoutubeApiKey(key)
    }

    suspend fun addCustomSource(source: String) {
        preferencesManager.addCustomSource(source)
    }

    suspend fun removeCustomSource(source: String) {
        preferencesManager.removeCustomSource(source)
    }

    suspend fun clearAllSettings() {
        preferencesManager.clearAllSettings()
    }
}
