package com.github.jameshnsears.chance.ui.tab.roll

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsInterface

class TabRollAndroidViewModelFactory(
    private val application: Application,
    private val repositorySettings: RepositorySettingsInterface,
    private val repositoryBag: RepositoryBagInterface,
    private val repositoryRoll: RepositoryRollInterface,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TabRollAndroidViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TabRollAndroidViewModel(
                application,
                repositorySettings,
                repositoryBag,
                repositoryRoll
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
