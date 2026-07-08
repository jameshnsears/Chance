package com.github.jameshnsears.chance.data.repo.impl.bag

import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.proto.BagProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.DiceProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.google.protobuf.util.JsonFormat
import java.util.UUID

interface RepositoryBagProtocolBufferInterface : RepositoryBagInterface {
    fun mapDiceBagIntoBagProtocolBufferBuilder(
        diceBag: DiceBag,
        bagProtocolBufferBuilder: BagProtocolBuffer.Builder,
    ) {
        for (dice in diceBag) {
            val diceProtocolBuffer = DiceProtocolBuffer.newBuilder()
            diceProtocolBuffer.setEpoch(dice.epoch)
            diceProtocolBuffer.setUuid(dice.uuid)

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

            diceProtocolBuffer.setDisplayIndex(dice.displayIndex)

            bagProtocolBufferBuilder
                .addDice(diceProtocolBuffer)
        }
    }

    fun mapBagProtocolBufferIntoDiceBag(
        bagProtocolBuffer: BagProtocolBuffer,
    ): DiceBag {
        val diceBag = mutableListOf<Dice>()
        bagProtocolBuffer.diceList.forEach { diceProtocolBuffer ->
            diceBag.add(mapDiceProtocolBufferIntoDice(diceProtocolBuffer))
        }
        return diceBag
    }

    fun mapDiceProtocolBufferIntoDice(
        diceProtocolBuffer: DiceProtocolBuffer,
    ): Dice {
        return Dice(
            epoch = diceProtocolBuffer.epoch,
            uuid = if (diceProtocolBuffer.uuid.isNullOrEmpty()) UUID.randomUUID()
                .toString() else diceProtocolBuffer.uuid,
            sides = jsonImportProcessSides(diceProtocolBuffer),
            title = diceProtocolBuffer.title,
            colour = diceProtocolBuffer.colour,
            selected = diceProtocolBuffer.selected,
            multiplierValue = diceProtocolBuffer.multiplierValue,
            explode = diceProtocolBuffer.explode,
            explodeWhen = diceProtocolBuffer.explodeWhen,
            explodeValue = diceProtocolBuffer.explodeValue,
            modifyScore = diceProtocolBuffer.modifyScore,
            modifyScoreValue = diceProtocolBuffer.modifyScoreValue,
            displayIndex = diceProtocolBuffer.displayIndex,
        )
    }

    fun jsonImportProcess(json: String): DiceBag {
        val bagProtocolBufferBuilder: BagProtocolBuffer.Builder =
            BagProtocolBuffer.newBuilder()

        JsonFormat.parser().merge(json, bagProtocolBufferBuilder)

        return mapBagProtocolBufferIntoDiceBag(bagProtocolBufferBuilder.build())
    }

    fun jsonImportProcessSides(diceProtocolBuffer: DiceProtocolBuffer): MutableList<Side> {
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
