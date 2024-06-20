package com.github.jameshnsears.chance.data.repository.bag

import com.github.jameshnsears.chance.data.domain.proto.DiceBagProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.DiceProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer
import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.domain.state.DiceBag
import com.github.jameshnsears.chance.data.repository.RepositoryImportExportInterface
import kotlinx.coroutines.flow.Flow

interface RepositoryBagInterface : RepositoryImportExportInterface {
    suspend fun fetch(): Flow<DiceBag>
    suspend fun fetch(epoch: Long): Flow<Dice>
    suspend fun store(newDiceBag: DiceBag)

    fun mapDiceBagIntoDiceBagProtocolBufferBuilder(
        diceBag: DiceBag,
        diceBagProtocolBufferBuilder: DiceBagProtocolBuffer.Builder,
    ) {
        for (dice in diceBag) {
            val diceProtocolBuffer = DiceProtocolBuffer.newBuilder()
            diceProtocolBuffer.setEpoch(dice.epoch)

            for (side in dice.sides) {
                val sideProtocolBuffer = SideProtocolBuffer.newBuilder()

                sideProtocolBuffer.setNumber(side.number)
                sideProtocolBuffer.setNumberColour(side.numberColour)
                sideProtocolBuffer.setImageBase64(side.imageBase64)
                sideProtocolBuffer.setImageDrawableId(side.imageDrawableId)
                sideProtocolBuffer.setDescription(side.description)
                sideProtocolBuffer.setDescriptionColour(side.descriptionColour)
                sideProtocolBuffer.build()

                diceProtocolBuffer.addSide(sideProtocolBuffer)
            }

            diceProtocolBuffer.setTitle(dice.title)
            diceProtocolBuffer.setColour(dice.colour)
            diceProtocolBuffer.setSelected(dice.selected)

            diceProtocolBuffer.setMultiplierValue(dice.multiplierValue)

            diceProtocolBuffer.setExplode(dice.explode)
            diceProtocolBuffer.setExplodeWhen(dice.explodeWhen)
            diceProtocolBuffer.setExplodeValue(dice.explodeValue)

            diceProtocolBuffer.setModifyScore(dice.modifyScore)
            diceProtocolBuffer.setModifyScoreValue(dice.modifyScoreValue)

            diceBagProtocolBufferBuilder
                .addDice(diceProtocolBuffer)
                .build()
        }
    }
}
