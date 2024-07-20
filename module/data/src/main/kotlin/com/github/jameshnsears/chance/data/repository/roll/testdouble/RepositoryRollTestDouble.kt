package com.github.jameshnsears.chance.data.repository.roll.testdouble

import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollInterface
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

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
        rollHistory = newRollHistory
    }

    override suspend fun jsonExport(): String {
        val rollHistoryProtocolBufferBuilder: RollHistoryProtocolBuffer.Builder =
            RollHistoryProtocolBuffer.newBuilder()

        mapRollHistoryIntoRollHistoryProtocolBufferBuilder(rollHistory, rollHistoryProtocolBufferBuilder)

        return JsonFormat.printer().includingDefaultValueFields()
            .print(rollHistoryProtocolBufferBuilder.build())
    }

    override suspend fun jsonImport(json: String) {
        store(jsomImportProcess(json))
    }

    override suspend fun clear() {
        rollHistory.clear()
    }
}
