package com.github.jameshnsears.chance.data.repository.roll.testdouble

import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollProtocolBufferInterface
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

class RepositoryRollProtocolBufferTestDouble private constructor() :
    RepositoryRollProtocolBufferInterface {
    companion object {
        private var instance: RepositoryRollProtocolBufferTestDouble? = null

        fun getInstance(
            rollHistory: RollHistory
        ): RepositoryRollProtocolBufferTestDouble {
            synchronized(this) {
                if (instance == null) {
                    runBlocking {
                        instance = RepositoryRollProtocolBufferTestDouble()

                        instance!!.store(rollHistory)
                        instance!!.traceUuid(rollHistory)
                    }
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

        mapRollHistoryIntoRollHistoryProtocolBufferBuilder(
            rollHistory,
            rollHistoryProtocolBufferBuilder
        )

        return JsonFormat.printer()
            .print(rollHistoryProtocolBufferBuilder.build())
    }

    override suspend fun jsonImport(json: String) {
        store(jsomImportProcess(json))
    }

    override suspend fun clear() {
        rollHistory.clear()
    }
}
