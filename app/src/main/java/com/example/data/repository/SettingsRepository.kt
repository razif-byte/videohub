package com.example.data.repository

import com.example.data.local.AppSettings
import com.example.data.local.AppThemeMode
import com.example.data.local.PreferencesManager
import kotlinx.coroutines.flow.Flow

open class SettingsRepository(
    private val preferencesManager: PreferencesManager
) {

    open val settings: Flow<AppSettings> = preferencesManager.settingsFlow

    open suspend fun setThemeMode(mode: AppThemeMode) {
        preferencesManager.setThemeMode(mode)
    }

    open suspend fun setAutoFullscreen(enabled: Boolean) {
        preferencesManager.setAutoFullscreen(enabled)
    }

    open suspend fun setRememberPosition(enabled: Boolean) {
        preferencesManager.setRememberPosition(enabled)
    }

    open suspend fun setAutoplay(enabled: Boolean) {
        preferencesManager.setAutoplay(enabled)
    }

    open suspend fun setWifiOnly(enabled: Boolean) {
        preferencesManager.setWifiOnly(enabled)
    }

    open suspend fun setMobileDataAllowed(enabled: Boolean) {
        preferencesManager.setMobileDataAllowed(enabled)
    }

    open suspend fun setFreeReelsBaseUrl(url: String) {
        preferencesManager.setFreeReelsBaseUrl(url)
    }

    open suspend fun setCustomYoutubeApiKey(key: String) {
        preferencesManager.setCustomYoutubeApiKey(key)
    }

    open suspend fun addCustomSource(source: String) {
        preferencesManager.addCustomSource(source)
    }

    open suspend fun removeCustomSource(source: String) {
        preferencesManager.removeCustomSource(source)
    }

    open suspend fun clearAllSettings() {
        preferencesManager.clearAllSettings()
    }

    open suspend fun login(name: String, email: String, provider: String) {
        preferencesManager.login(name, email, provider)
    }

    open suspend fun logout() {
        preferencesManager.logout()
    }
}
