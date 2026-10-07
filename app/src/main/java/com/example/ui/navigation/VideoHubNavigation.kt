package com.example.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.model.AdPromotion
import com.example.domain.model.AdPromotionCatalog
import com.example.domain.model.VideoItem
import com.example.domain.model.VideoProviderType
import com.example.ui.components.AdPromotionDialog
import com.example.ui.components.NavDestination
import com.example.ui.components.VideoHubBottomBar
import com.example.ui.components.VideoHubNavigationRail
import com.example.ui.favorites.FavoritesScreen
import com.example.ui.favorites.FavoritesViewModel
import com.example.ui.history.HistoryScreen
import com.example.ui.history.HistoryViewModel
import com.example.ui.home.HomeScreen
import com.example.ui.home.HomeViewModel
import com.example.ui.player.PlayerScreen
import com.example.ui.player.PlayerViewModel
import com.example.ui.search.SearchScreen
import com.example.ui.search.SearchViewModel
import com.example.ui.settings.SettingsScreen
import com.example.ui.settings.SettingsViewModel
import com.example.ui.web.WebScreen
import com.example.ui.web.WebViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun VideoHubApp(
    homeViewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
    playerViewModel: PlayerViewModel,
    favoritesViewModel: FavoritesViewModel,
    historyViewModel: HistoryViewModel,
    settingsViewModel: SettingsViewModel,
    webViewModel: WebViewModel = remember { WebViewModel() },
    initialDeepLinkVideo: VideoItem? = null
) {
    var currentRoute by remember { mutableStateOf(NavDestination.HOME.route) }
    var activeVideo by remember { mutableStateOf<VideoItem?>(initialDeepLinkVideo) }
    var activePromoAd by remember { mutableStateOf<AdPromotion?>(null) }

    // Every 20 minutes of active usage, display 1 random promotion ad (Requirement 2)
    LaunchedEffect(Unit) {
        while (isActive) {
            delay(20 * 60 * 1000L) // 20 minutes (1,200,000 ms)
            activePromoAd = AdPromotionCatalog.getRandomAd()
        }
    }

    // Handle deep link video updates
    if (initialDeepLinkVideo != null && activeVideo == null) {
        activeVideo = initialDeepLinkVideo
    }

    // Handle back button when player is open
    BackHandler(enabled = activeVideo != null) {
        activeVideo = null
    }

    // Show Ad Dialog if triggered
    activePromoAd?.let { ad ->
        AdPromotionDialog(
            ad = ad,
            onDismiss = { activePromoAd = null },
            onOpenUrl = { url ->
                activePromoAd = null
                webViewModel.selectLinkByUrl(url)
                currentRoute = NavDestination.WEB.route
            }
        )
    }

    if (activeVideo != null) {
        PlayerScreen(
            video = activeVideo!!,
            viewModel = playerViewModel,
            onBackClick = { activeVideo = null }
        )
    } else {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isWideScreen = maxWidth >= 600.dp

            if (isWideScreen) {
                // Wide Screen / Tablet / Android TV: Navigation Rail on left
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    VideoHubNavigationRail(
                        currentRoute = currentRoute,
                        onNavigate = { currentRoute = it }
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        NavigationContent(
                            currentRoute = currentRoute,
                            homeViewModel = homeViewModel,
                            searchViewModel = searchViewModel,
                            webViewModel = webViewModel,
                            favoritesViewModel = favoritesViewModel,
                            historyViewModel = historyViewModel,
                            settingsViewModel = settingsViewModel,
                            onVideoClick = { activeVideo = it },
                            onNavigateToSearch = { currentRoute = NavDestination.SEARCH.route },
                            onNavigateToSettings = { currentRoute = NavDestination.SETTINGS.route },
                            onTriggerAdPreview = { activePromoAd = AdPromotionCatalog.getRandomAd() }
                        )
                    }
                }
            } else {
                // Compact Screen (Smartphones): Bottom Bar
                Scaffold(
                    bottomBar = {
                        VideoHubBottomBar(
                            currentRoute = currentRoute,
                            onNavigate = { currentRoute = it }
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        NavigationContent(
                            currentRoute = currentRoute,
                            homeViewModel = homeViewModel,
                            searchViewModel = searchViewModel,
                            webViewModel = webViewModel,
                            favoritesViewModel = favoritesViewModel,
                            historyViewModel = historyViewModel,
                            settingsViewModel = settingsViewModel,
                            onVideoClick = { activeVideo = it },
                            onNavigateToSearch = { currentRoute = NavDestination.SEARCH.route },
                            onNavigateToSettings = { currentRoute = NavDestination.SETTINGS.route },
                            onTriggerAdPreview = { activePromoAd = AdPromotionCatalog.getRandomAd() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationContent(
    currentRoute: String,
    homeViewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
    webViewModel: WebViewModel,
    favoritesViewModel: FavoritesViewModel,
    historyViewModel: HistoryViewModel,
    settingsViewModel: SettingsViewModel,
    onVideoClick: (VideoItem) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onTriggerAdPreview: () -> Unit
) {
    when (currentRoute) {
        NavDestination.HOME.route -> HomeScreen(
            viewModel = homeViewModel,
            onVideoClick = onVideoClick,
            onSearchClick = onNavigateToSearch,
            onSettingsClick = onNavigateToSettings
        )
        NavDestination.SEARCH.route -> SearchScreen(
            viewModel = searchViewModel,
            onVideoClick = onVideoClick
        )
        NavDestination.WEB.route -> WebScreen(
            viewModel = webViewModel
        )
        NavDestination.FAVORITES.route -> FavoritesScreen(
            viewModel = favoritesViewModel,
            onVideoClick = onVideoClick
        )
        NavDestination.HISTORY.route -> HistoryScreen(
            viewModel = historyViewModel,
            onVideoClick = onVideoClick
        )
        NavDestination.SETTINGS.route -> SettingsScreen(
            viewModel = settingsViewModel,
            onTriggerAdPreview = onTriggerAdPreview
        )
    }
}
