package com.github.jameshnsears.chance.data.repository.roll.testdouble

import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollInterface
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class RepositoryRollTestDouble private constructor() :
    RepositoryRollInterface {
    companion object {
        private var instance: RepositoryRollTestDouble? = null

        fun getInstance(
            rollHistory: RollHistory
        ): RepositoryRollTestDouble {
            if (instance == null) {
                instance = RepositoryRollTestDouble()
                runBlocking {
                    instance!!.store(
                        rollHistory,
                    )
                }
            }
            return instance!!
        }
    }

    private lateinit var rollHistory: RollHistory

    override suspend fun fetch(): Flow<RollHistory> = flow {
        emit(rollHistory)
    }

    override suspend fun store(newRollHistory: RollHistory) {
        Timber.d(newRollHistory.toString())
        rollHistory = newRollHistory
    }

    override suspend fun jsonExport(): String {
        val rollHistoryProtocolBufferBuilder: RollHistoryProtocolBuffer.Builder =
            RollHistoryProtocolBuffer.newBuilder()

        mapBagIntoBagProtocolBufferBuilder(rollHistory, rollHistoryProtocolBufferBuilder)

        return JsonFormat.printer().includingDefaultValueFields()
            .print(rollHistoryProtocolBufferBuilder.build())
    }

    override suspend fun jsonImport(json: String) {
        rollHistory = RollHistory()

        val rollHistoryProtocolBufferBuilder: RollHistoryProtocolBuffer.Builder =
            RollHistoryProtocolBuffer.newBuilder()

        JsonFormat.parser().merge(json, rollHistoryProtocolBufferBuilder)

        val newRollHistory: RollHistory = LinkedHashMap()

        rollHistoryProtocolBufferBuilder.valuesMap.forEach { mapEntry ->
            val rollList = mutableListOf<Roll>()
            mapEntry.value.rollList.forEach {
                val side = Side()
                side.number = it.side.number
                side.numberColour = it.side.numberColour
                side.imageBase64 = it.side.imageBase64
                side.imageDrawableId = it.side.imageDrawableId
                side.description = it.side.description
                side.descriptionColour = it.side.descriptionColour

                rollList.add(
                    Roll(
                        diceEpoch = it.diceEpoch,
                        side = side,
                        multiplierIndex = it.multiplierIndex,
                        explodeIndex = it.explodeIndex,
                        scoreAdjustment = it.scoreAdjustment,
                        score = it.score
                    )
                )
            }

            newRollHistory[mapEntry.key] = rollList
        }

        store(newRollHistory)
    }

    override suspend fun clear() {
        rollHistory.clear()
    }
}
