package com.github.jameshnsears.chance.ui.dialog.bag.card.dice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface

class CardDiceViewModelFactory(
    private val repositoryBag: RepositoryBagInterface,
    private val dice: Dice
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardDiceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CardDiceViewModel(
                repositoryBag,
                dice
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

