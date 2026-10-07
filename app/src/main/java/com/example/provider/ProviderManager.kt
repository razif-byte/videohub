package com.example.provider

import com.example.domain.model.VideoItem
import com.example.domain.model.VideoProviderType
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class ProviderManager {

    private val providers = mutableMapOf<String, VideoProvider>()

    fun registerProvider(provider: VideoProvider) {
        providers[provider.id] = provider
    }

    fun unregisterProvider(id: String) {
        providers.remove(id)
    }

    fun getProviders(): List<VideoProvider> {
        return providers.values.filter { it.isEnabled }
    }

    fun getProvider(type: VideoProviderType): VideoProvider? {
        return providers.values.firstOrNull { it.type == type && it.isEnabled }
    }

    fun getProviderById(id: String): VideoProvider? {
        return providers[id]
    }

    suspend fun getFeaturedVideos(): List<VideoItem> = coroutineScope {
        providers.values
            .filter { it.isEnabled }
            .map { provider ->
                async {
                    runCatching { provider.getFeatured() }.getOrDefault(emptyList())
                }
            }
            .awaitAll()
            .flatten()
    }

    suspend fun getTrendingByProvider(): Map<VideoProviderType, List<VideoItem>> = coroutineScope {
        val activeProviders = providers.values.filter { it.isEnabled }
        val deferredMap = activeProviders.map { provider ->
            provider.type to async {
                runCatching { provider.getTrending() }.getOrDefault(emptyList())
            }
        }
        deferredMap.associate { (type, deferred) ->
            type to deferred.await()
        }
    }

    suspend fun search(query: String, typeFilter: VideoProviderType? = null): List<VideoItem> = coroutineScope {
        val targets = if (typeFilter == null) {
            providers.values.filter { it.isEnabled }
        } else {
            providers.values.filter { it.isEnabled && it.type == typeFilter }
        }

        targets.map { provider ->
            async {
                runCatching { provider.search(query) }.getOrDefault(emptyList())
            }
        }
        .awaitAll()
        .flatten()
    }
}
