package com.github.jameshnsears.chance.ui.dialog.bag.card

import android.app.Application
import androidx.lifecycle.AndroidViewModel

open class BagCardAndroidViewModel(application: Application) : AndroidViewModel(application) {
    protected fun getString(stringsId: Int): String {
        return try {
            getApplication<Application>().getString(stringsId)
        } catch (e: NullPointerException) {
            // support for previews
            stringsId.toString()
        }
    }
}