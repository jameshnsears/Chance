package com.github.jameshnsears.chance.data.repository.roll.impl

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollInterface
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
        private var instance: RepositoryRollImpl? = null

        fun getInstance(
            context: Context,
            rollHistory: RollHistory = RollHistory()
        ): RepositoryRollImpl {
            if (instance == null) {
                instance = RepositoryRollImpl(context)
                runBlocking {
                    if (instance!!.fetch().first().size == 0)
                        Timber.d("default")
                        instance!!.store(rollHistory)
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
        clear()

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
            RollHistoryProtocolBuffer.newBuilder().build()
        }
    }
}

val Context.rollDataStore: DataStore<RollHistoryProtocolBuffer> by dataStore(
    // /data/data/com.github.jameshnsears.chance.test.test/files/datastore
    fileName = "roll.pb",
    serializer = RollHistoryProtocolBufferSerializer,
)
