package com.github.jameshnsears.chance.data.repo.impl.roll.impl

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.RollListProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.impl.RepositoryProtocolBufferImageCache
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
    private val repositoryBag: RepositoryBagInterface? = null
) :
    RepositoryRollProtocolBufferInterface {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: RepositoryRollProtocolBufferImpl? = null

        fun getInstance(
            context: Context,
            repositoryBag: RepositoryBagInterface? = null
        ): RepositoryRollProtocolBufferImpl {
            if (instance == null) {
                instance = RepositoryRollProtocolBufferImpl(context, repositoryBag)
            }

            return instance!!
        }
    }

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val rollHistoryFlow = flow {
        if (repositoryBag != null) {
            emitAll(
                context.rollDataStore.data.combine(repositoryBag.fetch()) { rollHistoryProtocolBuffer, diceBag ->
                    val startTime = System.currentTimeMillis()
                    val rollHistory = mapRollHistoryProtocolBufferIntoRollHistory(
                        rollHistoryProtocolBuffer,
                        diceBag
                    )

                    Timber.d("repositoryRoll.FETCH ============================================")
                    Timber.d("repositoryRoll.size=${rollHistory.size}; mapping_time=${System.currentTimeMillis() - startTime}ms")

                    rollHistory
                }
            )
        } else {
            emitAll(
                context.rollDataStore.data.map { rollHistoryProtocolBuffer ->
                    val startTime = System.currentTimeMillis()
                    val rollHistory = mapRollHistoryProtocolBufferIntoRollHistory(rollHistoryProtocolBuffer)

                    Timber.d("repositoryRoll.FETCH ============================================")
                    Timber.d("repositoryRoll.size=${rollHistory.size}; mapping_time=${System.currentTimeMillis() - startTime}ms")

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
        fieldsToAlwaysOutput.add(RollProtocolBuffer.getDescriptor().findFieldByName("multiplierIndex"))
        fieldsToAlwaysOutput.add(RollProtocolBuffer.getDescriptor().findFieldByName("explodeIndex"))
        fieldsToAlwaysOutput.add(RollProtocolBuffer.getDescriptor().findFieldByName("scoreAdjustment"))
        fieldsToAlwaysOutput.add(RollProtocolBuffer.getDescriptor().findFieldByName("score"))

        val rollHistoryProtocolBuffer = context.rollDataStore.data.first()
        val rollHistory = mapRollHistoryProtocolBufferIntoRollHistory(rollHistoryProtocolBuffer)

        val exportedBuilder = RollHistoryProtocolBuffer.newBuilder()
        mapRollHistoryIntoRollHistoryProtocolBufferBuilder(
            rollHistory,
            exportedBuilder,
            useCache = false,
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

    override suspend fun store(newRollHistory: RollHistory) {
        withContext(Dispatchers.IO) {
            Timber.d("repositoryRoll.STORE ============================================")
            Timber.d("repositoryRoll.size=${newRollHistory.size}")

            context.rollDataStore.updateData {
                val rollHistoryProtocolBufferBuilder = RollHistoryProtocolBuffer.newBuilder()
                mapRollHistoryIntoRollHistoryProtocolBufferBuilder(
                    newRollHistory,
                    rollHistoryProtocolBufferBuilder
                )
                rollHistoryProtocolBufferBuilder.build()
            }
        }
    }

    override suspend fun store(epoch: Long, rollList: List<Roll>) {
        withContext(Dispatchers.IO) {
            Timber.d("repositoryRoll.STORE.INCREMENTAL ============================================")

            context.rollDataStore.updateData { currentRollHistoryProtocolBuffer ->
                val rollHistoryProtocolBufferBuilder = currentRollHistoryProtocolBuffer.toBuilder()

                val imageCache = mutableMapOf<String, String>()
                val existingCacheRollList = rollHistoryProtocolBufferBuilder.getValuesOrDefault(
                    RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE,
                    RollListProtocolBuffer.getDefaultInstance()
                )
                existingCacheRollList.rollList.forEach { imageCache[it.side.uuid] = it.side.imageBase64 }

                mapRollHistoryEntryIntoRollHistoryProtocolBufferBuilder(
                    epoch,
                    rollList,
                    rollHistoryProtocolBufferBuilder,
                    imageCache
                )
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
                        .filter { it != RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE }
                        .maxOrNull()
                    if (latestEpoch != null) {
                        rollHistoryProtocolBufferBuilder.removeValues(latestEpoch)
                        pruneCache(rollHistoryProtocolBufferBuilder)
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
