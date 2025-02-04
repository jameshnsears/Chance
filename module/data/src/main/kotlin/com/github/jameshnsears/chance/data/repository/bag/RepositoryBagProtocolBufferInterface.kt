package com.github.jameshnsears.chance.data.repository.bag

import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.proto.BagProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.DiceProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer
import com.google.protobuf.util.JsonFormat

interface RepositoryBagProtocolBufferInterface : RepositoryBagInterface {
    fun mapDiceBagIntoBagProtocolBufferBuilder(
        diceBag: DiceBag,
        bagProtocolBufferBuilder: BagProtocolBuffer.Builder,
    ) {
        for (dice in diceBag) {
            val diceProtocolBuffer = DiceProtocolBuffer.newBuilder()
            diceProtocolBuffer.setEpoch(dice.epoch)

            dice.sides.forEachIndexed { index, side ->

                val sideProtocolBuffer = SideProtocolBuffer.newBuilder()

                sideProtocolBuffer
                    .setUuid(side.uuid)
                    .setNumber(side.number)
                    .setNumberColour(side.numberColour)
                    .setImageDrawableId(side.imageDrawableId)
                    .setImageBase64(side.imageBase64)
                    .setDescription(side.description)
                    .setDescriptionColour(side.descriptionColour)
                    .build()

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

            bagProtocolBufferBuilder
                .addDice(diceProtocolBuffer)
                .build()
        }
    }

    fun jsomImportProcess(json: String): MutableList<Dice> {
        val bagProtocolBufferBuilder: BagProtocolBuffer.Builder =
            BagProtocolBuffer.newBuilder()

        JsonFormat.parser().merge(json, bagProtocolBufferBuilder)

        val newDiceBag = mutableListOf<Dice>()

        bagProtocolBufferBuilder.diceList.forEach { diceProtocolBuffer ->
            val dice = Dice()
            dice.epoch = diceProtocolBuffer.epoch

            dice.uuid = diceProtocolBuffer.uuid

            dice.sides = jsomImportProcessSides(diceProtocolBuffer)
            dice.title = diceProtocolBuffer.title
            dice.colour = diceProtocolBuffer.colour
            dice.selected = diceProtocolBuffer.selected

            dice.multiplierValue = diceProtocolBuffer.multiplierValue

            dice.explode = diceProtocolBuffer.explode
            dice.explodeWhen = diceProtocolBuffer.explodeWhen
            dice.explodeValue = diceProtocolBuffer.explodeValue

            dice.modifyScore = diceProtocolBuffer.modifyScore
            dice.modifyScoreValue = diceProtocolBuffer.modifyScoreValue

            newDiceBag.add(dice)
        }
        return newDiceBag
    }

    fun jsomImportProcessSides(diceProtocolBuffer: DiceProtocolBuffer): MutableList<Side> {
        val sides = mutableListOf<Side>()

        diceProtocolBuffer.sideList.forEach { sideProtocolBuffer ->
            val side = Side()
            side.uuid = sideProtocolBuffer.uuid
            side.number = sideProtocolBuffer.number
            side.numberColour = sideProtocolBuffer.numberColour
            side.imageDrawableId = sideProtocolBuffer.imageDrawableId
            side.imageBase64 = sideProtocolBuffer.imageBase64
            side.description = sideProtocolBuffer.description
            side.descriptionColour = sideProtocolBuffer.descriptionColour

            sides.add(side)
        }

        return sides
    }
}
