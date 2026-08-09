package com.github.jameshnsears.chance.data.repo.impl.bag.testdouble

import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.proto.BagProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.DiceProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer
import com.github.jameshnsears.chance.data.repo.impl.RepositoryProtocolBufferImageCache
import com.github.jameshnsears.chance.data.repo.impl.bag.RepositoryBagProtocolBufferInterface
import com.google.protobuf.Descriptors
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class RepositoryBagProtocolBufferTestDouble private constructor() :
    RepositoryBagProtocolBufferInterface {
    private val diceBagProtocolBufferStateFlow =
        MutableStateFlow(BagProtocolBuffer.getDefaultInstance())

    private var initialized = false

    companion object {
        private var instance: RepositoryBagProtocolBufferTestDouble? = null

        fun getInstance(
            diceBag: DiceBag = mutableListOf(),
        ): RepositoryBagProtocolBufferTestDouble {
            if (instance == null) {
                instance = RepositoryBagProtocolBufferTestDouble()
            }

            if (!instance!!.initialized && diceBag.isNotEmpty()) {
                instance!!.updateStateFlow(diceBag)
                instance!!.traceUuid(diceBag)
                instance!!.initialized = true
            }

            return instance!!
        }
    }

    private fun updateStateFlow(diceBag: DiceBag) {
        val bagProtocolBufferBuilder = BagProtocolBuffer.newBuilder()
        mapDiceBagIntoBagProtocolBufferBuilder(diceBag, bagProtocolBufferBuilder)
        diceBagProtocolBufferStateFlow.value = bagProtocolBufferBuilder.build()
    }

    override suspend fun jsonExport(): String {
        val fieldsToAlwaysOutput: MutableSet<Descriptors.FieldDescriptor> = HashSet()
        fieldsToAlwaysOutput.add(DiceProtocolBuffer.getDescriptor().findFieldByName("title"))
        fieldsToAlwaysOutput.add(DiceProtocolBuffer.getDescriptor().findFieldByName("colour"))
        fieldsToAlwaysOutput.add(DiceProtocolBuffer.getDescriptor().findFieldByName("selected"))
        fieldsToAlwaysOutput.add(DiceProtocolBuffer.getDescriptor().findFieldByName("multiplierValue"))
        fieldsToAlwaysOutput.add(DiceProtocolBuffer.getDescriptor().findFieldByName("explode"))
        fieldsToAlwaysOutput.add(DiceProtocolBuffer.getDescriptor().findFieldByName("explodeWhen"))
        fieldsToAlwaysOutput.add(DiceProtocolBuffer.getDescriptor().findFieldByName("explodeValue"))
        fieldsToAlwaysOutput.add(DiceProtocolBuffer.getDescriptor().findFieldByName("modifyScore"))
        fieldsToAlwaysOutput.add(DiceProtocolBuffer.getDescriptor().findFieldByName("modifyScoreValue"))
        fieldsToAlwaysOutput.add(DiceProtocolBuffer.getDescriptor().findFieldByName("displayIndex"))

        fieldsToAlwaysOutput.add(SideProtocolBuffer.getDescriptor().findFieldByName("number"))
        fieldsToAlwaysOutput.add(SideProtocolBuffer.getDescriptor().findFieldByName("numberColour"))
        fieldsToAlwaysOutput.add(SideProtocolBuffer.getDescriptor().findFieldByName("imageDrawableId"))
        fieldsToAlwaysOutput.add(SideProtocolBuffer.getDescriptor().findFieldByName("imageBase64"))
        fieldsToAlwaysOutput.add(SideProtocolBuffer.getDescriptor().findFieldByName("description"))
        fieldsToAlwaysOutput.add(SideProtocolBuffer.getDescriptor().findFieldByName("descriptionColour"))

        val bagProtocolBuffer = diceBagProtocolBufferStateFlow.value
        val diceBag = mapBagProtocolBufferIntoDiceBag(bagProtocolBuffer)

        val exportedBuilder = BagProtocolBuffer.newBuilder()
        mapDiceBagIntoBagProtocolBufferBuilderForExport(
            diceBag,
            exportedBuilder
        )

        return JsonFormat.printer().includingDefaultValueFields(fieldsToAlwaysOutput)
            .print(exportedBuilder.build())
    }

    override suspend fun jsonImport(json: String) {
        store(jsonImportProcess(json))
    }

    override fun fetch(): Flow<DiceBag> = diceBagProtocolBufferStateFlow
        .map { bagProtocolBuffer ->
            val diceBag = mapBagProtocolBufferIntoDiceBag(bagProtocolBuffer)

            Timber.d("repositoryBag.FETCH ============================================")
            Timber.d("repositoryBag.size=${diceBag.size}")

            diceBag
        }

    override fun fetch(uuid: String): Flow<Dice> = diceBagProtocolBufferStateFlow
        .map { bagProtocolBuffer ->
            val cacheDice =
                bagProtocolBuffer.diceList.find { it.epoch == RepositoryProtocolBufferImageCache.EPOCH_IMAGE_CACHE }
            val imageCache = cacheDice?.sideList?.associate { it.uuid to it.imageBase64 } ?: emptyMap()

            var dice = Dice()

            bagProtocolBuffer.diceList.forEach { diceProtocolBuffer ->
                if (uuid == diceProtocolBuffer.uuid) {
                    dice = mapDiceProtocolBufferIntoDice(diceProtocolBuffer, imageCache)
                }
            }
            dice
        }

    override suspend fun store(newDiceBag: DiceBag) {
        Timber.d("repositoryBag.STORE ============================================")
        Timber.d("repositoryBag.size=${newDiceBag.size}")

        updateStateFlow(newDiceBag)
    }

    override suspend fun clear() {
        diceBagProtocolBufferStateFlow.value = BagProtocolBuffer.getDefaultInstance()
    }
}
