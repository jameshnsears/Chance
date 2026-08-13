package com.github.jameshnsears.chance.data.repo.impl.group.testdouble

import com.github.jameshnsears.chance.data.domain.core.group.GroupHistory
import com.github.jameshnsears.chance.data.domain.proto.GroupHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.GroupProtocolBuffer
import com.github.jameshnsears.chance.data.repo.impl.group.RepositoryGroupProtocolBufferInterface
import com.google.protobuf.Descriptors
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

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
        fieldsToAlwaysOutput.add(GroupProtocolBuffer.getDescriptor().findFieldByName("uuid"))
        fieldsToAlwaysOutput.add(GroupProtocolBuffer.getDescriptor().findFieldByName("name"))
        fieldsToAlwaysOutput.add(GroupProtocolBuffer.getDescriptor().findFieldByName("uuidDice"))
        fieldsToAlwaysOutput.add(GroupProtocolBuffer.getDescriptor().findFieldByName("notes"))
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
            groupHistory
        }

    override fun fetchSelected(): Flow<Set<String>> = groupHistoryProtocolBufferStateFlow
        .map { history ->
            history.groupList
                .filter { it.selected }
                .map { it.uuid }
                .toSet()
        }.distinctUntilChanged()

    override suspend fun store(newGroupHistory: GroupHistory) {
        updateStateFlow(newGroupHistory)
    }

    override suspend fun store(groupUuid: String, selected: Boolean) {
        val builder = groupHistoryProtocolBufferStateFlow.value.toBuilder()
        val groupIndex = builder.groupList.indexOfFirst { it.uuid == groupUuid }
        if (groupIndex != -1) {
            val groupBuilder = builder.getGroup(groupIndex).toBuilder()
            groupBuilder.selected = selected
            builder.setGroup(groupIndex, groupBuilder.build())
        }
        groupHistoryProtocolBufferStateFlow.value = builder.build()
    }

    override suspend fun clear() {
        groupHistoryProtocolBufferStateFlow.value = GroupHistoryProtocolBuffer.getDefaultInstance()
    }
}
