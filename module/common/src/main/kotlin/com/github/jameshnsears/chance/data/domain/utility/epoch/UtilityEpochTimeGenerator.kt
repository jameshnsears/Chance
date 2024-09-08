package com.github.jameshnsears.chance.data.domain.utility.epoch

class UtilityEpochTimeGenerator {
    companion object {
        fun now(): Long {
            Thread.sleep(2)
            return System.currentTimeMillis()
        }
    }
}
