package com.github.jameshnsears.chance.utils.epoch

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EpochTime {
    companion object {
        fun now() = System.currentTimeMillis()

        fun formatDay(timeMillis: Long): String =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(Date(timeMillis))

        fun formatTime(timeMillis: Long): String =
            SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
                .format(Date(timeMillis))
    }
}
