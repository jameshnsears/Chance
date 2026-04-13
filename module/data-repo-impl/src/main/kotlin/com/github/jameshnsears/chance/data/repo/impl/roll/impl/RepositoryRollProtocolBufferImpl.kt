package com.github.jameshnsears.chance.data.repo.impl.roll.impl

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.repo.impl.BuildConfig
import com.github.jameshnsears.chance.data.repo.impl.roll.RepositoryRollProtocolBufferInterface
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class RepositoryRollProtocolBufferImpl private constructor(private val context: Context) :
    RepositoryRollProtocolBufferInterface {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: RepositoryRollProtocolBufferImpl? = null

        fun getInstance(
            context: Context,
            rollHistory: RollHistory
        ): RepositoryRollProtocolBufferImpl {
            if (instance == null) {
                instance = RepositoryRollProtocolBufferImpl(context)

                CoroutineScope(Dispatchers.IO).launch {
                    if (BuildConfig.DEBUG) {
                        if (!UtilityFeature.isEnabled(UtilityFeature.Flag.REPO_PROTOCOL_BUFFER)) {
                            instance!!.clear()
                        }
                    }

                    if (instance!!.fetch().first().isEmpty()) {
                        instance!!.store(rollHistory)
                        instance!!.traceUuid(rollHistory)
                    }
                }
            }

            return instance!!
        }
    }

    override suspend fun jsonExport(): String = withContext(Dispatchers.IO) {
        JsonFormat.printer()
            .print(context.rollDataStore.data.first())
    }

    override suspend fun jsonImport(json: String) {
        store(jsonImportProcess(json))
    }

    override suspend fun fetch(): Flow<RollHistory> = flow {
        Timber.d("repositoryRoll.FETCH.start ============================================")

        val rollHistory = RollHistory()

        context.rollDataStore.data
            .map { rollHistoryProtocolBuffer ->
                rollHistoryProtocolBuffer.valuesMap.forEach { mapEntry ->
                    val rollList = mutableListOf<Roll>()

                    mapEntry.value.rollList.forEach { rollProtocolBuffer ->
                        val sideProtocolBuffer = rollProtocolBuffer.side

                        rollList.add(
                            Roll(
                                rollProtocolBuffer.diceEpoch,
                                Side(
                                    uuid = sideProtocolBuffer.uuid,
                                    number = sideProtocolBuffer.number,
                                    numberColour = sideProtocolBuffer.numberColour,
                                    imageBase64 = sideProtocolBuffer.imageBase64,
                                    imageDrawableId = sideProtocolBuffer.imageDrawableId,
                                    description = sideProtocolBuffer.description,
                                    descriptionColour = sideProtocolBuffer.descriptionColour
                                ),
                                rollProtocolBuffer.multiplierIndex,
                                rollProtocolBuffer.explodeIndex,
                                rollProtocolBuffer.scoreAdjustment,
                                rollProtocolBuffer.score
                            )
                        )
                    }
                    rollHistory[mapEntry.key] = rollList
                }
            }.first()

        Timber.d("repositoryRoll.FETCH.end ============================================")
        Timber.d("repositoryRoll.size=${rollHistory.size}")

        emit(rollHistory)
    }.flowOn(Dispatchers.IO)

    override suspend fun store(newRollHistory: RollHistory) {
        withContext(Dispatchers.IO) {
            clear()

            Timber.d("repositoryRoll.STORE ============================================")
            Timber.d("repositoryRoll.size=${newRollHistory.size}")

            context.rollDataStore.updateData {
                val rollHistoryProtocolBufferBuilder = it.toBuilder()
                mapRollHistoryIntoRollHistoryProtocolBufferBuilder(
                    newRollHistory,
                    rollHistoryProtocolBufferBuilder
                )
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
)
