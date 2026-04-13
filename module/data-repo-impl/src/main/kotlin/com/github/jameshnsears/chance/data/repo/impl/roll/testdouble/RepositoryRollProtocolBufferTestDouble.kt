package com.github.jameshnsears.chance.data.repo.impl.roll.testdouble

import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.repo.impl.roll.RepositoryRollProtocolBufferInterface
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class RepositoryRollProtocolBufferTestDouble private constructor() :
    RepositoryRollProtocolBufferInterface {
    companion object {
        private var instance: RepositoryRollProtocolBufferTestDouble? = null

        fun getInstance(
            rollHistory: RollHistory
        ): RepositoryRollProtocolBufferTestDouble {
            if (instance == null) {
                instance = RepositoryRollProtocolBufferTestDouble()

                CoroutineScope(Dispatchers.IO).launch {
                    instance!!.store(rollHistory)
                    instance!!.traceUuid(rollHistory)
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
        store(jsonImportProcess(json))
    }

    override suspend fun clear() {
        rollHistory.clear()
    }
}
