package com.github.jameshnsears.chance.data.repo.impl.group

import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.jameshnsears.chance.data.domain.core.group.GroupHistory
import com.github.jameshnsears.chance.data.domain.proto.GroupHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.GroupProtocolBuffer
import com.github.jameshnsears.chance.data.repo.api.group.RepositoryGroupInterface
import com.google.protobuf.util.JsonFormat

interface RepositoryGroupProtocolBufferInterface : RepositoryGroupInterface {
    fun mapGroupHistoryIntoGroupHistoryProtocolBufferBuilder(
        groupHistory: GroupHistory,
        groupHistoryProtocolBufferBuilder: GroupHistoryProtocolBuffer.Builder,
    ) {
        for (group in groupHistory) {
            val groupProtocolBuffer = GroupProtocolBuffer.newBuilder()
            groupProtocolBuffer.setUuid(group.uuid)
            groupProtocolBuffer.setName(group.name)
            group.uuidDice.forEach { groupProtocolBuffer.addUuidDice(it) }
            groupProtocolBuffer.setNotes(group.notes)
            groupProtocolBuffer.setSelected(group.selected)
            groupProtocolBuffer.setDisplayIndex(group.displayIndex)

            groupHistoryProtocolBufferBuilder.addGroup(groupProtocolBuffer)
        }
    }

    fun mapGroupHistoryProtocolBufferIntoGroupHistory(
        groupHistoryProtocolBuffer: GroupHistoryProtocolBuffer,
    ): GroupHistory {
        return groupHistoryProtocolBuffer.groupList.map { groupProtocolBuffer ->
            Group(
                uuid = groupProtocolBuffer.uuid,
                name = groupProtocolBuffer.name,
                uuidDice = groupProtocolBuffer.uuidDiceList,
                notes = groupProtocolBuffer.notes,
                selected = groupProtocolBuffer.selected,
                displayIndex = groupProtocolBuffer.displayIndex,
            )
        }
    }

    fun jsonImportProcess(json: String): GroupHistory {
        val groupHistoryProtocolBufferBuilder = GroupHistoryProtocolBuffer.newBuilder()
        JsonFormat.parser().merge(json, groupHistoryProtocolBufferBuilder)

        return mapGroupHistoryProtocolBufferIntoGroupHistory(groupHistoryProtocolBufferBuilder.build())
    }
}
