package com.github.jameshnsears.chance.ui.dialog.dice

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface

class DialogDiceAndroidViewModelFactory(
    private val application: Application,
    private val repositoryBag: RepositoryBagInterface,
    private val dice: Dice,
    private val side: Side,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DialogDiceAndroidViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DialogDiceAndroidViewModel(
                application,
                repositoryBag,
                dice,
                side
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
