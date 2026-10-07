package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.entity.FavoriteEntity
import com.example.data.local.entity.WatchHistoryEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    private lateinit var database: AppDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `read string from context matches Video Hub`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Video Hub", appName)
    }

    @Test
    fun `test favorite dao operations`() = runBlocking {
        val dao = database.favoriteDao()
        val fav = FavoriteEntity(
            videoId = "v1",
            provider = "YOUTUBE",
            title = "Test Favorite",
            thumbnailUrl = "thumb",
            videoUrl = "https://youtube.com/watch?v=v1",
            channelName = "Channel",
            duration = "10:00"
        )

        dao.insertFavorite(fav)

        val isFav = dao.isFavorite("v1", "YOUTUBE").first()
        assertTrue(isFav)

        val allFavs = dao.getAllFavorites().first()
        assertEquals(1, allFavs.size)
        assertEquals("Test Favorite", allFavs[0].title)

        dao.deleteFavorite("v1", "YOUTUBE")
        assertFalse(dao.isFavorite("v1", "YOUTUBE").first())
    }

    @Test
    fun `test watch history dao and continue watching`() = runBlocking {
        val dao = database.watchHistoryDao()
        val historyItem = WatchHistoryEntity(
            videoId = "v2",
            provider = "WEB",
            title = "Test History",
            thumbnailUrl = "thumb",
            videoUrl = "https://example.com/video.mp4",
            channelName = "Stream",
            duration = "5:00",
            durationSeconds = 300L,
            position = 150L
        )

        dao.insertOrUpdate(historyItem)

        val historyList = dao.getAllHistory().first()
        assertEquals(1, historyList.size)
        assertEquals(150L, historyList[0].position)

        // Update playback position
        dao.updatePosition("v2", "WEB", 240L)
        val updatedItem = dao.getHistoryItem("v2", "WEB")
        assertEquals(240L, updatedItem?.position)
    }
}
