package com.github.jameshnsears.chance.data.repo.impl.bag.testdouble

import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.proto.BagProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.DiceProtocolBuffer
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
            diceBag: DiceBag,
        ): RepositoryBagProtocolBufferTestDouble {
            if (instance == null) {
                instance = RepositoryBagProtocolBufferTestDouble()
            }

            if (!instance!!.initialized) {
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
        fieldsToAlwaysOutput.add(DiceProtocolBuffer.getDescriptor().findFieldByName("selected"))

        return JsonFormat.printer().includingDefaultValueFields(fieldsToAlwaysOutput)
            .print(diceBagProtocolBufferStateFlow.value)
    }

    override suspend fun jsonImport(json: String) {
        store(jsonImportProcess(json))
    }

    override suspend fun fetch(): Flow<DiceBag> = diceBagProtocolBufferStateFlow
        .map { bagProtocolBuffer ->
            val diceBag = mapBagProtocolBufferIntoDiceBag(bagProtocolBuffer)

            Timber.d("repositoryBag.FETCH ============================================")
            Timber.d("repositoryBag.size=${diceBag.size}")

            diceBag
        }

    override suspend fun fetch(uuid: String): Flow<Dice> = diceBagProtocolBufferStateFlow
        .map { bagProtocolBuffer ->
            var dice = Dice()

            bagProtocolBuffer.diceList.forEach { diceProtocolBuffer ->
                if (uuid == diceProtocolBuffer.uuid) {
                    dice = mapDiceProtocolBufferIntoDice(diceProtocolBuffer)
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
