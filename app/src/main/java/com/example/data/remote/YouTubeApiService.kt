package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Query

@JsonClass(generateAdapter = true)
data class YouTubeSearchResponse(
    @field:Json(name = "items") val items: List<YouTubeSearchItem> = emptyList()
)

@JsonClass(generateAdapter = true)
data class YouTubeSearchItem(
    @field:Json(name = "id") val id: YouTubeIdInfo?,
    @field:Json(name = "snippet") val snippet: YouTubeSnippetInfo?
)

@JsonClass(generateAdapter = true)
data class YouTubeIdInfo(
    @field:Json(name = "videoId") val videoId: String?
)

@JsonClass(generateAdapter = true)
data class YouTubeSnippetInfo(
    @field:Json(name = "title") val title: String?,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "channelTitle") val channelTitle: String?,
    @field:Json(name = "channelId") val channelId: String?,
    @field:Json(name = "publishedAt") val publishedAt: String?,
    @field:Json(name = "thumbnails") val thumbnails: YouTubeThumbnails?
)

@JsonClass(generateAdapter = true)
data class YouTubeThumbnails(
    @field:Json(name = "high") val high: YouTubeThumbnail?,
    @field:Json(name = "medium") val medium: YouTubeThumbnail?,
    @field:Json(name = "default") val default: YouTubeThumbnail?
)

@JsonClass(generateAdapter = true)
data class YouTubeThumbnail(
    @field:Json(name = "url") val url: String?
)

interface YouTubeApiService {
    @GET("youtube/v3/search")
    suspend fun searchVideos(
        @Query("part") part: String = "snippet",
        @Query("maxResults") maxResults: Int = 20,
        @Query("q") query: String,
        @Query("type") type: String = "video",
        @Query("key") apiKey: String
    ): YouTubeSearchResponse
}
