package com.github.jameshnsears.chance.data.repo.impl.side.impl

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import com.github.jameshnsears.chance.data.domain.proto.SideCacheProtocolBuffer
import com.github.jameshnsears.chance.data.repo.api.side.RepositorySideCacheInterface
import com.github.jameshnsears.chance.data.repo.impl.utility.svg.UtilitySvgSerializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber

class RepositorySideCacheProtocolBufferImpl private constructor(private val context: Context) : RepositorySideCacheInterface {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: RepositorySideCacheProtocolBufferImpl? = null

        fun getInstance(context: Context): RepositorySideCacheProtocolBufferImpl {
            if (instance == null) {
                instance = RepositorySideCacheProtocolBufferImpl(context)
            }
            return instance!!
        }
    }

    override fun fetch(): Flow<Map<String, String>> = context.sideCacheDataStore.data
        .map { proto ->
            proto.entriesMap
        }

    override suspend fun store(uuid: String, imageBase64: String) {
        store(mapOf(uuid to imageBase64))
    }

    override suspend fun store(entries: Map<String, String>) {
        if (entries.isEmpty()) return

        Timber.d("repositorySideCache.STORE ============================================")

        withContext(Dispatchers.IO) {
            context.sideCacheDataStore.updateData { currentCache ->
                val newEntries = entries.filterKeys { !currentCache.containsEntries(it) && it.isNotEmpty() }
                    .filterValues { it.isNotEmpty() }

                if (newEntries.isEmpty()) {
                    currentCache
                } else {
                    val builder = currentCache.toBuilder()
                    newEntries.forEach { (uuid, imageBase64) ->
                        val svg = String(UtilitySvgSerializer.decodeBase64StringIntoByteArray(imageBase64))
                        builder.putEntries(uuid, svg)
                        Timber.d("repositorySideCache: uuid=$uuid; svg.length=${svg.length}")
                    }

                    Timber.d("repositorySideCache.size=${entries.size}")
                    builder.build()

                }
            }
        }
    }

    override suspend fun prune(referencedUuids: Set<String>) {
        withContext(Dispatchers.IO) {
            context.sideCacheDataStore.updateData { currentCache ->
                val startSize = currentCache.entriesCount
                val entriesToKeep = currentCache.entriesMap.filterKeys { referencedUuids.contains(it) }

                if (entriesToKeep.size == startSize) {
                    currentCache
                } else {
                    val builder = currentCache.toBuilder().clearEntries().putAllEntries(entriesToKeep)
                    val result = builder.build()
                    Timber.d("repositorySideCache.CACHE.PRUNE: startSize=$startSize; endSize=${result.entriesCount}")
                    result
                }
            }
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            context.sideCacheDataStore.updateData {
                it.toBuilder().clear().build()
            }
        }
    }
}

val Context.sideCacheDataStore: DataStore<SideCacheProtocolBuffer> by dataStore(
    fileName = "side_cache.pb",
    serializer = RepositorySideCacheProtocolBufferSerializer,
    corruptionHandler = ReplaceFileCorruptionHandler {
        SideCacheProtocolBuffer.getDefaultInstance()
    },
)
