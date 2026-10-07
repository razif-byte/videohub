package com.example

import com.example.data.local.AppSettings
import com.example.data.local.AppThemeMode
import com.example.data.local.dao.FavoriteDao
import com.example.data.local.dao.WatchHistoryDao
import com.example.data.local.entity.FavoriteEntity
import com.example.data.local.entity.WatchHistoryEntity
import com.example.data.repository.SettingsRepository
import com.example.data.repository.VideoRepository
import com.example.domain.model.UiState
import com.example.domain.model.VideoItem
import com.example.domain.model.VideoProviderType
import com.example.provider.ProviderManager
import com.example.provider.YouTubeProvider
import com.example.ui.home.HomeViewModel
import com.example.ui.player.PlayerViewModel
import com.example.ui.search.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    // Fake DAOs for unit testing
    private val fakeFavoriteDao = object : FavoriteDao {
        val list = mutableListOf<FavoriteEntity>()
        override suspend fun insertFavorite(favorite: FavoriteEntity): Long {
            list.add(favorite)
            return 1L
        }
        override suspend fun deleteFavorite(videoId: String, provider: String) {
            list.removeAll { it.videoId == videoId && it.provider == provider }
        }
        override fun isFavorite(videoId: String, provider: String): Flow<Boolean> {
            return flowOf(list.any { it.videoId == videoId && it.provider == provider })
        }
        override suspend fun isFavoriteSync(videoId: String, provider: String): Boolean {
            return list.any { it.videoId == videoId && it.provider == provider }
        }
        override fun getAllFavorites(): Flow<List<FavoriteEntity>> = flowOf(list)
        override suspend fun getFavoriteById(id: Long): FavoriteEntity? = list.firstOrNull { it.id == id }
        override suspend fun clearAllFavorites() { list.clear() }
    }

    private val fakeWatchHistoryDao = object : WatchHistoryDao {
        val list = mutableListOf<WatchHistoryEntity>()
        override suspend fun insertOrUpdate(history: WatchHistoryEntity): Long {
            list.removeAll { it.videoId == history.videoId && it.provider == history.provider }
            list.add(0, history)
            return 1L
        }
        override fun getAllHistory(): Flow<List<WatchHistoryEntity>> = flowOf(list)
        override fun getRecentHistory(limit: Int): Flow<List<WatchHistoryEntity>> = flowOf(list.take(limit))
        override suspend fun getHistoryItem(videoId: String, provider: String): WatchHistoryEntity? {
            return list.firstOrNull { it.videoId == videoId && it.provider == provider }
        }
        override suspend fun updatePosition(videoId: String, provider: String, position: Long, timestamp: Long) {
            val idx = list.indexOfFirst { it.videoId == videoId && it.provider == provider }
            if (idx != -1) {
                list[idx] = list[idx].copy(position = position, lastWatchedAt = timestamp)
            }
        }
        override suspend fun deleteItem(videoId: String, provider: String) {
            list.removeAll { it.videoId == videoId && it.provider == provider }
        }
        override suspend fun clearAllHistory() { list.clear() }
        override suspend fun trimHistory(maxItems: Int) {
            if (list.size > maxItems) {
                val sub = list.take(maxItems)
                list.clear()
                list.addAll(sub)
            }
        }
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testHomeViewModelLoadsData() = runTest {
        val providerManager = ProviderManager().apply {
            registerProvider(YouTubeProvider())
        }
        val repository = VideoRepository(providerManager, fakeFavoriteDao, fakeWatchHistoryDao)
        val homeViewModel = HomeViewModel(repository)

        testDispatcher.scheduler.advanceUntilIdle()

        val state = homeViewModel.uiState.value
        assertTrue(state is UiState.Success)
        val data = (state as UiState.Success).data
        assertTrue(data.featuredVideos.isNotEmpty())
    }

    @Test
    fun testPlayerViewModelToggleFavorite() = runTest {
        val providerManager = ProviderManager().apply {
            registerProvider(YouTubeProvider())
        }
        val repository = VideoRepository(providerManager, fakeFavoriteDao, fakeWatchHistoryDao)
        val playerViewModel = PlayerViewModel(repository)

        val video = VideoItem(
            id = "test-123",
            title = "Test Video",
            description = "Desc",
            thumbnailUrl = "thumb",
            provider = VideoProviderType.YOUTUBE,
            videoUrl = "https://youtube.com/watch?v=test-123",
            duration = "4:00",
            publishedAt = "2024",
            channelName = "Channel"
        )

        playerViewModel.setVideo(video)
        testDispatcher.scheduler.advanceUntilIdle()

        playerViewModel.toggleFavorite()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, fakeFavoriteDao.list.size)
    }

    @Test
    fun testLoginViewModelGoogleLogin() = runTest {
        val context = androidx.test.core.app.ApplicationProvider.getApplicationContext<android.content.Context>()
        val fakeRepo = object : SettingsRepository(com.example.data.local.PreferencesManager(context)) {
            var loggedInName = ""
            override suspend fun login(name: String, email: String, provider: String) {
                loggedInName = name
            }
        }
        val loginViewModel = com.example.ui.login.LoginViewModel(fakeRepo)
        loginViewModel.loginWithGoogle("Test User")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = loginViewModel.uiState.value
        assertTrue(state is com.example.ui.login.LoginUiState.Success)
        assertEquals("Test User", (state as com.example.ui.login.LoginUiState.Success).userName)
        assertEquals("Test User", fakeRepo.loggedInName)
    }

    @Test
    fun testWebViewModelAndLinks() {
        val webViewModel = com.example.ui.web.WebViewModel()
        val links = webViewModel.webLinks

        assertEquals(4, links.size)
        assertTrue(links.any { it.url == "https://razif-interaktif-al-quran.ai.studio" })
        assertTrue(links.any { it.url == "https://sistem-pemantauan-banjir-iot.ai.studio" })
        assertTrue(links.any { it.url == "https://remix-sky-metropolis-9687.ai.studio" })
        assertTrue(links.any { it.url == "https://nasadef.com.my" })

        webViewModel.selectLinkByUrl("https://sistem-pemantauan-banjir-iot.ai.studio")
        assertEquals("https://sistem-pemantauan-banjir-iot.ai.studio", webViewModel.selectedWebLink.value.url)
    }

    @Test
    fun testAdPromotionCatalog() {
        val ad = com.example.domain.model.AdPromotionCatalog.getRandomAd()
        assertNotNull(ad)
        assertTrue(ad.title.isNotBlank())
        assertTrue(ad.targetUrl.startsWith("https://"))
    }
}
