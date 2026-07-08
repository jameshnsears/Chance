package com.github.jameshnsears.chance.common.utility

import android.content.Context
import androidx.core.content.edit

class UtilitySharedPreferencesHelper(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("UtilitySharedPreferencesHelper", Context.MODE_PRIVATE)

    var lastTab: Int
        get() = sharedPreferences.getInt("lastTab", 0)
        set(value) {
            sharedPreferences.edit(commit = true) { putInt("lastTab", value) }
        }

    var lastSubTab: Int
        get() = sharedPreferences.getInt("lastSubTab", 0)
        set(value) {
            sharedPreferences.edit(commit = true) { putInt("lastSubTab", value) }
        }
}
