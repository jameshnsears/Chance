package com.github.jameshnsears.chance.data.repo.impl.side.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.github.jameshnsears.chance.data.repo.api.side.RepositorySideCacheInterface
import com.github.jameshnsears.chance.data.repo.impl.sqldelight.ChanceDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class RepositorySideCacheSqlDelightImpl(
    private val database: ChanceDatabase
) : RepositorySideCacheInterface {

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val sideCacheFlow = database.imageQueries.getAllImages()
        .asFlow()
        .mapToList(Dispatchers.IO)
        .map { imageEntities ->
            imageEntities.associate { it.uuid to (it.base64 ?: it.svg ?: "") }
        }.shareIn(
            scope = repositoryScope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        )

    override fun fetch(): Flow<Map<String, String>> = sideCacheFlow

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun store(uuid: String, imageBase64: String) {
        withContext(Dispatchers.IO) {
            val decoded = try {
                String(Base64.decode(imageBase64))
            } catch (_: Exception) {
                null
            }

            val isSvg = decoded != null && decoded.contains("<svg") && decoded.contains("</svg>")

            database.imageQueries.saveImage(
                uuid = uuid,
                base64 = if (isSvg) null else imageBase64,
                svg = if (isSvg) decoded else null
            )
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun store(entries: Map<String, String>) {
        withContext(Dispatchers.IO) {
            database.transaction {
                entries.forEach { (uuid, imageBase64) ->
                    val decoded = try {
                        String(Base64.decode(imageBase64))
                    } catch (_: Exception) {
                        null
                    }

                    val isSvg = decoded != null && decoded.contains("<svg") && decoded.contains("</svg>")

                    database.imageQueries.saveImage(
                        uuid = uuid,
                        base64 = if (isSvg) null else imageBase64,
                        svg = if (isSvg) decoded else null
                    )
                }
            }
        }
    }

    override suspend fun prune(referencedUuids: Set<String>) {
        withContext(Dispatchers.IO) {
            database.imageQueries.pruneImages(referencedUuids.toList())
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.imageQueries.clear()
        }
    }
}
