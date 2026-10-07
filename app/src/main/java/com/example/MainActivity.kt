package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.data.local.AppDatabase
import com.example.data.local.PreferencesManager
import com.example.data.repository.SettingsRepository
import com.example.data.repository.VideoRepository
import com.example.domain.model.VideoItem
import com.example.domain.model.VideoProviderType
import com.example.provider.FreeReelsProvider
import com.example.provider.ProviderManager
import com.example.provider.WebProvider
import com.example.provider.YouTubeProvider
import com.example.ui.favorites.FavoritesViewModel
import com.example.ui.history.HistoryViewModel
import com.example.ui.home.HomeViewModel
import com.example.ui.navigation.VideoHubApp
import com.example.ui.player.PlayerViewModel
import com.example.ui.search.SearchViewModel
import com.example.ui.settings.SettingsViewModel
import com.example.ui.theme.VideoHubTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Local Storage & Preferences
        val database = AppDatabase.getInstance(applicationContext)
        val preferencesManager = PreferencesManager(applicationContext)
        val settingsRepository = SettingsRepository(preferencesManager)

        // Initialize Providers & Provider Manager
        val providerManager = ProviderManager().apply {
            registerProvider(
                YouTubeProvider(
                    apiKeyProvider = {
                        runBlocking {
                            preferencesManager.settingsFlow.first().customYoutubeApiKey
                        }
                    }
                )
            )
            registerProvider(
                FreeReelsProvider(
                    baseUrlProvider = {
                        runBlocking {
                            preferencesManager.settingsFlow.first().freeReelsBaseUrl
                        }
                    }
                )
            )
            registerProvider(
                WebProvider(
                    customSourcesProvider = {
                        runBlocking {
                            preferencesManager.settingsFlow.first().customSources
                        }
                    }
                )
            )
        }

        // Initialize Repositories
        val videoRepository = VideoRepository(
            providerManager = providerManager,
            favoriteDao = database.favoriteDao(),
            watchHistoryDao = database.watchHistoryDao()
        )

        // ViewModels
        val homeViewModel = HomeViewModel(videoRepository)
        val searchViewModel = SearchViewModel(videoRepository)
        val playerViewModel = PlayerViewModel(videoRepository)
        val favoritesViewModel = FavoritesViewModel(videoRepository)
        val historyViewModel = HistoryViewModel(videoRepository)
        val settingsViewModel = SettingsViewModel(settingsRepository, videoRepository)
        val loginViewModel = com.example.ui.login.LoginViewModel(settingsRepository)

        // Parse Deep Links (Section 28)
        val initialDeepLinkVideo = parseDeepLink(intent?.data)

        setContent {
            val appSettings by settingsViewModel.settings.collectAsState()

            VideoHubTheme(themeMode = appSettings.themeMode) {
                if (!appSettings.user.isLoggedIn) {
                    com.example.ui.login.LoginScreen(
                        viewModel = loginViewModel,
                        onLoginSuccess = { /* Automatically switches via StateFlow */ }
                    )
                } else {
                    VideoHubApp(
                        homeViewModel = homeViewModel,
                        searchViewModel = searchViewModel,
                        playerViewModel = playerViewModel,
                        favoritesViewModel = favoritesViewModel,
                        historyViewModel = historyViewModel,
                        settingsViewModel = settingsViewModel,
                        initialDeepLinkVideo = initialDeepLinkVideo
                    )
                }
            }
        }
    }

    private fun parseDeepLink(uri: Uri?): VideoItem? {
        if (uri == null) return null

        val scheme = uri.scheme?.lowercase()
        val host = uri.host?.lowercase()

        // videohub://youtube/{videoId}
        if (scheme == "videohub" && host == "youtube") {
            val videoId = uri.lastPathSegment ?: return null
            return VideoItem(
                id = videoId,
                title = "YouTube Video ($videoId)",
                description = "Opened via deep link",
                thumbnailUrl = "https://img.youtube.com/vi/$videoId/hqdefault.jpg",
                provider = VideoProviderType.YOUTUBE,
                videoUrl = "https://www.youtube.com/watch?v=$videoId",
                duration = "Video",
                publishedAt = "Now",
                channelName = "YouTube",
                durationSeconds = 0L
            )
        }

        // videohub://freereels/{id}
        if (scheme == "videohub" && host == "freereels") {
            val reelId = uri.lastPathSegment ?: return null
            return VideoItem(
                id = reelId,
                title = "FreeReels ($reelId)",
                description = "Opened via deep link",
                thumbnailUrl = "https://images.unsplash.com/photo-1514565131-fce0801e5785?w=600&auto=format&fit=crop&q=80",
                provider = VideoProviderType.FREEREELS,
                videoUrl = "https://freereels.com/reel/$reelId",
                duration = "Reel",
                publishedAt = "Now",
                channelName = "FreeReels",
                durationSeconds = 0L
            )
        }

        // videohub://web?url=...
        if (scheme == "videohub" && host == "web") {
            val url = uri.getQueryParameter("url") ?: return null
            return VideoItem(
                id = "deep-web-${System.currentTimeMillis()}",
                title = "Web Video Stream",
                description = url,
                thumbnailUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=600&auto=format&fit=crop&q=80",
                provider = VideoProviderType.WEB,
                videoUrl = url,
                duration = "Web",
                publishedAt = "Now",
                channelName = "Web Source",
                durationSeconds = 0L
            )
        }

        // https://videohub.app/video/{id}
        if (scheme == "https" && host == "videohub.app") {
            val videoId = uri.lastPathSegment ?: return null
            return VideoItem(
                id = videoId,
                title = "Video ($videoId)",
                description = "Opened via link",
                thumbnailUrl = "https://img.youtube.com/vi/$videoId/hqdefault.jpg",
                provider = VideoProviderType.YOUTUBE,
                videoUrl = "https://www.youtube.com/watch?v=$videoId",
                duration = "Video",
                publishedAt = "Now",
                channelName = "Video Hub",
                durationSeconds = 0L
            )
        }

        return null
    }
}
