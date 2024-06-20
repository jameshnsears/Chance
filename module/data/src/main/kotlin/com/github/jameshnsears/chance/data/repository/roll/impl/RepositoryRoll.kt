package com.github.jameshnsears.chance.data.repository.roll.impl

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.domain.state.Roll
import com.github.jameshnsears.chance.data.domain.state.RollHistory
import com.github.jameshnsears.chance.data.domain.state.Side
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollInterface
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class RepositoryRoll private constructor(private val context: Context) :
    RepositoryRollInterface {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: RepositoryRoll? = null

        fun getInstance(context: Context): RepositoryRoll {
            if (instance == null) {
                instance = RepositoryRoll(context)
            }
            return instance!!
        }
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

        emit(rollHistory)
    }

    override suspend fun store(newRollHistory: RollHistory) {
        Timber.d(newRollHistory.toString())

        context.rollDataStore.updateData {
            mapRollHistoryIntoRollHistoryProtocolBuffer(newRollHistory, it).build()
        }
    }

    private fun mapRollHistoryIntoRollHistoryProtocolBuffer(
        rollHistory: RollHistory,
        rollHistoryProtocolBuffer: RollHistoryProtocolBuffer,
    ): RollHistoryProtocolBuffer.Builder {
        val rollHistoryProtocolBufferBuilder = rollHistoryProtocolBuffer.toBuilder()
        mapBagIntoBagProtocolBufferBuilder(rollHistory, rollHistoryProtocolBufferBuilder)
        return rollHistoryProtocolBufferBuilder
    }

    override suspend fun exportJson(): String =
        JsonFormat.printer().includingDefaultValueFields()
            .print(context.rollDataStore.data.first())

    override suspend fun importJson(json: String) {
        context.rollDataStore.updateData {
            val rollHistoryProtocolBuffer: RollHistoryProtocolBuffer.Builder =
                RollHistoryProtocolBuffer.newBuilder()
            JsonFormat.parser().merge(json, rollHistoryProtocolBuffer)
            rollHistoryProtocolBuffer.build()
        }
    }

    override suspend fun clear() {
        context.rollDataStore.updateData {
            RollHistoryProtocolBuffer.newBuilder().build()
        }
    }
}

val Context.rollDataStore: DataStore<RollHistoryProtocolBuffer> by dataStore(
    // /data/data/com.github.jameshnsears.chance.test.test/files/datastore
    fileName = "roll.pb",
    serializer = RollHistoryProtocolBufferSerializer,
)
