package com.github.jameshnsears.chance.ui.dialog.bag.card.roll

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.jameshnsears.chance.data.domain.core.Dice

class CardRollViewModelFactory(
    private val dice: Dice,
    private var diceSidesSize: Int = dice.sides.size
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardRollViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CardRollViewModel(
                dice,
                diceSidesSize
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}