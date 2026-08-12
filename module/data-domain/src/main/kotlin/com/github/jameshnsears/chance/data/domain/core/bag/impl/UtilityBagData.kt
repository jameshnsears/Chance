package com.github.jameshnsears.chance.data.domain.core.bag.impl

import android.content.Context

class UtilityBagData(private val context: Context?) {
    fun getString(resId: Int): String {
        return context?.getString(resId) ?: ""
    }

    @Synchronized
    fun readBase64File(fileName: String): String {
        return try {
            context!!.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (_: Exception) {
            // caused by unit tests using mock context
            ""
        }
    }
}
