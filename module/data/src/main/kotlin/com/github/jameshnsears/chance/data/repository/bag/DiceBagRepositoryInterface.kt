package com.github.jameshnsears.chance.data.repository.bag

import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.DiceBag
import com.github.jameshnsears.chance.data.protocolbuffer.DiceBagProtocolBuffer
import com.github.jameshnsears.chance.data.protocolbuffer.DiceProtocolBuffer
import com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer
import com.github.jameshnsears.chance.data.repository.ImportExportRepositoryInterface

interface DiceBagRepositoryInterface : ImportExportRepositoryInterface {
    suspend fun fetch(): DiceBag
    suspend fun fetch(epoch: Long): Dice
    suspend fun store(newDiceBag: DiceBag)

    fun mapBagIntoBagProtocolBufferBuilder(
        diceBag: DiceBag,
        diceBagProtocolBufferBuilder: DiceBagProtocolBuffer.Builder,
    ) {
        for (dice in diceBag) {
            val diceProtocolBuffer = DiceProtocolBuffer.newBuilder()
            diceProtocolBuffer.setEpoch(dice.epoch)

            for (side in dice.sides) {
                val sideProtocolBuffer = SideProtocolBuffer.newBuilder()

                sideProtocolBuffer.setNumber(side.number)
                sideProtocolBuffer.setColour(side.colour)
                sideProtocolBuffer.setImageBase64(side.imageBase64)
                sideProtocolBuffer.setImageDrawableId(side.imageDrawableId)
                sideProtocolBuffer.setDescription(side.description)
                sideProtocolBuffer.setDescriptionStringsId(side.descriptionStringsId)
                sideProtocolBuffer.setDescriptionColour(side.descriptionColour)
                sideProtocolBuffer.build()

                diceProtocolBuffer.addSide(sideProtocolBuffer)
            }

            diceProtocolBuffer.setTitle(dice.title)
            diceProtocolBuffer.setTitleStringsId(dice.titleStringsId)
            diceProtocolBuffer.setColour(dice.colour)
            diceProtocolBuffer.setSelected(dice.selected)

            diceBagProtocolBufferBuilder
                .addDice(diceProtocolBuffer)
                .build()
        }
    }
}
