package com.github.jameshnsears.chance.data.repo.impl.bag

import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.proto.BagProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.DiceProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.impl.utility.svg.UtilitySvgSerializer
import com.google.protobuf.util.JsonFormat
import java.util.UUID

interface RepositoryBagProtocolBufferInterface : RepositoryBagInterface {

    fun mapDiceBagIntoBagProtocolBufferBuilder(
        diceBag: DiceBag,
        bagProtocolBufferBuilder: BagProtocolBuffer.Builder
    ) {
        bagProtocolBufferBuilder.clearDice()

        // Create a lookup for imageBase64 referenced by UUID
        val sideUuidToImageBase64Map = diceBag.flatMap { it.sides }
            .filter { it.imageBase64.isNotEmpty() }
            .associate { it.uuid to it.imageBase64 }

        for (dice in diceBag) {
            val diceProtocolBuffer = DiceProtocolBuffer.newBuilder()
            diceProtocolBuffer.setUuid(dice.uuid)

            dice.sides.forEach { side ->

                val sideProtocolBuffer = SideProtocolBuffer.newBuilder()

                sideProtocolBuffer
                    .setUuid(side.uuid)
                    .setNumber(side.number)
                    .setNumberColour(side.numberColour)
                    .setImageDrawableId(side.imageDrawableId)
                    .setImageBase64Uuid(side.imageBase64Uuid)

                var imageBase64ToUse = side.imageBase64
                if (imageBase64ToUse.isEmpty() && side.imageBase64Uuid.isNotEmpty()) {
                    imageBase64ToUse = sideUuidToImageBase64Map[side.imageBase64Uuid] ?: ""
                }

                sideProtocolBuffer.setImageBase64(imageBase64ToUse)

                sideProtocolBuffer
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
        sideCache: Map<String, String> = emptyMap()
    ): DiceBag {
        val diceBag = mutableListOf<Dice>()

        bagProtocolBuffer.diceList.forEach { diceProtocolBuffer ->
            diceBag.add(mapDiceProtocolBufferIntoDice(diceProtocolBuffer, sideCache))
        }
        return diceBag
    }

    fun mapDiceProtocolBufferIntoDice(
        diceProtocolBuffer: DiceProtocolBuffer,
        sideCache: Map<String, String> = emptyMap()
    ): Dice {
        return Dice(
            uuid = if (diceProtocolBuffer.uuid.isNullOrEmpty()) UUID.randomUUID()
                .toString() else diceProtocolBuffer.uuid,
            sides = jsonImportProcessSides(diceProtocolBuffer, sideCache),
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

    fun jsonImportProcess(json: String, sideCache: Map<String, String> = emptyMap()): DiceBag {
        val bagProtocolBufferBuilder: BagProtocolBuffer.Builder =
            BagProtocolBuffer.newBuilder()

        JsonFormat.parser().merge(json, bagProtocolBufferBuilder)

        return mapBagProtocolBufferIntoDiceBag(bagProtocolBufferBuilder.build(), sideCache)
    }

    fun jsonImportProcessSides(
        diceProtocolBuffer: DiceProtocolBuffer,
        sideCache: Map<String, String> = emptyMap()
    ): MutableList<Side> {
        val sides = mutableListOf<Side>()

        diceProtocolBuffer.sideList.forEach { sideProtocolBuffer ->
            val side = Side()
            side.uuid = sideProtocolBuffer.uuid
            side.number = sideProtocolBuffer.number
            side.numberColour = sideProtocolBuffer.numberColour
            side.imageDrawableId = sideProtocolBuffer.imageDrawableId

            var imageBase64 = sideProtocolBuffer.imageBase64
            if (imageBase64.isEmpty()) {
                // the SVG was moved into the side cache on store, so the proto holds no base64.
                // if it's an owner, use its UUID. if it's a ref, use imageBase64Uuid.
                val svgString = sideCache[sideProtocolBuffer.uuid]
                    ?: sideCache[sideProtocolBuffer.imageBase64Uuid]
                if (!svgString.isNullOrEmpty()) {
                    imageBase64 = UtilitySvgSerializer.encodeIntoBase64String(svgString)
                }
            }
            side.imageBase64 = imageBase64

            side.description = sideProtocolBuffer.description
            side.descriptionColour = sideProtocolBuffer.descriptionColour

            side.imageBase64Uuid = sideProtocolBuffer.imageBase64Uuid

            sides.add(side)
        }

        return sides
    }

    fun mapDiceBagIntoBagProtocolBufferBuilderForExport(
        diceBag: DiceBag,
        bagProtocolBufferBuilder: BagProtocolBuffer.Builder
    ) {
        bagProtocolBufferBuilder.clearDice()

        val imageToSideUuidMap = mutableMapOf<String, String>()

        for (dice in diceBag) {
            val diceProtocolBuffer = DiceProtocolBuffer.newBuilder()
            diceProtocolBuffer.setUuid(dice.uuid)

            dice.sides.forEach { side ->
                val sideProtocolBuffer = SideProtocolBuffer.newBuilder()
                sideProtocolBuffer
                    .setUuid(side.uuid)
                    .setNumber(side.number)
                    .setNumberColour(side.numberColour)
                    .setImageDrawableId(side.imageDrawableId)
                    .setDescription(side.description)
                    .setDescriptionColour(side.descriptionColour)

                val currentImageBase64 = side.imageBase64
                if (currentImageBase64.isNotEmpty()) {
                    if (!imageToSideUuidMap.containsKey(currentImageBase64)) {
                        imageToSideUuidMap[currentImageBase64] = side.uuid
                        sideProtocolBuffer.setImageBase64(currentImageBase64)
                        sideProtocolBuffer.setImageBase64Uuid("")
                    } else {
                        sideProtocolBuffer.setImageBase64("")
                        sideProtocolBuffer.setImageBase64Uuid(imageToSideUuidMap[currentImageBase64]!!)
                    }
                } else if (side.imageBase64Uuid.isNotEmpty()) {
                    sideProtocolBuffer.setImageBase64("")
                    sideProtocolBuffer.setImageBase64Uuid(side.imageBase64Uuid)
                }

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

            bagProtocolBufferBuilder.addDice(diceProtocolBuffer)
        }
    }

}
