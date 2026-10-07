package com.example.provider

import com.example.BuildConfig
import com.example.data.remote.YouTubeApiService
import com.example.domain.model.VideoItem
import com.example.domain.model.VideoProviderType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class YouTubeProvider(
    private var apiKeyProvider: () -> String = { "" }
) : VideoProvider {

    override val id: String = "youtube"
    override val name: String = "YouTube"
    override val type: VideoProviderType = VideoProviderType.YOUTUBE
    override val isEnabled: Boolean = true

    private val apiService: YouTubeApiService by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(YouTubeApiService::class.java)
    }

    private val curatedVideos = listOf(
        VideoItem(
            id = "dQw4w9WgXcQ",
            title = "Rick Astley - Never Gonna Give You Up (Official Music Video)",
            description = "The official video for “Never Gonna Give You Up” by Rick Astley.",
            thumbnailUrl = "https://img.youtube.com/vi/dQw4w9WgXcQ/hqdefault.jpg",
            provider = VideoProviderType.YOUTUBE,
            videoUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
            duration = "3:33",
            publishedAt = "2009-10-25",
            channelName = "Rick Astley",
            channelId = "UCuAXFkgsw1L7xaCfnd5JJOw",
            durationSeconds = 213L
        ),
        VideoItem(
            id = "kJQP7kiw5Fk",
            title = "Luis Fonsi - Despacito ft. Daddy Yankee",
            description = "Official Music Video for Despacito performed by Luis Fonsi.",
            thumbnailUrl = "https://img.youtube.com/vi/kJQP7kiw5Fk/hqdefault.jpg",
            provider = VideoProviderType.YOUTUBE,
            videoUrl = "https://www.youtube.com/watch?v=kJQP7kiw5Fk",
            duration = "4:42",
            publishedAt = "2017-01-13",
            channelName = "Luis Fonsi",
            channelId = "UCxoq-PAQeAdk_ysh8G1yHCA",
            durationSeconds = 282L
        ),
        VideoItem(
            id = "fJ9rUzIMcZQ",
            title = "Queen – Bohemian Rhapsody (Official Video Remastered)",
            description = "The official music video for Bohemian Rhapsody by Queen.",
            thumbnailUrl = "https://img.youtube.com/vi/fJ9rUzIMcZQ/hqdefault.jpg",
            provider = VideoProviderType.YOUTUBE,
            videoUrl = "https://www.youtube.com/watch?v=fJ9rUzIMcZQ",
            duration = "5:59",
            publishedAt = "2008-08-01",
            channelName = "Queen Official",
            channelId = "UCiMhD4jzUqG-IgPzUmmytRQ",
            durationSeconds = 359L
        ),
        VideoItem(
            id = "OPf0YbXqDm0",
            title = "Mark Ronson - Uptown Funk ft. Bruno Mars",
            description = "Official Video for Uptown Funk by Mark Ronson ft. Bruno Mars.",
            thumbnailUrl = "https://img.youtube.com/vi/OPf0YbXqDm0/hqdefault.jpg",
            provider = VideoProviderType.YOUTUBE,
            videoUrl = "https://www.youtube.com/watch?v=OPf0YbXqDm0",
            duration = "4:30",
            publishedAt = "2014-11-19",
            channelName = "MarkRonsonVEVO",
            channelId = "UCwcTX6s5xH2qfH_Jd14G_bA",
            durationSeconds = 270L
        ),
        VideoItem(
            id = "L_LUpnjgPso",
            title = "Android 15 Developer Deep Dive: What's New",
            description = "Discover the latest features, APIs, and modern architecture patterns in Android.",
            thumbnailUrl = "https://img.youtube.com/vi/L_LUpnjgPso/hqdefault.jpg",
            provider = VideoProviderType.YOUTUBE,
            videoUrl = "https://www.youtube.com/watch?v=L_LUpnjgPso",
            duration = "14:20",
            publishedAt = "2024-05-15",
            channelName = "Android Developers",
            channelId = "UCVHFbqXqoYvEWM1Ddxl0QDg",
            durationSeconds = 860L
        ),
        VideoItem(
            id = "1la4bWlZdgE",
            title = "Google I/O Keynote Highlights",
            description = "Catch the most innovative announcements from Google across mobile, AI, and developer tools.",
            thumbnailUrl = "https://img.youtube.com/vi/1la4bWlZdgE/hqdefault.jpg",
            provider = VideoProviderType.YOUTUBE,
            videoUrl = "https://www.youtube.com/watch?v=1la4bWlZdgE",
            duration = "22:15",
            publishedAt = "2024-05-14",
            channelName = "Google",
            channelId = "UCK8sQmJBp8GCxrOtXWBpyEA",
            durationSeconds = 1335L
        ),
        VideoItem(
            id = "LXb3EKWsInQ",
            title = "COSTA RICA IN 4K 60fps HDR (ULTRA HD)",
            description = "Experience the stunning wildlife and natural beauty of Costa Rica in Ultra High Definition.",
            thumbnailUrl = "https://img.youtube.com/vi/LXb3EKWsInQ/hqdefault.jpg",
            provider = VideoProviderType.YOUTUBE,
            videoUrl = "https://www.youtube.com/watch?v=LXb3EKWsInQ",
            duration = "5:17",
            publishedAt = "2020-03-10",
            channelName = "Jacob + Katie Schwarz",
            channelId = "UCn_4sL8Qf51H_yW1tH16l1w",
            durationSeconds = 317L
        ),
        VideoItem(
            id = "Bey4XXJAqS8",
            title = "What Space Looks Like in Ultra HD",
            description = "Ultra high resolution NASA views of Earth and the cosmos recorded from the ISS.",
            thumbnailUrl = "https://img.youtube.com/vi/Bey4XXJAqS8/hqdefault.jpg",
            provider = VideoProviderType.YOUTUBE,
            videoUrl = "https://www.youtube.com/watch?v=Bey4XXJAqS8",
            duration = "10:04",
            publishedAt = "2021-08-20",
            channelName = "NASA Space Exploration",
            channelId = "UCLA_DiR1FfKNvjuUpBHmylQ",
            durationSeconds = 604L
        )
    )

    override suspend fun getFeatured(): List<VideoItem> {
        return curatedVideos.take(3)
    }

    override suspend fun getTrending(): List<VideoItem> {
        return curatedVideos
    }

    override suspend fun search(query: String): List<VideoItem> {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isEmpty()) return curatedVideos

        val apiKey = apiKeyProvider().ifEmpty {
            runCatching { BuildConfig.YOUTUBE_API_KEY }.getOrDefault("")
        }

        if (apiKey.isNotBlank() && apiKey != "DEFAULT_KEY") {
            try {
                val response = apiService.searchVideos(query = trimmedQuery, apiKey = apiKey)
                val items = response.items.mapNotNull { item ->
                    val videoId = item.id?.videoId ?: return@mapNotNull null
                    val snippet = item.snippet ?: return@mapNotNull null
                    val thumbUrl = snippet.thumbnails?.high?.url
                        ?: snippet.thumbnails?.medium?.url
                        ?: "https://img.youtube.com/vi/$videoId/hqdefault.jpg"

                    VideoItem(
                        id = videoId,
                        title = snippet.title.orEmpty(),
                        description = snippet.description.orEmpty(),
                        thumbnailUrl = thumbUrl,
                        provider = VideoProviderType.YOUTUBE,
                        videoUrl = "https://www.youtube.com/watch?v=$videoId",
                        duration = "Video",
                        publishedAt = snippet.publishedAt.orEmpty().take(10),
                        channelName = snippet.channelTitle ?: "YouTube Channel",
                        channelId = snippet.channelId,
                        durationSeconds = 0L
                    )
                }
                if (items.isNotEmpty()) return items
            } catch (e: Exception) {
                // If API quota or network error occurs, proceed to curated search fallback
            }
        }

        // Curated search match
        val filtered = curatedVideos.filter {
            it.title.contains(trimmedQuery, ignoreCase = true) ||
                it.description.contains(trimmedQuery, ignoreCase = true) ||
                it.channelName.contains(trimmedQuery, ignoreCase = true)
        }
        if (filtered.isNotEmpty()) return filtered

        // Check if query is a direct YouTube URL or Video ID
        extractVideoId(trimmedQuery)?.let { id ->
            return listOf(
                VideoItem(
                    id = id,
                    title = "YouTube Video ($id)",
                    description = "Direct YouTube video link requested by user.",
                    thumbnailUrl = "https://img.youtube.com/vi/$id/hqdefault.jpg",
                    provider = VideoProviderType.YOUTUBE,
                    videoUrl = "https://www.youtube.com/watch?v=$id",
                    duration = "Video",
                    publishedAt = "Recent",
                    channelName = "YouTube",
                    durationSeconds = 0L
                )
            )
        }

        return curatedVideos
    }

    override fun canEmbed(url: String): Boolean = true

    override fun getEmbedUrl(item: VideoItem): String {
        val videoId = extractVideoId(item.videoUrl) ?: item.id
        return "https://www.youtube-nocookie.com/embed/$videoId?autoplay=1&playsinline=1&rel=0&modestbranding=1&enablejsapi=1"
    }

    override fun getOriginalUrl(item: VideoItem): String {
        return item.videoUrl.ifEmpty { "https://www.youtube.com/watch?v=${item.id}" }
    }

    private fun extractVideoId(input: String): String? {
        if (input.matches(Regex("^[a-zA-Z0-9_-]{11}$"))) {
            return input
        }
        val regex = Regex("""(?:youtu\.be/|youtube\.com/(?:embed/|v/|watch\?v=|watch\?.+&v=))([\w-]{11})""")
        return regex.find(input)?.groupValues?.get(1)
    }
}
