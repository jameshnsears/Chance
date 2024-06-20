package com.github.jameshnsears.chance.data.repository.roll

import com.github.jameshnsears.chance.data.domain.proto.RollHistoryProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.RollListProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer
import com.github.jameshnsears.chance.data.domain.state.RollHistory
import com.github.jameshnsears.chance.data.repository.RepositoryImportExportInterface
import kotlinx.coroutines.flow.Flow

interface RepositoryRollInterface : RepositoryImportExportInterface {
    suspend fun fetch(): Flow<RollHistory>
    suspend fun store(newRollHistory: RollHistory)

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

                rollListProtocolBuffer.addRoll(rollProtocolBuffer.build())
            }

            rollHistoryProtocolBufferBuilder.putValues(keyEpoch, rollListProtocolBuffer.build())
        }
    }
}
