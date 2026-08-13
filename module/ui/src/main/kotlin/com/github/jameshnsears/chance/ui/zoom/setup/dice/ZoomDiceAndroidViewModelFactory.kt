package com.github.jameshnsears.chance.ui.zoom.setup.dice

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.locked.RepositoryLockedInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.data.repo.api.side.RepositorySideCacheInterface

class ZoomDiceAndroidViewModelFactory(
    private val application: Application,
    private val repositorySettings: RepositorySettingsInterface,
    private val repositoryBag: RepositoryBagInterface,
    private val repositoryRoll: RepositoryRollInterface,
    private val repositoryLocked: RepositoryLockedInterface,
    private val repositorySideCache: RepositorySideCacheInterface? = null,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ZoomDiceAndroidViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ZoomDiceAndroidViewModel(
                application,
                repositorySettings,
                repositoryBag,
                repositoryRoll,
                repositoryLocked,
                repositorySideCache
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
