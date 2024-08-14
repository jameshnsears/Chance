package com.github.jameshnsears.chance.ui.dialog.bag.card.side

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.jameshnsears.chance.data.domain.core.Side

class CardSideAndroidViewModelFactory(
    private val application: Application,
    private val side: Side
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardSideAndroidViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CardSideAndroidViewModel(
                application,
                side
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}