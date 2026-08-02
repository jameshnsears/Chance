package com.github.jameshnsears.chance.data.repo.impl.bag

import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.proto.BagProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.DiceProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.impl.RepositoryProtocolBufferImageCache
import com.google.protobuf.util.JsonFormat
import timber.log.Timber
import java.util.UUID

interface RepositoryBagProtocolBufferInterface : RepositoryBagInterface, RepositoryProtocolBufferImageCache {

    private fun getOrUpdateCache(
        base64: String,
        bagProtocolBufferBuilder: BagProtocolBuffer.Builder,
        imageCache: MutableMap<String, String>
    ): String {
        if (base64.isEmpty() || base64.startsWith(RepositoryProtocolBufferImageCache.IMAGE_REF_PREFIX)) {
            return base64
        }

        val hash = sha256(base64)

        if (!imageCache.containsKey(hash)) {
            var cacheDiceIndex = -1
            for (i in 0 until bagProtocolBufferBuilder.diceCount) {
                if (bagProtocolBufferBuilder.getDice(i).epoch == RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE) {
                    cacheDiceIndex = i
                    break
                }
            }

            val cacheDiceBuilder = if (cacheDiceIndex != -1) {
                bagProtocolBufferBuilder.getDice(cacheDiceIndex).toBuilder()
            } else {
                DiceProtocolBuffer.newBuilder().setEpoch(RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE)
            }

            val cacheSide = SideProtocolBuffer.newBuilder()
                .setUuid(hash)
                .setImageBase64(base64)
                .build()
            cacheDiceBuilder.addSide(cacheSide)

            if (cacheDiceIndex != -1) {
                bagProtocolBufferBuilder.setDice(cacheDiceIndex, cacheDiceBuilder.build())
            } else {
                bagProtocolBufferBuilder.addDice(cacheDiceBuilder.build())
                cacheDiceIndex = bagProtocolBufferBuilder.diceCount - 1
            }

            imageCache[hash] = base64

            Timber.d("repositoryBag.CACHE.ADD: size=${bagProtocolBufferBuilder.getDice(cacheDiceIndex).sideCount}")
        }

        return "${RepositoryProtocolBufferImageCache.IMAGE_REF_PREFIX}$hash"
    }

    fun mapDiceBagIntoBagProtocolBufferBuilder(
        diceBag: DiceBag,
        bagProtocolBufferBuilder: BagProtocolBuffer.Builder,
        useCache: Boolean = true
    ) {
        bagProtocolBufferBuilder.clearDice()

        val imageCache = mutableMapOf<String, String>()
        if (useCache) {
            val existingCacheDice =
                bagProtocolBufferBuilder.diceList.find { it.epoch == RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE }
            existingCacheDice?.sideList?.forEach { imageCache[it.uuid] = it.imageBase64 }
        }

        // Create a lookup for imageBase64 referenced by UUID
        val sideUuidToImageBase64Map = diceBag.flatMap { it.sides }
            .filter { it.imageBase64.isNotEmpty() }
            .associate { it.uuid to it.imageBase64 }

        for (dice in diceBag) {
            val diceProtocolBuffer = DiceProtocolBuffer.newBuilder()
            diceProtocolBuffer.setEpoch(dice.epoch)
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

                if (useCache) {
                    sideProtocolBuffer.setImageBase64(
                        getOrUpdateCache(imageBase64ToUse, bagProtocolBufferBuilder, imageCache)
                    )
                } else {
                    sideProtocolBuffer.setImageBase64(imageBase64ToUse)
                }

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

        pruneCache(bagProtocolBufferBuilder)
    }

    fun pruneCache(bagProtocolBufferBuilder: BagProtocolBuffer.Builder) {
        val cacheDiceIndex = bagProtocolBufferBuilder.diceList.indexOfFirst {
            it.epoch == RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE
        }

        if (cacheDiceIndex == -1) return

        val existingCacheDice = bagProtocolBufferBuilder.getDice(cacheDiceIndex)
        Timber.d("repositoryBag.CACHE.PRUNE.start: size=${existingCacheDice.sideCount}")

        val referencedImageHashes = bagProtocolBufferBuilder.diceList
            .filter { it.epoch != RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE }
            .flatMap { it.sideList }
            .filter { it.imageBase64.startsWith(RepositoryProtocolBufferImageCache.IMAGE_REF_PREFIX) }
            .map { it.imageBase64.removePrefix(RepositoryProtocolBufferImageCache.IMAGE_REF_PREFIX) }
            .toSet()

        val prunedCacheDiceBuilder = DiceProtocolBuffer.newBuilder()
            .setEpoch(RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE)

        existingCacheDice.sideList.forEach { cacheSide ->
            if (referencedImageHashes.contains(cacheSide.uuid)) {
                prunedCacheDiceBuilder.addSide(cacheSide)
            }
        }

        if (prunedCacheDiceBuilder.sideCount == 0) {
            bagProtocolBufferBuilder.removeDice(cacheDiceIndex)
            Timber.d("repositoryBag.CACHE.PRUNE.end: size=0")
        } else {
            val updatedCache = prunedCacheDiceBuilder.build()
            bagProtocolBufferBuilder.setDice(cacheDiceIndex, updatedCache)
            Timber.d("repositoryBag.CACHE.PRUNE.end: size=${updatedCache.sideCount}")
        }
    }

    fun mapBagProtocolBufferIntoDiceBag(
        bagProtocolBuffer: BagProtocolBuffer,
    ): DiceBag {
        val diceBag = mutableListOf<Dice>()

        val cacheDice =
            bagProtocolBuffer.diceList.find { it.epoch == RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE }
        val imageCache = cacheDice?.sideList?.associate { it.uuid to it.imageBase64 } ?: emptyMap()

        bagProtocolBuffer.diceList.forEach { diceProtocolBuffer ->
            if (diceProtocolBuffer.epoch != RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE) {
                diceBag.add(mapDiceProtocolBufferIntoDice(diceProtocolBuffer, imageCache))
            }
        }
        return diceBag
    }

    fun mapDiceProtocolBufferIntoDice(
        diceProtocolBuffer: DiceProtocolBuffer,
        imageCache: Map<String, String> = emptyMap()
    ): Dice {
        return Dice(
            epoch = diceProtocolBuffer.epoch,
            uuid = if (diceProtocolBuffer.uuid.isNullOrEmpty()) UUID.randomUUID()
                .toString() else diceProtocolBuffer.uuid,
            sides = jsonImportProcessSides(diceProtocolBuffer, imageCache),
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

    fun jsonImportProcessSides(
        diceProtocolBuffer: DiceProtocolBuffer,
        imageCache: Map<String, String> = emptyMap()
    ): MutableList<Side> {
        val sides = mutableListOf<Side>()

        diceProtocolBuffer.sideList.forEach { sideProtocolBuffer ->
            val side = Side()
            side.uuid = sideProtocolBuffer.uuid
            side.number = sideProtocolBuffer.number
            side.numberColour = sideProtocolBuffer.numberColour
            side.imageDrawableId = sideProtocolBuffer.imageDrawableId

            var imageBase64 = sideProtocolBuffer.imageBase64
            if (imageBase64.startsWith(RepositoryProtocolBufferImageCache.IMAGE_REF_PREFIX)) {
                val hash = imageBase64.removePrefix(RepositoryProtocolBufferImageCache.IMAGE_REF_PREFIX)
                imageBase64 = imageCache[hash] ?: ""
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
            diceProtocolBuffer.setEpoch(dice.epoch)
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
