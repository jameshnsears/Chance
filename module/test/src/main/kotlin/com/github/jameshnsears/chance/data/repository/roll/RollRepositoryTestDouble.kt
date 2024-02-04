package com.github.jameshnsears.chance.data.repository.roll

import com.github.jameshnsears.chance.data.domain.RollHistory
import com.github.jameshnsears.chance.data.protocolbuffer.RollHistoryProtocolBuffer
import com.google.protobuf.util.JsonFormat

class RollRepositoryTestDouble private constructor() :
    RollRepositoryInterface {
    companion object {
        private var instance: RollRepositoryTestDouble? = null

        fun getInstance(): RollRepositoryTestDouble {
            if (instance == null) {
                instance = RollRepositoryTestDouble()
            }
            return instance!!
        }
    }

    private lateinit var rollHistory: RollHistory

    override fun fetch(): RollHistory {
        return rollHistory
    }

    override fun store(newRollHistory: RollHistory) {
        rollHistory = newRollHistory
    }

    override suspend fun export(): String {
        val rollHistoryProtocolBufferBuilder: RollHistoryProtocolBuffer.Builder =
            RollHistoryProtocolBuffer.newBuilder()

        mapBagIntoBagProtocolBufferBuilder(rollHistory, rollHistoryProtocolBufferBuilder)

        return JsonFormat.printer().includingDefaultValueFields()
            .print(rollHistoryProtocolBufferBuilder.build())
    }

    override suspend fun import(json: String) {
        TODO("Not yet implemented")
    }
}
