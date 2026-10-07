package com.example.provider

import com.example.domain.model.VideoItem
import com.example.domain.model.VideoProviderType

class FreeReelsProvider(
    private var baseUrlProvider: () -> String = { "https://freereels.com" }
) : VideoProvider {

    override val id: String = "freereels"
    override val name: String = "FreeReels"
    override val type: VideoProviderType = VideoProviderType.FREEREELS
    override val isEnabled: Boolean = true

    private fun getBaseUrl(): String {
        val url = baseUrlProvider().trim()
        return if (url.isNotEmpty()) url.removeSuffix("/") else "https://freereels.com"
    }

    private val sampleReels: List<VideoItem>
        get() {
            val base = getBaseUrl()
            return listOf(
                VideoItem(
                    id = "reel-101",
                    title = "Urban City Timelapse Night Lights",
                    description = "Stunning 4K hyperlapse of neon skyline and city movements.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1514565131-fce0801e5785?w=600&auto=format&fit=crop&q=80",
                    provider = VideoProviderType.FREEREELS,
                    videoUrl = "$base/reel/urban-timelapse-101",
                    duration = "0:30",
                    publishedAt = "2024-08-01",
                    channelName = "NightLife Creators",
                    channelId = "c-101",
                    durationSeconds = 30L
                ),
                VideoItem(
                    id = "reel-102",
                    title = "Artisan Pour Over Coffee Craft",
                    description = "Macro cinematic reel showcasing specialty coffee brewing technique.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1511920170033-f8396924c348?w=600&auto=format&fit=crop&q=80",
                    provider = VideoProviderType.FREEREELS,
                    videoUrl = "$base/reel/artisan-coffee-102",
                    duration = "0:45",
                    publishedAt = "2024-08-05",
                    channelName = "Brew Culture",
                    channelId = "c-102",
                    durationSeconds = 45L
                ),
                VideoItem(
                    id = "reel-103",
                    title = "Epic Mountain Peak Drone Flyby",
                    description = "Spectacular cinematic aerial shot soaring above snow-capped summits.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=600&auto=format&fit=crop&q=80",
                    provider = VideoProviderType.FREEREELS,
                    videoUrl = "$base/reel/mountain-drone-103",
                    duration = "0:40",
                    publishedAt = "2024-08-10",
                    channelName = "Alpine Perspectives",
                    channelId = "c-103",
                    durationSeconds = 40L
                ),
                VideoItem(
                    id = "reel-104",
                    title = "Deep Sea Bioluminescent Creatures",
                    description = "Underwater macro photography of glowing jellyfish and coral reefs.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1544551763-46a013bb70d5?w=600&auto=format&fit=crop&q=80",
                    provider = VideoProviderType.FREEREELS,
                    videoUrl = "$base/reel/deep-sea-104",
                    duration = "0:50",
                    publishedAt = "2024-08-12",
                    channelName = "Oceanic Wonders",
                    channelId = "c-104",
                    durationSeconds = 50L
                ),
                VideoItem(
                    id = "reel-105",
                    title = "Minimalist Setup Desk Tour 2024",
                    description = "Clean aesthetic workspace with ambient lighting and clean ergonomics.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=600&auto=format&fit=crop&q=80",
                    provider = VideoProviderType.FREEREELS,
                    videoUrl = "$base/reel/clean-desk-105",
                    duration = "0:35",
                    publishedAt = "2024-08-14",
                    channelName = "Tech Living",
                    channelId = "c-105",
                    durationSeconds = 35L
                ),
                VideoItem(
                    id = "reel-106",
                    title = "High Speed Supercar Track Launch",
                    description = "Exhaust note sound check and zero to sixty launch control.",
                    thumbnailUrl = "https://images.unsplash.com/photo-1503376780353-7e6692767b70?w=600&auto=format&fit=crop&q=80",
                    provider = VideoProviderType.FREEREELS,
                    videoUrl = "$base/reel/supercar-launch-106",
                    duration = "0:25",
                    publishedAt = "2024-08-18",
                    channelName = "Throttle Syndicate",
                    channelId = "c-106",
                    durationSeconds = 25L
                )
            )
        }

    override suspend fun getFeatured(): List<VideoItem> {
        return sampleReels.take(2)
    }

    override suspend fun getTrending(): List<VideoItem> {
        return sampleReels
    }

    override suspend fun search(query: String): List<VideoItem> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return sampleReels

        return sampleReels.filter {
            it.title.contains(trimmed, ignoreCase = true) ||
                it.description.contains(trimmed, ignoreCase = true) ||
                it.channelName.contains(trimmed, ignoreCase = true)
        }
    }

    /**
     * By default, FreeReels implements strict Content-Security-Policy and X-Frame-Options
     * prohibiting embedding in third-party contexts unless explicitly whitelist configured.
     * We follow strict compliance and return false, triggering the graceful Open Original Website fallback.
     */
    override fun canEmbed(url: String): Boolean {
        // FreeReels disallows iframe embedding by default to protect platform rights.
        // We do not bypass or proxy.
        return false
    }

    override fun getEmbedUrl(item: VideoItem): String? {
        return if (canEmbed(item.videoUrl)) item.videoUrl else null
    }

    override fun getOriginalUrl(item: VideoItem): String {
        return item.videoUrl.ifEmpty { "${getBaseUrl()}/reel/${item.id}" }
    }
}
