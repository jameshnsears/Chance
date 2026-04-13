package com.github.jameshnsears.chance.ui.tab.bag

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface

class BagAndroidViewModelFactory(
    private val application: Application,
    private val repositorySettings: RepositorySettingsInterface,
    private val repositoryBag: RepositoryBagInterface,
    private val repositoryRoll: RepositoryRollInterface,
    private val resizeInitialValue: Int,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TabBagAndroidViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TabBagAndroidViewModel(
                application,
                repositorySettings,
                repositoryBag,
                repositoryRoll,
                resizeInitialValue
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
