package com.github.jameshnsears.chance.data.repo.impl.roll

import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.RollListProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.google.protobuf.util.JsonFormat

interface RepositoryRollProtocolBufferInterface : RepositoryRollInterface {

    fun mapRollHistoryIntoRollHistoryProtocolBufferBuilder(
        rollHistory: RollHistory,
        rollHistoryProtocolBufferBuilder: RollHistoryProtocolBuffer.Builder,
        isExport: Boolean = false
    ) {
        // Clear existing values to avoid duplication if this is a full store
        rollHistoryProtocolBufferBuilder.clearValues()

        for ((keyEpoch, valueRolls) in rollHistory) {
            mapRollHistoryEntryIntoRollHistoryProtocolBufferBuilder(
                keyEpoch,
                valueRolls,
                rollHistoryProtocolBufferBuilder,
                isExport
            )
        }
    }

    fun mapRollHistoryEntryIntoRollHistoryProtocolBufferBuilder(
        keyEpoch: Long,
        valueRolls: List<Roll>,
        rollHistoryProtocolBufferBuilder: RollHistoryProtocolBuffer.Builder,
        isExport: Boolean = false
    ) {
        val rollListProtocolBuffer = RollListProtocolBuffer.newBuilder()

        for (roll in valueRolls) {
            val rollProtocolBuffer = RollProtocolBuffer.newBuilder()
            rollProtocolBuffer.setUuidDice(roll.uuidDice)

            val sideProtocolBuffer = SideProtocolBuffer.newBuilder()
            sideProtocolBuffer.setUuid(roll.side.uuid)

            if (isExport) {
                sideProtocolBuffer.setNumber(roll.side.number)
                sideProtocolBuffer.setNumberColour(roll.side.numberColour)
                sideProtocolBuffer.setImageDrawableId(roll.side.imageDrawableId)
                sideProtocolBuffer.setImageBase64(roll.side.imageBase64)
                sideProtocolBuffer.setDescription(roll.side.description)
                sideProtocolBuffer.setDescriptionColour(roll.side.descriptionColour)
            }

            rollProtocolBuffer.setSide(sideProtocolBuffer.build())

            rollProtocolBuffer.setMultiplierIndex(roll.multiplierIndex)
            rollProtocolBuffer.setExplodeIndex(roll.explodeIndex)
            rollProtocolBuffer.setScoreAdjustment(roll.scoreAdjustment)
            rollProtocolBuffer.setScore(roll.score)
            rollProtocolBuffer.setUuidGroup(roll.uuidGroup)

            rollListProtocolBuffer.addRoll(rollProtocolBuffer.build())
        }

        rollHistoryProtocolBufferBuilder.putValues(keyEpoch, rollListProtocolBuffer.build())
    }

    fun mapRollHistoryProtocolBufferIntoRollHistory(
        rollHistoryProtocolBuffer: RollHistoryProtocolBuffer,
        diceBag: DiceBag? = null,
        sideCache: Map<String, String> = emptyMap()
    ): RollHistory {
        val newRollHistory: RollHistory = LinkedHashMap()

        rollHistoryProtocolBuffer.valuesMap.forEach { mapEntry ->
            val rollList = mutableListOf<Roll>()
            mapEntry.value.rollList.forEach { rollProto ->

                var side = Side(
                    uuid = rollProto.side.uuid,
                    number = rollProto.side.number,
                    numberColour = rollProto.side.numberColour,
                    imageBase64 = rollProto.side.imageBase64,
                    imageDrawableId = rollProto.side.imageDrawableId,
                    description = rollProto.side.description,
                    descriptionColour = rollProto.side.descriptionColour,
                )

                if (diceBag != null && side.number == 0 && side.imageBase64 == "" && side.imageDrawableId == 0) {
                    val sideFromBag = diceBag.flatMap { it.sides }.find { it.uuid == side.uuid }
                    if (sideFromBag != null) {
                        side = sideFromBag.copy()
                    }
                }

                rollList.add(
                    Roll(
                        uuidDice = rollProto.uuidDice,
                        side = side,
                        multiplierIndex = rollProto.multiplierIndex,
                        explodeIndex = rollProto.explodeIndex,
                        scoreAdjustment = rollProto.scoreAdjustment,
                        score = rollProto.score,
                        uuidGroup = rollProto.uuidGroup,
                    ),
                )
            }

            newRollHistory[mapEntry.key] = rollList
        }

        return newRollHistory
    }

    fun jsonImportProcess(
        json: String,
        diceBag: DiceBag? = null,
        sideCache: Map<String, String> = emptyMap()
    ): RollHistory {
        val rollHistoryProtocolBufferBuilder: RollHistoryProtocolBuffer.Builder =
            RollHistoryProtocolBuffer.newBuilder()

        JsonFormat.parser().merge(json, rollHistoryProtocolBufferBuilder)

        return mapRollHistoryProtocolBufferIntoRollHistory(
            rollHistoryProtocolBufferBuilder.build(),
            diceBag,
            sideCache
        )
    }
}
