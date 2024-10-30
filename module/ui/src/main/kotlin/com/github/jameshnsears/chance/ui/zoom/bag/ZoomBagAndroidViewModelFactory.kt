package com.github.jameshnsears.chance.ui.zoom.bag

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsInterface

class ZoomBagAndroidViewModelFactory(
    private val application: Application,
    private val repositorySettings: RepositorySettingsInterface,
    private val repositoryBag: RepositoryBagInterface,
    private val repositoryRoll: RepositoryRollInterface,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ZoomBagAndroidViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ZoomBagAndroidViewModel(
                application,
                repositorySettings,
                repositoryBag,
                repositoryRoll
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
