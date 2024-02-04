package com.github.jameshnsears.chance.data.repository.roll

import com.github.jameshnsears.chance.data.domain.RollHistory
import com.github.jameshnsears.chance.data.protocolbuffer.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer
import com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer
import com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer
import com.github.jameshnsears.chance.data.repository.ImportExportRepositoryInterface

interface RollRepositoryInterface : ImportExportRepositoryInterface {
    fun fetch(): RollHistory
    fun store(newRollHistory: RollHistory)

    fun mapBagIntoBagProtocolBufferBuilder(
        rollHistory: RollHistory,
        rollHistoryProtocolBufferBuilder: RollHistoryProtocolBuffer.Builder,
    ) {
        for ((keyEpoch, valueRolls) in rollHistory) {
            val rollListProtocolBuffer = RollListProtocolBuffer.newBuilder()

            for (roll in valueRolls) {
                val rollProtocolBuffer = RollProtocolBuffer.newBuilder()
                rollProtocolBuffer.setDiceEpoch(roll.diceEpoch)

                val sideProtocolBuffer = SideProtocolBuffer.newBuilder()
                sideProtocolBuffer.setNumber(roll.side.number)
                sideProtocolBuffer.setColour(roll.side.colour)
                sideProtocolBuffer.setImageBase64(roll.side.imageBase64)
                sideProtocolBuffer.setImageDrawableId(roll.side.imageDrawableId)
                sideProtocolBuffer.setDescription(roll.side.description)
                sideProtocolBuffer.setDescriptionStringsId(roll.side.descriptionStringsId)
                sideProtocolBuffer.setDescriptionColour(roll.side.descriptionColour)
                sideProtocolBuffer.build()

                rollProtocolBuffer.setSide(sideProtocolBuffer)

                rollListProtocolBuffer.addRoll(rollProtocolBuffer.build())
            }

            rollHistoryProtocolBufferBuilder.putValues(keyEpoch, rollListProtocolBuffer.build())
        }
    }
}
