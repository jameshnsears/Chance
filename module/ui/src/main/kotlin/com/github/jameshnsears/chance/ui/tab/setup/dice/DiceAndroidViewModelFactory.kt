package com.github.jameshnsears.chance.ui.tab.setup.dice

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.group.RepositoryGroupInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface

class DiceAndroidViewModelFactory(
    private val application: Application,
    private val repositorySettings: RepositorySettingsInterface,
    private val repositoryBag: RepositoryBagInterface,
    private val repositoryRoll: RepositoryRollInterface,
    private val repositoryGroup: RepositoryGroupInterface,
    private val resizeInitialValue: Float,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiceAndroidViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DiceAndroidViewModel(
                application,
                repositorySettings,
                repositoryBag,
                repositoryRoll,
                repositoryGroup,
                resizeInitialValue
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
