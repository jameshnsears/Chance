package com.github.jameshnsears.chance.data.repo.impl.group.testdouble

import com.github.jameshnsears.chance.data.domain.core.group.GroupHistory
import com.github.jameshnsears.chance.data.domain.proto.GroupHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.GroupProtocolBuffer
import com.github.jameshnsears.chance.data.repo.impl.group.RepositoryGroupProtocolBufferInterface
import com.google.protobuf.Descriptors
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class RepositoryGroupProtocolBufferTestDouble private constructor() :
    RepositoryGroupProtocolBufferInterface {
    private val groupHistoryProtocolBufferStateFlow =
        MutableStateFlow(GroupHistoryProtocolBuffer.getDefaultInstance())

    private var initialized = false

    companion object {
        private var instance: RepositoryGroupProtocolBufferTestDouble? = null

        fun getInstance(
            groupHistory: GroupHistory,
        ): RepositoryGroupProtocolBufferTestDouble {
            if (instance == null) {
                instance = RepositoryGroupProtocolBufferTestDouble()
            }

            if (!instance!!.initialized) {
                instance!!.updateStateFlow(groupHistory)
                instance!!.traceUuid(groupHistory)
                instance!!.initialized = true
            }
            return instance!!
        }
    }

    private fun updateStateFlow(groupHistory: GroupHistory) {
        val groupHistoryProtocolBufferBuilder = GroupHistoryProtocolBuffer.newBuilder()
        mapGroupHistoryIntoGroupHistoryProtocolBufferBuilder(
            groupHistory,
            groupHistoryProtocolBufferBuilder
        )
        groupHistoryProtocolBufferStateFlow.value = groupHistoryProtocolBufferBuilder.build()
    }

    override suspend fun jsonExport(): String {
        val fieldsToAlwaysOutput: MutableSet<Descriptors.FieldDescriptor> = HashSet()
        fieldsToAlwaysOutput.add(GroupProtocolBuffer.getDescriptor().findFieldByName("displayIndex"))
        fieldsToAlwaysOutput.add(GroupProtocolBuffer.getDescriptor().findFieldByName("selected"))

        return JsonFormat.printer().includingDefaultValueFields(fieldsToAlwaysOutput)
            .print(groupHistoryProtocolBufferStateFlow.value)
    }

    override suspend fun jsonImport(json: String) {
        store(jsonImportProcess(json))
    }

    override fun fetch(): Flow<GroupHistory> = groupHistoryProtocolBufferStateFlow
        .map { groupHistoryProtocolBuffer ->
            val groupHistory = mapGroupHistoryProtocolBufferIntoGroupHistory(groupHistoryProtocolBuffer)

            Timber.d("repositoryGroup.FETCH ============================================")
            Timber.d("repositoryGroup.size=${groupHistory.size}")

            groupHistory
        }

    override suspend fun store(newGroupHistory: GroupHistory) {
        Timber.d("repositoryGroup.STORE ============================================")
        Timber.d("repositoryGroup.size=${newGroupHistory.size}")

        updateStateFlow(newGroupHistory)
    }

    override suspend fun clear() {
        groupHistoryProtocolBufferStateFlow.value = GroupHistoryProtocolBuffer.getDefaultInstance()
    }
}
