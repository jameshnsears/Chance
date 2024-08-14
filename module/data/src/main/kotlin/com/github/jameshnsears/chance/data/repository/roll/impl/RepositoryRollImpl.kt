package com.github.jameshnsears.chance.data.repository.roll.impl

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.github.jameshnsears.chance.data.BuildConfig
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.utility.feature.UtilityFeature
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class RepositoryRollImpl private constructor(private val context: Context) :
    RepositoryRollInterface {
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: RepositoryRollImpl? = null

        fun getInstance(
            context: Context,
            rollHistory: RollHistory
        ): RepositoryRollImpl {
            synchronized(this) {

                if (instance == null) {
                    instance = RepositoryRollImpl(context)

                    runBlocking {
                        if (BuildConfig.DEBUG) {
                            if (!UtilityFeature.isEnabled(UtilityFeature.Flag.USE_PROTO_REPO)) {
                                instance!!.clear()

                                if (instance!!.fetch().first().size == 0)
                                    instance!!.store(rollHistory)
                            }
                        }

                        instance!!.traceUuid(instance!!.fetch().first())
                    }
                }
            }

            return instance!!
        }
    }

    override suspend fun jsonExport(): String =
        JsonFormat.printer().includingDefaultValueFields()
            .print(context.rollDataStore.data.first())

    override suspend fun jsonImport(json: String) {
        store(jsomImportProcess(json))
    }

    override suspend fun fetch(): Flow<RollHistory> = flow {
        val rollHistory = RollHistory()

        context.rollDataStore.data
            .map { rollHistoryProtocolBuffer ->
                // LinkedHashMap<Long, List<Roll>>
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

        Timber.d("repositoryRoll.FETCH ============================================")
        Timber.d("repositoryRoll.size=${rollHistory.size}")

        emit(rollHistory)
    }

    override suspend fun store(newRollHistory: RollHistory) {
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

    override suspend fun clear() {
        context.rollDataStore.updateData {
            it.toBuilder().clear().build()
        }
    }
}

val Context.rollDataStore: DataStore<RollHistoryProtocolBuffer> by dataStore(
    // /data/data/com.github.jameshnsears.chance.test.test/files/datastore
    fileName = "roll.pb",
    serializer = RollHistoryProtocolBufferSerializer,
)
