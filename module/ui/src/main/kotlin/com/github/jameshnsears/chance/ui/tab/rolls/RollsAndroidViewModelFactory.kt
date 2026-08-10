package com.github.jameshnsears.chance.ui.tab.rolls

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.group.RepositoryGroupInterface
import com.github.jameshnsears.chance.data.repo.api.locked.RepositoryLockedInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface

class RollsAndroidViewModelFactory(
    private val application: Application,
    private val repositorySettings: RepositorySettingsInterface,
    private val repositoryBag: RepositoryBagInterface,
    private val repositoryRoll: RepositoryRollInterface,
    private val repositoryGroup: RepositoryGroupInterface,
    private val repositoryLocked: RepositoryLockedInterface,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RollsAndroidViewModel::class.java)) {
            val viewModel = RollsAndroidViewModel(
                application,
                repositorySettings,
                repositoryBag,
                repositoryRoll,
                repositoryGroup,
                repositoryLocked,
                RollsSelectionHelper(repositoryBag, repositoryGroup),
                RollsCoreHelper(repositoryRoll)
            )
            @Suppress("UNCHECKED_CAST")
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
