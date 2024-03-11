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

        fun formatDay(timeMillis: Long): String =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(Date(timeMillis))

        fun formatTime(timeMillis: Long): String =
            SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
                .format(Date(timeMillis))
    }
}
