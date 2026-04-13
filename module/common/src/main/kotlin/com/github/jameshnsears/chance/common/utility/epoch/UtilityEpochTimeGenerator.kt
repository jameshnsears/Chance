package com.github.jameshnsears.chance.common.utility.epoch

class UtilityEpochTimeGenerator {
    companion object {
        fun currentEpochMillis(): Long {
            Thread.sleep(2)
            return System.currentTimeMillis()
        }
    }
}
