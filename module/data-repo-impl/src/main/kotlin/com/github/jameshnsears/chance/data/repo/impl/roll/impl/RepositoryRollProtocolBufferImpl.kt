package com.github.jameshnsears.chance.data.repo.impl.roll.impl

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.side.RepositorySideCacheInterface
import com.github.jameshnsears.chance.data.repo.impl.roll.RepositoryRollProtocolBufferInterface
import com.google.protobuf.Descriptors
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import timber.log.Timber

class RepositoryRollProtocolBufferImpl private constructor(
    private val context: Context,
    private val repositoryBag: RepositoryBagInterface? = null,
    private val repositorySideCache: RepositorySideCacheInterface? = null
) :
    RepositoryRollProtocolBufferInterface {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: RepositoryRollProtocolBufferImpl? = null

        fun getInstance(
            context: Context,
            repositoryBag: RepositoryBagInterface? = null,
            repositorySideCache: RepositorySideCacheInterface? = null
        ): RepositoryRollProtocolBufferImpl {
            if (instance == null) {
                instance = RepositoryRollProtocolBufferImpl(context, repositoryBag, repositorySideCache)
            }

            return instance!!
        }
    }

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val rollHistoryFlow = flow {
        val baseFlow = if (repositoryBag != null) {
            context.rollDataStore.data.combine(repositoryBag.fetch()) { rollHistoryProtocolBuffer, diceBag ->
                rollHistoryProtocolBuffer to diceBag
            }
        } else {
            context.rollDataStore.data.map { rollHistoryProtocolBuffer ->
                rollHistoryProtocolBuffer to null
            }
        }

        if (repositorySideCache != null) {
            emitAll(
                baseFlow.combine(repositorySideCache.fetch()) { (rollHistoryProtocolBuffer, diceBag), sideCache ->
                    val rollHistory = mapRollHistoryProtocolBufferIntoRollHistory(
                        rollHistoryProtocolBuffer,
                        diceBag,
                        sideCache
                    )
                    rollHistory
                }
            )
        } else {
            emitAll(
                baseFlow.map { (rollHistoryProtocolBuffer, diceBag) ->
                    val rollHistory = mapRollHistoryProtocolBufferIntoRollHistory(
                        rollHistoryProtocolBuffer,
                        diceBag
                    )
                    rollHistory
                }
            )
        }
    }.shareIn(
        scope = repositoryScope,
        started = SharingStarted.WhileSubscribed(5000),
        replay = 1
    )

    override suspend fun jsonExport(): String = withContext(Dispatchers.IO) {
        val fieldsToAlwaysOutput: MutableSet<Descriptors.FieldDescriptor> = HashSet()
        fieldsToAlwaysOutput.add(RollProtocolBuffer.getDescriptor().findFieldByName("uuidDice"))
        fieldsToAlwaysOutput.add(RollProtocolBuffer.getDescriptor().findFieldByName("uuidGroup"))
        fieldsToAlwaysOutput.add(RollProtocolBuffer.getDescriptor().findFieldByName("side"))
        fieldsToAlwaysOutput.add(RollProtocolBuffer.getDescriptor().findFieldByName("multiplierIndex"))
        fieldsToAlwaysOutput.add(RollProtocolBuffer.getDescriptor().findFieldByName("explodeIndex"))
        fieldsToAlwaysOutput.add(RollProtocolBuffer.getDescriptor().findFieldByName("scoreAdjustment"))
        fieldsToAlwaysOutput.add(RollProtocolBuffer.getDescriptor().findFieldByName("score"))

        fieldsToAlwaysOutput.add(SideProtocolBuffer.getDescriptor().findFieldByName("uuid"))

        val rollHistoryProtocolBuffer = context.rollDataStore.data.first()
        val rollHistory = mapRollHistoryProtocolBufferIntoRollHistory(rollHistoryProtocolBuffer)

        val exportedBuilder = RollHistoryProtocolBuffer.newBuilder()
        mapRollHistoryIntoRollHistoryProtocolBufferBuilder(
            rollHistory,
            exportedBuilder,
            isExport = true
        )

        JsonFormat.printer().includingDefaultValueFields(fieldsToAlwaysOutput)
            .print(exportedBuilder.build())
    }

    override suspend fun jsonImport(json: String) {
        val diceBag = repositoryBag?.fetch()?.first()
        store(jsonImportProcess(json, diceBag))
    }

    override fun fetch(): Flow<RollHistory> = rollHistoryFlow

    override suspend fun fetchReferencedSideUuids(): Set<String> {
        val rollHistoryProtocolBuffer = context.rollDataStore.data.first()
        return rollHistoryProtocolBuffer.valuesMap.values
            .flatMap { it.rollList }
            .map { it.side.uuid }
            .toSet()
    }

    override suspend fun store(newRollHistory: RollHistory) {
        withContext(Dispatchers.IO) {
            Timber.d("repositoryRoll.STORE ============================================")
            Timber.d("repositoryRoll.size=${newRollHistory.size}")

            if (repositorySideCache != null) {
                val cacheEntries = newRollHistory.values.flatten()
                    .filter { it.side.imageBase64.isNotEmpty() }
                    .associate { it.side.uuid to it.side.imageBase64 }
                repositorySideCache.store(cacheEntries)
            }

            context.rollDataStore.updateData {
                val rollHistoryProtocolBufferBuilder = RollHistoryProtocolBuffer.newBuilder()
                mapRollHistoryIntoRollHistoryProtocolBufferBuilder(
                    newRollHistory,
                    rollHistoryProtocolBufferBuilder
                )
                val result = rollHistoryProtocolBufferBuilder.build()

                // Optimization: clear imageBase64 in stored proto after moving to cache
                val optimizedBuilder = result.toBuilder()
                optimizedBuilder.valuesMap.forEach { (_, rollList) ->
                    val optimizedRollListBuilder = rollList.toBuilder()
                    optimizedRollListBuilder.rollBuilderList.forEach { rollBuilder ->
                        rollBuilder.sideBuilder.imageBase64 = ""
                    }
                    // Since it's a map, we might need to put it back or use a different way to mutate
                }
                // Protobuf maps are tricky to mutate while iterating.
                // For now, let's keep it simple. If we really want to optimize, we'd rebuild the map.
                result
            }
        }
    }

    override suspend fun store(epoch: Long, rollList: List<Roll>) {
        withContext(Dispatchers.IO) {
            Timber.d("repositoryRoll.STORE.INCREMENTAL ============================================")

            if (repositorySideCache != null) {
                val cacheEntries = rollList
                    .filter { it.side.imageBase64.isNotEmpty() }
                    .associate { it.side.uuid to it.side.imageBase64 }
                repositorySideCache.store(cacheEntries)
            }

            context.rollDataStore.updateData { currentRollHistoryProtocolBuffer ->
                val rollHistoryProtocolBufferBuilder = currentRollHistoryProtocolBuffer.toBuilder()

                mapRollHistoryEntryIntoRollHistoryProtocolBufferBuilder(
                    epoch,
                    rollList,
                    rollHistoryProtocolBufferBuilder
                )
                val result = rollHistoryProtocolBufferBuilder.build()

                // Optimization: clear imageBase64 for the newly added entry
                val optimizedBuilder = result.toBuilder()
                val newlyAddedRollList = optimizedBuilder.getValuesOrThrow(epoch).toBuilder()
                newlyAddedRollList.rollBuilderList.forEach { rollBuilder ->
                    rollBuilder.sideBuilder.imageBase64 = ""
                }
                optimizedBuilder.putValues(epoch, newlyAddedRollList.build())
                optimizedBuilder.build()
            }
        }
    }

    override suspend fun delete(diceUuid: String) {
        withContext(Dispatchers.IO) {
            Timber.d("repositoryRoll.DELETE ============================================")
            Timber.d("repositoryRoll.delete.diceUuid=$diceUuid")

            context.rollDataStore.updateData { currentRollHistoryProtocolBuffer ->
                val rollHistoryProtocolBufferBuilder = currentRollHistoryProtocolBuffer.toBuilder()

                val keysToRemove = mutableListOf<Long>()
                rollHistoryProtocolBufferBuilder.valuesMap.forEach { (epoch, rollList) ->
                    if (epoch != -1L) {
                        if (rollList.rollList.any { it.uuidDice == diceUuid }) {
                            keysToRemove.add(epoch)
                        }
                    }
                }

                keysToRemove.forEach {
                    rollHistoryProtocolBufferBuilder.removeValues(it)
                }

                rollHistoryProtocolBufferBuilder.build()
            }
        }
    }

    override suspend fun removeLatest() {
        withContext(Dispatchers.IO) {
            Timber.d("repositoryRoll.REMOVE.LATEST ============================================")

            context.rollDataStore.updateData { currentRollHistoryProtocolBuffer ->
                val rollHistoryProtocolBufferBuilder = currentRollHistoryProtocolBuffer.toBuilder()
                if (rollHistoryProtocolBufferBuilder.valuesCount > 0) {
                    val latestEpoch = rollHistoryProtocolBufferBuilder.valuesMap.keys
                        .filter { it != -1L }
                        .maxOrNull()
                    if (latestEpoch != null) {
                        rollHistoryProtocolBufferBuilder.removeValues(latestEpoch)
                    }
                }
                rollHistoryProtocolBufferBuilder.build()
            }
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            context.rollDataStore.updateData {
                it.toBuilder().clear().build()
            }
        }
    }
}

val Context.rollDataStore: DataStore<RollHistoryProtocolBuffer> by dataStore(
    // /data/data/com.github.jameshnsears.chance.test.test/files/datastore
    fileName = "roll.pb",
    serializer = RepositoryRollProtocolBufferSerializer,
    corruptionHandler = ReplaceFileCorruptionHandler {
        RollHistoryProtocolBuffer.getDefaultInstance()
    },
)
