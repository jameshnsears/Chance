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
import com.github.jameshnsears.chance.data.repo.impl.RepositoryProtocolBufferImageCache
import com.google.protobuf.util.JsonFormat
import timber.log.Timber

interface RepositoryRollProtocolBufferInterface : RepositoryRollInterface, RepositoryProtocolBufferImageCache {

    fun mapRollHistoryIntoRollHistoryProtocolBufferBuilder(
        rollHistory: RollHistory,
        rollHistoryProtocolBufferBuilder: RollHistoryProtocolBuffer.Builder,
        useCache: Boolean = true,
        isExport: Boolean = false
    ) {
        // Clear existing values to avoid duplication if this is a full store
        rollHistoryProtocolBufferBuilder.clearValues()

        val imageCache = mutableMapOf<String, String>()
        if (useCache) {
            val existingCacheRollList = rollHistoryProtocolBufferBuilder.getValuesOrDefault(
                RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE,
                RollListProtocolBuffer.getDefaultInstance()
            )
            existingCacheRollList.rollList.forEach { imageCache[it.side.uuid] = it.side.imageBase64 }
        }

        for ((keyEpoch, valueRolls) in rollHistory) {
            mapRollHistoryEntryIntoRollHistoryProtocolBufferBuilder(
                keyEpoch,
                valueRolls,
                rollHistoryProtocolBufferBuilder,
                imageCache,
                useCache,
                isExport
            )
        }
    }

    fun mapRollHistoryEntryIntoRollHistoryProtocolBufferBuilder(
        keyEpoch: Long,
        valueRolls: List<Roll>,
        rollHistoryProtocolBufferBuilder: RollHistoryProtocolBuffer.Builder,
        imageCache: MutableMap<String, String> = mutableMapOf(),
        useCache: Boolean = true,
        isExport: Boolean = false
    ) {
        val rollListProtocolBuffer = RollListProtocolBuffer.newBuilder()

        for (roll in valueRolls) {
            val rollProtocolBuffer = RollProtocolBuffer.newBuilder()
            rollProtocolBuffer.setUuidDice(roll.uuidDice)

            val sideProtocolBuffer = SideProtocolBuffer.newBuilder()
            sideProtocolBuffer.setUuid(roll.side.uuid)

            if (!isExport) {
                if (useCache) {
                    // Check if side is already in cache
                    val cacheRollList = rollHistoryProtocolBufferBuilder.getValuesOrDefault(
                        RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE,
                        RollListProtocolBuffer.getDefaultInstance()
                    )

                    val sideInCache = cacheRollList.rollList.any { it.side.uuid == roll.side.uuid }

                    if (!sideInCache) {
                        val cacheRollListBuilder = cacheRollList.toBuilder()
                        val sideToCache = SideProtocolBuffer.newBuilder()
                            .setUuid(roll.side.uuid)
                            .setNumber(roll.side.number)
                            .setNumberColour(roll.side.numberColour)
                            .setImageDrawableId(roll.side.imageDrawableId)
                            .setImageBase64(roll.side.imageBase64)
                            .setDescription(roll.side.description)
                            .setDescriptionColour(roll.side.descriptionColour)
                            .build()

                        cacheRollListBuilder.addRoll(RollProtocolBuffer.newBuilder().setSide(sideToCache).build())
                        val updatedCache = cacheRollListBuilder.build()
                        rollHistoryProtocolBufferBuilder.putValues(
                            RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE,
                            updatedCache
                        )

                        Timber.d("repositoryRoll.CACHE.ADD: size=${updatedCache.rollCount}")
                    }
                } else {
                    sideProtocolBuffer.setNumber(roll.side.number)
                    sideProtocolBuffer.setNumberColour(roll.side.numberColour)
                    sideProtocolBuffer.setImageDrawableId(roll.side.imageDrawableId)
                    sideProtocolBuffer.setImageBase64(roll.side.imageBase64)
                    sideProtocolBuffer.setDescription(roll.side.description)
                    sideProtocolBuffer.setDescriptionColour(roll.side.descriptionColour)
                }
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
        diceBag: DiceBag? = null
    ): RollHistory {
        val newRollHistory: RollHistory = LinkedHashMap()

        // Extract cache: Map<UUID, SideProtocolBuffer>
        val sideCache = rollHistoryProtocolBuffer.getValuesOrDefault(
            RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE,
            RollListProtocolBuffer.getDefaultInstance()
        ).rollList.associate { it.side.uuid to it.side }

        rollHistoryProtocolBuffer.valuesMap.forEach { mapEntry ->
            if (mapEntry.key == RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE) return@forEach

            val rollList = mutableListOf<Roll>()
            mapEntry.value.rollList.forEach { rollProto ->
                val cachedSide = sideCache[rollProto.side.uuid]

                var side = Side(
                    uuid = rollProto.side.uuid,
                    number = cachedSide?.number ?: rollProto.side.number,
                    numberColour = cachedSide?.numberColour ?: rollProto.side.numberColour,
                    imageBase64 = cachedSide?.imageBase64 ?: rollProto.side.imageBase64,
                    imageDrawableId = cachedSide?.imageDrawableId ?: rollProto.side.imageDrawableId,
                    description = cachedSide?.description ?: rollProto.side.description,
                    descriptionColour = cachedSide?.descriptionColour ?: rollProto.side.descriptionColour,
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

    fun pruneCache(rollHistoryProtocolBufferBuilder: RollHistoryProtocolBuffer.Builder) {
        if (!rollHistoryProtocolBufferBuilder.containsValues(RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE)) {
            return
        }

        val cacheRollList =
            rollHistoryProtocolBufferBuilder.getValuesOrThrow(RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE)
        Timber.d("repositoryRoll.CACHE.PRUNE.start: size=${cacheRollList.rollCount}")

        val referencedSideUuids = mutableSetOf<String>()
        rollHistoryProtocolBufferBuilder.valuesMap.forEach { (epoch, rollList) ->
            if (epoch != RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE) {
                rollList.rollList.forEach { roll ->
                    referencedSideUuids.add(roll.side.uuid)
                }
            }
        }

        val prunedCacheRollListBuilder = RollListProtocolBuffer.newBuilder()
        cacheRollList.rollList.forEach { cacheRoll ->
            if (referencedSideUuids.contains(cacheRoll.side.uuid)) {
                prunedCacheRollListBuilder.addRoll(cacheRoll)
            }
        }

        if (prunedCacheRollListBuilder.rollCount == 0) {
            rollHistoryProtocolBufferBuilder.removeValues(RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE)
            Timber.d("repositoryRoll.CACHE.PRUNE.end: size=0")
        } else {
            val updatedCache = prunedCacheRollListBuilder.build()
            rollHistoryProtocolBufferBuilder.putValues(
                RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE,
                updatedCache
            )
            Timber.d("repositoryRoll.CACHE.PRUNE.end: size=${updatedCache.rollCount}")
        }
    }

    fun jsonImportProcess(json: String, diceBag: DiceBag? = null): RollHistory {
        val rollHistoryProtocolBufferBuilder: RollHistoryProtocolBuffer.Builder =
            RollHistoryProtocolBuffer.newBuilder()

        JsonFormat.parser().merge(json, rollHistoryProtocolBufferBuilder)

        return mapRollHistoryProtocolBufferIntoRollHistory(
            rollHistoryProtocolBufferBuilder.build(),
            diceBag
        )
    }
}
