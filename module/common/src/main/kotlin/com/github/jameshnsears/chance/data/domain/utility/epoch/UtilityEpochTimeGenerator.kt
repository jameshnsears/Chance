package com.github.jameshnsears.chance.data.domain.utility.epoch

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UtilityEpochTimeGenerator {
    companion object {
        fun now(): Long {
            Thread.sleep(2)
            return System.currentTimeMillis()
        }

        fun format(timeMillis: Long): String =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
                .format(Date(timeMillis))
    }
}
