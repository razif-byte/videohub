package com.example.provider

import com.example.domain.model.VideoItem
import com.example.domain.model.VideoProviderType

interface VideoProvider {
    val id: String
    val name: String
    val type: VideoProviderType
    val isEnabled: Boolean

    suspend fun getFeatured(): List<VideoItem>
    suspend fun getTrending(): List<VideoItem>
    suspend fun search(query: String): List<VideoItem>
    fun canEmbed(url: String): Boolean
    fun getEmbedUrl(item: VideoItem): String?
    fun getOriginalUrl(item: VideoItem): String
}
