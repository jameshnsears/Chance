package com.github.jameshnsears.chance.data.repo.impl.roll.testdouble

import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.impl.roll.RepositoryRollProtocolBufferInterface
import com.google.protobuf.Descriptors
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber

class RepositoryRollProtocolBufferTestDouble private constructor(
    private val repositoryBag: RepositoryBagInterface? = null
) :
    RepositoryRollProtocolBufferInterface {
    private val rollHistoryProtocolBufferStateFlow =
        MutableStateFlow(RollHistoryProtocolBuffer.getDefaultInstance())

    private var initialized = false

    companion object {
        private var instance: RepositoryRollProtocolBufferTestDouble? = null

        fun getInstance(
            rollHistory: RollHistory,
            repositoryBag: RepositoryBagInterface? = null
        ): RepositoryRollProtocolBufferTestDouble {
            if (instance == null) {
                instance = RepositoryRollProtocolBufferTestDouble(repositoryBag)
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
        val fieldsToAlwaysOutput: MutableSet<Descriptors.FieldDescriptor> = HashSet()
        fieldsToAlwaysOutput.add(RollProtocolBuffer.getDescriptor().findFieldByName("multiplierIndex"))
        fieldsToAlwaysOutput.add(RollProtocolBuffer.getDescriptor().findFieldByName("explodeIndex"))
        fieldsToAlwaysOutput.add(RollProtocolBuffer.getDescriptor().findFieldByName("scoreAdjustment"))
        fieldsToAlwaysOutput.add(RollProtocolBuffer.getDescriptor().findFieldByName("score"))

        val rollHistory = mapRollHistoryProtocolBufferIntoRollHistory(rollHistoryProtocolBufferStateFlow.value)

        val exportedBuilder = RollHistoryProtocolBuffer.newBuilder()
        mapRollHistoryIntoRollHistoryProtocolBufferBuilder(
            rollHistory,
            exportedBuilder,
            useCache = false,
            isExport = true
        )

        return JsonFormat.printer().includingDefaultValueFields(fieldsToAlwaysOutput)
            .print(exportedBuilder.build())
    }

    override suspend fun jsonImport(json: String) {
        val diceBag = repositoryBag?.fetch()?.first()
        store(jsonImportProcess(json, diceBag))
    }

    override fun fetch(): Flow<RollHistory> = rollHistoryProtocolBufferStateFlow
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

    override suspend fun store(epoch: Long, rollList: List<Roll>) {
        Timber.d("repositoryRoll.STORE.INCREMENTAL ============================================")

        val rollHistoryProtocolBufferBuilder = rollHistoryProtocolBufferStateFlow.value.toBuilder()
        mapRollHistoryEntryIntoRollHistoryProtocolBufferBuilder(
            epoch,
            rollList,
            rollHistoryProtocolBufferBuilder
        )
        rollHistoryProtocolBufferStateFlow.value = rollHistoryProtocolBufferBuilder.build()
    }

    override suspend fun removeLatest() {
        Timber.d("repositoryRoll.REMOVE.LATEST ============================================")

        val rollHistoryProtocolBufferBuilder = rollHistoryProtocolBufferStateFlow.value.toBuilder()
        if (rollHistoryProtocolBufferBuilder.valuesCount > 0) {
            val latestEpoch = rollHistoryProtocolBufferBuilder.valuesMap.keys.maxOrNull()
            if (latestEpoch != null) {
                rollHistoryProtocolBufferBuilder.removeValues(latestEpoch)
            }
        }
        rollHistoryProtocolBufferStateFlow.value = rollHistoryProtocolBufferBuilder.build()
    }

    override suspend fun clear() {
        rollHistoryProtocolBufferStateFlow.value = RollHistoryProtocolBuffer.getDefaultInstance()
    }
}
