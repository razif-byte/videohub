package com.example

import com.example.domain.model.VideoItem
import com.example.domain.model.VideoProviderType
import com.example.provider.FreeReelsProvider
import com.example.provider.ProviderManager
import com.example.provider.WebProvider
import com.example.provider.YouTubeProvider
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class VideoProviderTest {

    @Test
    fun testYouTubeProviderEmbedUrl() {
        val provider = YouTubeProvider()
        val item = VideoItem(
            id = "dQw4w9WgXcQ",
            title = "Test Video",
            description = "Desc",
            thumbnailUrl = "thumb",
            provider = VideoProviderType.YOUTUBE,
            videoUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
            duration = "3:30",
            publishedAt = "2024",
            channelName = "Test"
        )

        assertTrue(provider.canEmbed(item.videoUrl))
        val embedUrl = provider.getEmbedUrl(item)
        assertNotNull(embedUrl)
        assertTrue(embedUrl!!.contains("youtube-nocookie.com/embed/dQw4w9WgXcQ"))
    }

    @Test
    fun testFreeReelsProviderRestrictsEmbed() {
        val provider = FreeReelsProvider(baseUrlProvider = { "https://freereels.com" })
        val item = VideoItem(
            id = "reel-101",
            title = "Test Reel",
            description = "Desc",
            thumbnailUrl = "thumb",
            provider = VideoProviderType.FREEREELS,
            videoUrl = "https://freereels.com/reel/101",
            duration = "0:30",
            publishedAt = "2024",
            channelName = "Creator"
        )

        // Strict compliance check: FreeReels restricts iframe embedding
        assertFalse(provider.canEmbed(item.videoUrl))
        assertEquals(null, provider.getEmbedUrl(item))
        assertEquals("https://freereels.com/reel/101", provider.getOriginalUrl(item))
    }

    @Test
    fun testWebProvider() = runBlocking {
        val provider = WebProvider()
        val featured = provider.getFeatured()
        assertTrue(featured.isNotEmpty())
        assertTrue(provider.canEmbed(featured.first().videoUrl))
    }

    @Test
    fun testProviderManagerSearchAndFilter() = runBlocking {
        val manager = ProviderManager()
        val yt = YouTubeProvider()
        val fr = FreeReelsProvider()
        val web = WebProvider()

        manager.registerProvider(yt)
        manager.registerProvider(fr)
        manager.registerProvider(web)

        assertEquals(3, manager.getProviders().size)

        // Search across all providers
        val allResults = manager.search("City")
        assertNotNull(allResults)

        // Filter search to YouTube only
        val ytResults = manager.search("Video", VideoProviderType.YOUTUBE)
        assertTrue(ytResults.all { it.provider == VideoProviderType.YOUTUBE })
    }
}
