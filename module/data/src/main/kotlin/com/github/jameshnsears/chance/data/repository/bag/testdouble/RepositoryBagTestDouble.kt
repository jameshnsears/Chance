package com.github.jameshnsears.chance.data.repository.bag.testdouble

import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.proto.BagProtocolBuffer
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.google.protobuf.util.JsonFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class RepositoryBagTestDouble private constructor() :
    RepositoryBagInterface {
    companion object {
        private var instance: RepositoryBagTestDouble? = null

        fun getInstance(diceBag: DiceBag): RepositoryBagTestDouble {
            if (instance == null) {
                instance = RepositoryBagTestDouble()
                instance!!.diceBag = diceBag
            }
            return instance!!
        }
    }

    private lateinit var diceBag: DiceBag

    override suspend fun jsonExport(): String {
        val bagProtocolBufferBuilder: BagProtocolBuffer.Builder =
            BagProtocolBuffer.newBuilder()

        mapDiceBagIntoBagProtocolBufferBuilder(diceBag, bagProtocolBufferBuilder)

        return JsonFormat.printer().includingDefaultValueFields()
            .print(bagProtocolBufferBuilder.build())
    }

    override suspend fun jsonImport(json: String) {
        store(jsomImportProcess(json))
    }

    override suspend fun fetch(): Flow<DiceBag> = flow {
        emit(diceBag)
    }

    override suspend fun fetch(epoch: Long): Flow<Dice> = flow {
        emit(diceBag.firstOrNull { it.epoch == epoch } ?: Dice())
    }

    override suspend fun store(newDiceBag: DiceBag) {
        clear()

        diceBag = newDiceBag
    }

    override suspend fun clear() {
        diceBag.clear()
    }
}
