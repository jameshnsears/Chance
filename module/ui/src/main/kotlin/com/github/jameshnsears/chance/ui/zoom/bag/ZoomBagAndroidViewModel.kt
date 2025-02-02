package com.github.jameshnsears.chance.ui.zoom.bag

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.ui.tab.bag.TabBagResetStorageEvent
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class ZoomBagAndroidViewModel(
    application: Application,
    repositorySettings: RepositorySettingsInterface,
    repositoryBag: RepositoryBagInterface,
    repositoryRoll: RepositoryRollInterface,
) : ZoomAndroidViewModel(
    application,
    repositorySettings,
    repositoryBag,
    repositoryRoll,
) {
    init {
        viewModelScope.launch {
            updateResize()
            updateStateFlowZoom()
        }

        viewModelScope.launch {
            TabBagResetStorageEvent.sharedFlowTabBagResetStorageEvent.collect {
                Timber.d("collect.TabBagResetStorageEvent.ZoomBagAndroidViewModel")
                updateStateFlowZoom()
            }
        }
    }

    override suspend fun updateStateFlowZoom() {
        _stateFlowZoom.update {
            it.copy(
                diceBag = repositoryBag.fetch().first()
            )
        }

        updateDiceBagList()
    }
}
