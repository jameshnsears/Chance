package com.github.jameshnsears.chance.data.repo.impl.roll

import com.github.jameshnsears.chance.data.domain.core.Side
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
    ) {
        for ((keyEpoch, valueRolls) in rollHistory) {
            val rollListProtocolBuffer = RollListProtocolBuffer.newBuilder()

            for (roll in valueRolls) {
                val rollProtocolBuffer = RollProtocolBuffer.newBuilder()
                rollProtocolBuffer.setUuidDice(roll.uuidDice)

                val sideProtocolBuffer = SideProtocolBuffer.newBuilder()
                sideProtocolBuffer.setUuid(roll.side.uuid)
                sideProtocolBuffer.setNumber(roll.side.number)
                sideProtocolBuffer.setNumberColour(roll.side.numberColour)
                sideProtocolBuffer.setImageDrawableId(roll.side.imageDrawableId)
                sideProtocolBuffer.setImageBase64(roll.side.imageBase64)
                sideProtocolBuffer.setDescription(roll.side.description)
                sideProtocolBuffer.setDescriptionColour(roll.side.descriptionColour)
                sideProtocolBuffer.build()
                rollProtocolBuffer.setSide(sideProtocolBuffer)

                rollProtocolBuffer.setMultiplierIndex(roll.multiplierIndex)
                rollProtocolBuffer.setExplodeIndex(roll.explodeIndex)
                rollProtocolBuffer.setScoreAdjustment(roll.scoreAdjustment)
                rollProtocolBuffer.setScore(roll.score)
                rollProtocolBuffer.setUuidGroup(roll.uuidGroup)

                rollListProtocolBuffer.addRoll(rollProtocolBuffer.build())
            }

            rollHistoryProtocolBufferBuilder.putValues(keyEpoch, rollListProtocolBuffer.build())
        }
    }

    fun mapRollHistoryProtocolBufferIntoRollHistory(
        rollHistoryProtocolBuffer: RollHistoryProtocolBuffer,
    ): RollHistory {
        val newRollHistory: RollHistory = LinkedHashMap()

        rollHistoryProtocolBuffer.valuesMap.forEach { mapEntry ->
            val rollList = mutableListOf<Roll>()
            mapEntry.value.rollList.forEach {
                rollList.add(
                    Roll(
                        uuidDice = it.uuidDice,
                        side = Side(
                            uuid = it.side.uuid,
                            number = it.side.number,
                            numberColour = it.side.numberColour,
                            imageBase64 = it.side.imageBase64,
                            imageDrawableId = it.side.imageDrawableId,
                            description = it.side.description,
                            descriptionColour = it.side.descriptionColour,
                        ),
                        multiplierIndex = it.multiplierIndex,
                        explodeIndex = it.explodeIndex,
                        scoreAdjustment = it.scoreAdjustment,
                        score = it.score,
                        uuidGroup = it.uuidGroup,
                    ),
                )
            }

            newRollHistory[mapEntry.key] = rollList
        }

        return newRollHistory
    }

    fun jsonImportProcess(json: String): RollHistory {
        val rollHistoryProtocolBufferBuilder: RollHistoryProtocolBuffer.Builder =
            RollHistoryProtocolBuffer.newBuilder()

        JsonFormat.parser().merge(json, rollHistoryProtocolBufferBuilder)

        return mapRollHistoryProtocolBufferIntoRollHistory(rollHistoryProtocolBufferBuilder.build())
    }
}
