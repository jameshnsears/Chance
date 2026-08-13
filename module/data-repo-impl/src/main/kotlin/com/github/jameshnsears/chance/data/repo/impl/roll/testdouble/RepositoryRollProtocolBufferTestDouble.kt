package com.github.jameshnsears.chance.data.repo.impl.roll.testdouble

import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.side.RepositorySideCacheInterface
import com.github.jameshnsears.chance.data.repo.impl.roll.RepositoryRollProtocolBufferInterface
import com.google.protobuf.Descriptors
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber

class RepositoryRollProtocolBufferTestDouble private constructor(
    private val repositoryBag: RepositoryBagInterface? = null,
    private val repositorySideCache: RepositorySideCacheInterface? = null
) :
    RepositoryRollProtocolBufferInterface {
    private val rollHistoryProtocolBufferStateFlow =
        MutableStateFlow(RollHistoryProtocolBuffer.getDefaultInstance())

    private var initialized = false

    companion object {
        private var instance: RepositoryRollProtocolBufferTestDouble? = null

        fun getInstance(
            rollHistory: RollHistory = LinkedHashMap(),
            repositoryBag: RepositoryBagInterface? = null,
            repositorySideCache: RepositorySideCacheInterface? = null
        ): RepositoryRollProtocolBufferTestDouble {
            if (instance == null) {
                instance = RepositoryRollProtocolBufferTestDouble(repositoryBag, repositorySideCache)
            }

            if (!instance!!.initialized && rollHistory.isNotEmpty()) {
                instance!!.updateStateFlow(rollHistory)
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
        fieldsToAlwaysOutput.add(RollProtocolBuffer.getDescriptor().findFieldByName("uuidDice"))
        fieldsToAlwaysOutput.add(RollProtocolBuffer.getDescriptor().findFieldByName("uuidGroup"))
        fieldsToAlwaysOutput.add(RollProtocolBuffer.getDescriptor().findFieldByName("side"))
        fieldsToAlwaysOutput.add(RollProtocolBuffer.getDescriptor().findFieldByName("multiplierIndex"))
        fieldsToAlwaysOutput.add(RollProtocolBuffer.getDescriptor().findFieldByName("explodeIndex"))
        fieldsToAlwaysOutput.add(RollProtocolBuffer.getDescriptor().findFieldByName("scoreAdjustment"))
        fieldsToAlwaysOutput.add(RollProtocolBuffer.getDescriptor().findFieldByName("score"))

        fieldsToAlwaysOutput.add(SideProtocolBuffer.getDescriptor().findFieldByName("uuid"))

        val rollHistory = mapRollHistoryProtocolBufferIntoRollHistory(rollHistoryProtocolBufferStateFlow.value)

        val exportedBuilder = RollHistoryProtocolBuffer.newBuilder()
        mapRollHistoryIntoRollHistoryProtocolBufferBuilder(
            rollHistory,
            exportedBuilder,
            isExport = true
        )

        return JsonFormat.printer().includingDefaultValueFields(fieldsToAlwaysOutput)
            .print(exportedBuilder.build())
    }

    override suspend fun jsonImport(json: String) {
        val diceBag = repositoryBag?.fetch()?.first()
        store(jsonImportProcess(json, diceBag))
    }

    override fun fetch(): Flow<RollHistory> {
        val baseFlow = if (repositoryBag != null) {
            rollHistoryProtocolBufferStateFlow.combine(repositoryBag.fetch()) { rollHistoryProtocolBuffer, diceBag ->
                rollHistoryProtocolBuffer to diceBag
            }
        } else {
            rollHistoryProtocolBufferStateFlow.map { rollHistoryProtocolBuffer ->
                rollHistoryProtocolBuffer to null as DiceBag?
            }
        }

        return if (repositorySideCache != null) {
            baseFlow.combine(repositorySideCache.fetch()) { (rollHistoryProtocolBuffer, diceBag), sideCache ->
                mapRollHistoryProtocolBufferIntoRollHistory(
                    rollHistoryProtocolBuffer,
                    diceBag,
                    sideCache
                )
            }
        } else {
            baseFlow.map { (rollHistoryProtocolBuffer, diceBag) ->
                mapRollHistoryProtocolBufferIntoRollHistory(
                    rollHistoryProtocolBuffer,
                    diceBag
                )
            }
        }
    }

    override suspend fun store(newRollHistory: RollHistory) {
        Timber.d("repositoryRoll.STORE ============================================")
        Timber.d("repositoryRoll.size=${newRollHistory.size}")

        if (repositorySideCache != null) {
            newRollHistory.values.forEach { rolls ->
                rolls.forEach { roll ->
                    repositorySideCache.store(roll.side.uuid, roll.side.imageBase64)
                }
            }
        }

        updateStateFlow(newRollHistory)
    }

    override suspend fun fetchReferencedSideUuids(): Set<String> {
        return rollHistoryProtocolBufferStateFlow.value.valuesMap.values
            .flatMap { it.rollList }
            .map { it.side.uuid }
            .toSet()
    }

    override suspend fun store(epoch: Long, rollList: List<Roll>) {
        Timber.d("repositoryRoll.STORE.ENTRY =====================================")

        if (repositorySideCache != null) {
            rollList.forEach { roll ->
                repositorySideCache.store(roll.side.uuid, roll.side.imageBase64)
            }
        }

        val builder = rollHistoryProtocolBufferStateFlow.value.toBuilder()
        mapRollHistoryEntryIntoRollHistoryProtocolBufferBuilder(
            epoch,
            rollList,
            builder
        )
        rollHistoryProtocolBufferStateFlow.value = builder.build()
    }

    override suspend fun delete(diceUuid: String) {
        Timber.d("repositoryRoll.DELETE ============================================")
        Timber.d("repositoryRoll.delete.diceUuid=$diceUuid")

        val builder = rollHistoryProtocolBufferStateFlow.value.toBuilder()

        val keysToRemove = mutableListOf<Long>()
        builder.valuesMap.forEach { (epoch, rollList) ->
            if (epoch != -1L) {
                if (rollList.rollList.any { it.uuidDice == diceUuid }) {
                    keysToRemove.add(epoch)
                }
            }
        }

        keysToRemove.forEach {
            builder.removeValues(it)
        }

        rollHistoryProtocolBufferStateFlow.value = builder.build()
    }

    override suspend fun removeLatest() {
        Timber.d("repositoryRoll.REMOVE.LATEST ============================================")

        val builder = rollHistoryProtocolBufferStateFlow.value.toBuilder()
        if (builder.valuesCount > 0) {
            val latestEpoch = builder.valuesMap.keys
                .filter { it != -1L }
                .maxOrNull()
            if (latestEpoch != null) {
                builder.removeValues(latestEpoch)
            }
        }
        rollHistoryProtocolBufferStateFlow.value = builder.build()
    }

    override suspend fun clear() {
        rollHistoryProtocolBufferStateFlow.value = RollHistoryProtocolBuffer.getDefaultInstance()
    }
}
