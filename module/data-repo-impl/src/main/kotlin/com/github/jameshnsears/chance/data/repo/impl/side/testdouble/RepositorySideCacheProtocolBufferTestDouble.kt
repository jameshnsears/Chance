package com.github.jameshnsears.chance.data.repo.impl.side.testdouble

import com.github.jameshnsears.chance.data.repo.api.side.RepositorySideCacheInterface
import com.github.jameshnsears.chance.data.repo.impl.utility.svg.UtilitySvgSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber

class RepositorySideCacheProtocolBufferTestDouble private constructor() : RepositorySideCacheInterface {
    companion object {
        private var instance: RepositorySideCacheProtocolBufferTestDouble? = null

        fun getInstance(): RepositorySideCacheProtocolBufferTestDouble {
            if (instance == null) {
                instance = RepositorySideCacheProtocolBufferTestDouble()
            }
            return instance!!
        }
    }

    private val _cache = MutableStateFlow<Map<String, String>>(emptyMap())

    override fun fetch(): Flow<Map<String, String>> = _cache.asStateFlow()

    override suspend fun store(uuid: String, imageBase64: String) {
        store(mapOf(uuid to imageBase64))
    }

    override suspend fun store(entries: Map<String, String>) {
        if (entries.isEmpty()) return

        _cache.update { current ->
            val newEntries = entries.filterKeys { !current.containsKey(it) && it.isNotEmpty() }
                .filterValues { it.isNotEmpty() }

            if (newEntries.isEmpty()) {
                current
            } else {
                val updatedMap = current.toMutableMap()
                newEntries.forEach { (uuid, imageBase64) ->
                    val svg = String(UtilitySvgSerializer.decodeBase64StringIntoByteArray(imageBase64))
                    updatedMap[uuid] = svg
                    Timber.d("repositorySideCache.TEST_DOUBLE.ADD: uuid=$uuid; svg.length=${svg.length}")
                }
                updatedMap
            }
        }
    }

    override suspend fun prune(referencedUuids: Set<String>) {
        val startSize = _cache.value.size
        _cache.update { current ->
            current.filterKeys { referencedUuids.contains(it) }
        }
        Timber.d("repositorySideCache.TEST_DOUBLE.PRUNE: startSize=$startSize; endSize=${_cache.value.size}")
    }

    override suspend fun clear() {
        _cache.value = emptyMap()
    }
}
