package com.github.jameshnsears.chance.data.repo.api.side

import kotlinx.coroutines.flow.Flow

interface RepositorySideCacheInterface {
    fun fetch(): Flow<Map<String, String>>
    suspend fun store(uuid: String, imageBase64: String)
    suspend fun store(entries: Map<String, String>)
    suspend fun prune(referencedUuids: Set<String>)
    suspend fun clear()
}
