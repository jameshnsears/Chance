package com.github.jameshnsears.chance.data.repository.bag

import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.DiceBag
import com.github.jameshnsears.chance.data.protocolbuffer.DiceBagProtocolBuffer
import com.google.protobuf.util.JsonFormat

class DiceBagRepositoryTestDouble private constructor() :
    DiceBagRepositoryInterface {
    companion object {
        private var instance: DiceBagRepositoryTestDouble? = null

        fun getInstance(): DiceBagRepositoryTestDouble {
            if (instance == null) {
                instance = DiceBagRepositoryTestDouble()
            }
            return instance!!
        }
    }

    private lateinit var diceBag: DiceBag

    override suspend fun export(): String {
        val diceBagProtocolBufferBuilder: DiceBagProtocolBuffer.Builder =
            DiceBagProtocolBuffer.newBuilder()

        mapBagIntoBagProtocolBufferBuilder(diceBag, diceBagProtocolBufferBuilder)

        return JsonFormat.printer().includingDefaultValueFields()
            .print(diceBagProtocolBufferBuilder.build())
    }

    override suspend fun import(json: String) {
        TODO("Not yet implemented")
    }

    override suspend fun fetch(): DiceBag {
        return diceBag
    }

    override suspend fun fetch(epoch: Long) = diceBag.firstOrNull { it.epoch == epoch } ?: Dice()

    override suspend fun store(newDiceBag: DiceBag) {
        diceBag = newDiceBag
    }
}
