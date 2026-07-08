package com.github.jameshnsears.chance.ui.tab.setup.groups

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.group.RepositoryGroupInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface

class GroupsAndroidViewModelFactory(
    private val application: Application,
    private val repositoryBag: RepositoryBagInterface,
    private val repositoryGroup: RepositoryGroupInterface,
    private val repositoryRoll: RepositoryRollInterface,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroupsAndroidViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GroupsAndroidViewModel(
                application,
                repositoryBag,
                repositoryGroup,
                repositoryRoll,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
