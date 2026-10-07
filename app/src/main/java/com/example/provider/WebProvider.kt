package com.example.provider

import com.example.domain.model.VideoItem
import com.example.domain.model.VideoProviderType

class WebProvider(
    private var customSourcesProvider: () -> Set<String> = { emptySet() }
) : VideoProvider {

    override val id: String = "web"
    override val name: String = "Web & Open Streams"
    override val type: VideoProviderType = VideoProviderType.WEB
    override val isEnabled: Boolean = true

    private val defaultWebVideos = listOf(
        VideoItem(
            id = "web-blender-bbb",
            title = "Big Buck Bunny (Blender Open Movie)",
            description = "A large and lovable rabbit deals with bullying forest creatures in this famous open-source CGI animated film.",
            thumbnailUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&auto=format&fit=crop&q=80",
            provider = VideoProviderType.WEB,
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            duration = "9:56",
            publishedAt = "2008-04-10",
            channelName = "Blender Animation Studio",
            channelId = "blender-studio",
            durationSeconds = 596L
        ),
        VideoItem(
            id = "web-blender-sintel",
            title = "Sintel (Blender Open Movie)",
            description = "A lonely young woman searches the world for a baby dragon companion she nursed back to health.",
            thumbnailUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80",
            provider = VideoProviderType.WEB,
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
            duration = "14:48",
            publishedAt = "2010-09-27",
            channelName = "Blender Animation Studio",
            channelId = "blender-studio",
            durationSeconds = 888L
        ),
        VideoItem(
            id = "web-blender-tos",
            title = "Tears of Steel (Sci-Fi VFX Project)",
            description = "A group of warriors and scientists in futuristic Amsterdam attempt to save the world from rogue robotic tentacles.",
            thumbnailUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=600&auto=format&fit=crop&q=80",
            provider = VideoProviderType.WEB,
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            duration = "12:14",
            publishedAt = "2012-09-26",
            channelName = "Blender VFX Initiative",
            channelId = "blender-vfx",
            durationSeconds = 734L
        ),
        VideoItem(
            id = "web-we-are-going-on-bullrun",
            title = "For Bigger Blazes (HD Test Stream)",
            description = "High bitrate landscape and action cinematography test feed for HTML5 players.",
            thumbnailUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop&q=80",
            provider = VideoProviderType.WEB,
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            duration = "0:15",
            publishedAt = "2023-01-01",
            channelName = "Chromecast Demo Media",
            channelId = "demo-media",
            durationSeconds = 15L
        )
    )

    private fun getAllVideos(): List<VideoItem> {
        val custom = customSourcesProvider().mapIndexed { index, url ->
            VideoItem(
                id = "custom-web-$index",
                title = "Custom Source: ${url.take(35)}…",
                description = "User configured web video source: $url",
                thumbnailUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=600&auto=format&fit=crop&q=80",
                provider = VideoProviderType.WEB,
                videoUrl = url,
                duration = "Web",
                publishedAt = "Custom",
                channelName = "Custom Source",
                channelId = "custom",
                durationSeconds = 0L
            )
        }
        return defaultWebVideos + custom
    }

    override suspend fun getFeatured(): List<VideoItem> {
        return getAllVideos().take(2)
    }

    override suspend fun getTrending(): List<VideoItem> {
        return getAllVideos()
    }

    override suspend fun search(query: String): List<VideoItem> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return getAllVideos()

        val matched = getAllVideos().filter {
            it.title.contains(trimmed, ignoreCase = true) ||
                it.description.contains(trimmed, ignoreCase = true)
        }

        if (matched.isNotEmpty()) return matched

        // If user entered a valid URL in search, wrap it as a playable web stream
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            return listOf(
                VideoItem(
                    id = "direct-web-${System.currentTimeMillis()}",
                    title = "Web Video Stream",
                    description = trimmed,
                    thumbnailUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=600&auto=format&fit=crop&q=80",
                    provider = VideoProviderType.WEB,
                    videoUrl = trimmed,
                    duration = "Direct URL",
                    publishedAt = "Direct",
                    channelName = "Direct Web",
                    durationSeconds = 0L
                )
            )
        }

        return getAllVideos()
    }

    override fun canEmbed(url: String): Boolean {
        // Direct media streams or HTML5 safe players can be embedded
        return true
    }

    override fun getEmbedUrl(item: VideoItem): String {
        return item.videoUrl
    }

    override fun getOriginalUrl(item: VideoItem): String {
        return item.videoUrl
    }
}
