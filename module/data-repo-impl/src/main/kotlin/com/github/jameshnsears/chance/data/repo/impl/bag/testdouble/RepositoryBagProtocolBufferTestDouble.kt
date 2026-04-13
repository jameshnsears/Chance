package com.github.jameshnsears.chance.data.repo.impl.bag.testdouble

import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.proto.BagProtocolBuffer
import com.github.jameshnsears.chance.data.domain.proto.DiceProtocolBuffer
import com.github.jameshnsears.chance.data.repo.impl.bag.RepositoryBagProtocolBufferInterface
import com.google.protobuf.Descriptors
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RepositoryBagProtocolBufferTestDouble private constructor() :
    RepositoryBagProtocolBufferInterface {
    companion object {
        private var instance: RepositoryBagProtocolBufferTestDouble? = null

        fun getInstance(
            diceBag: DiceBag
        ): RepositoryBagProtocolBufferTestDouble {
            if (instance == null) {
                instance = RepositoryBagProtocolBufferTestDouble()

                instance!!.diceBag = diceBag
                instance!!.traceUuid(diceBag)
            }

            return instance!!
        }
    }

    private lateinit var diceBag: DiceBag

    override suspend fun jsonExport(): String {
        val bagProtocolBufferBuilder: BagProtocolBuffer.Builder =
            BagProtocolBuffer.newBuilder()

        mapDiceBagIntoBagProtocolBufferBuilder(diceBag, bagProtocolBufferBuilder)

        val fieldsToAlwaysOutput: MutableSet<Descriptors.FieldDescriptor> = HashSet()
        fieldsToAlwaysOutput.add(DiceProtocolBuffer.getDescriptor().findFieldByName("selected"))

        return JsonFormat.printer().includingDefaultValueFields(fieldsToAlwaysOutput)
            .print(bagProtocolBufferBuilder.build())
    }

    override suspend fun jsonImport(json: String) {
        store(jsonImportProcess(json))
    }

    override suspend fun fetch(): Flow<DiceBag> = flow {
        emit(diceBag)
    }

    override suspend fun fetch(epoch: Long): Flow<Dice> = flow {
        emit(diceBag.firstOrNull { it.epoch == epoch } ?: Dice())
    }

    override suspend fun store(newDiceBag: DiceBag) {
        diceBag = newDiceBag
    }

    override suspend fun clear() {
        diceBag.clear()
    }
}
