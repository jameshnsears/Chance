package com.github.jameshnsears.chance.data.repo.impl.roll.testdouble

import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.repo.impl.roll.RepositoryRollProtocolBufferInterface
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class RepositoryRollProtocolBufferTestDouble private constructor() :
    RepositoryRollProtocolBufferInterface {
    private val rollHistoryProtocolBufferStateFlow =
        MutableStateFlow(RollHistoryProtocolBuffer.getDefaultInstance())

    private var initialized = false

    companion object {
        private var instance: RepositoryRollProtocolBufferTestDouble? = null

        fun getInstance(
            rollHistory: RollHistory,
        ): RepositoryRollProtocolBufferTestDouble {
            if (instance == null) {
                instance = RepositoryRollProtocolBufferTestDouble()
            }

            if (!instance!!.initialized) {
                instance!!.updateStateFlow(rollHistory)
                instance!!.traceUuid(rollHistory)
                instance!!.initialized = true
            }
            return instance!!
        }
    }

    private fun updateStateFlow(rollHistory: RollHistory) {
        val rollHistoryProtocolBufferBuilder = RollHistoryProtocolBuffer.newBuilder()
        mapRollHistoryIntoRollHistoryProtocolBufferBuilder(
            rollHistory,
            rollHistoryProtocolBufferBuilder
        )
        rollHistoryProtocolBufferStateFlow.value = rollHistoryProtocolBufferBuilder.build()
    }

    override suspend fun jsonExport(): String {
        return JsonFormat.printer()
            .print(rollHistoryProtocolBufferStateFlow.value)
    }

    override suspend fun jsonImport(json: String) {
        store(jsonImportProcess(json))
    }

    override suspend fun fetch(): Flow<RollHistory> = rollHistoryProtocolBufferStateFlow
        .map { rollHistoryProtocolBuffer ->
            Timber.d("repositoryRoll.FETCH.start ============================================")

            val rollHistory = mapRollHistoryProtocolBufferIntoRollHistory(rollHistoryProtocolBuffer)

            Timber.d("repositoryRoll.FETCH.end ============================================")
            Timber.d("repositoryRoll.size=${rollHistory.size}")

            rollHistory
        }

    override suspend fun store(newRollHistory: RollHistory) {
        Timber.d("repositoryRoll.STORE ============================================")
        Timber.d("repositoryRoll.size=${newRollHistory.size}")

        updateStateFlow(newRollHistory)
    }

    override suspend fun clear() {
        rollHistoryProtocolBufferStateFlow.value = RollHistoryProtocolBuffer.getDefaultInstance()
    }
}
