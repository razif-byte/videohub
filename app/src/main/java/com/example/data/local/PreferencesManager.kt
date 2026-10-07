package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "videohub_settings")

enum class AppThemeMode {
    DARK,
    LIGHT,
    SYSTEM
}

data class AppSettings(
    val themeMode: AppThemeMode = AppThemeMode.DARK,
    val autoFullscreen: Boolean = false,
    val rememberPosition: Boolean = true,
    val autoplay: Boolean = true,
    val wifiOnly: Boolean = false,
    val mobileDataAllowed: Boolean = true,
    val freeReelsBaseUrl: String = "https://freereels.com",
    val customYoutubeApiKey: String = "",
    val customSources: Set<String> = emptySet()
)

class PreferencesManager(private val context: Context) {

    private val KEY_THEME = stringPreferencesKey("app_theme")
    private val KEY_AUTO_FULLSCREEN = booleanPreferencesKey("auto_fullscreen")
    private val KEY_REMEMBER_POSITION = booleanPreferencesKey("remember_position")
    private val KEY_AUTOPLAY = booleanPreferencesKey("autoplay")
    private val KEY_WIFI_ONLY = booleanPreferencesKey("wifi_only")
    private val KEY_MOBILE_DATA_ALLOWED = booleanPreferencesKey("mobile_data_allowed")
    private val KEY_FREEREELS_BASE_URL = stringPreferencesKey("freereels_base_url")
    private val KEY_YOUTUBE_API_KEY = stringPreferencesKey("youtube_api_key")
    private val KEY_CUSTOM_SOURCES = stringSetPreferencesKey("custom_sources")

    val settingsFlow: Flow<AppSettings> = context.dataStore.data.map { prefs ->
        val themeStr = prefs[KEY_THEME] ?: AppThemeMode.DARK.name
        val themeMode = runCatching { AppThemeMode.valueOf(themeStr) }.getOrDefault(AppThemeMode.DARK)

        AppSettings(
            themeMode = themeMode,
            autoFullscreen = prefs[KEY_AUTO_FULLSCREEN] ?: false,
            rememberPosition = prefs[KEY_REMEMBER_POSITION] ?: true,
            autoplay = prefs[KEY_AUTOPLAY] ?: true,
            wifiOnly = prefs[KEY_WIFI_ONLY] ?: false,
            mobileDataAllowed = prefs[KEY_MOBILE_DATA_ALLOWED] ?: true,
            freeReelsBaseUrl = prefs[KEY_FREEREELS_BASE_URL] ?: "https://freereels.com",
            customYoutubeApiKey = prefs[KEY_YOUTUBE_API_KEY] ?: "",
            customSources = prefs[KEY_CUSTOM_SOURCES] ?: emptySet()
        )
    }

    suspend fun setThemeMode(mode: AppThemeMode) {
        context.dataStore.edit { it[KEY_THEME] = mode.name }
    }

    suspend fun setAutoFullscreen(enabled: Boolean) {
        context.dataStore.edit { it[KEY_AUTO_FULLSCREEN] = enabled }
    }

    suspend fun setRememberPosition(enabled: Boolean) {
        context.dataStore.edit { it[KEY_REMEMBER_POSITION] = enabled }
    }

    suspend fun setAutoplay(enabled: Boolean) {
        context.dataStore.edit { it[KEY_AUTOPLAY] = enabled }
    }

    suspend fun setWifiOnly(enabled: Boolean) {
        context.dataStore.edit { it[KEY_WIFI_ONLY] = enabled }
    }

    suspend fun setMobileDataAllowed(enabled: Boolean) {
        context.dataStore.edit { it[KEY_MOBILE_DATA_ALLOWED] = enabled }
    }

    suspend fun setFreeReelsBaseUrl(url: String) {
        context.dataStore.edit { it[KEY_FREEREELS_BASE_URL] = url.trim() }
    }

    suspend fun setCustomYoutubeApiKey(key: String) {
        context.dataStore.edit { it[KEY_YOUTUBE_API_KEY] = key.trim() }
    }

    suspend fun addCustomSource(source: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[KEY_CUSTOM_SOURCES] ?: emptySet()
            prefs[KEY_CUSTOM_SOURCES] = current + source.trim()
        }
    }

    suspend fun removeCustomSource(source: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[KEY_CUSTOM_SOURCES] ?: emptySet()
            prefs[KEY_CUSTOM_SOURCES] = current - source
        }
    }

    suspend fun clearAllSettings() {
        context.dataStore.edit { it.clear() }
    }
}
