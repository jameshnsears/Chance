package com.github.jameshnsears.chance.ui.zoom.roll

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.ui.tab.ResizeEvent
import com.github.jameshnsears.chance.ui.tab.bag.TabBagResetStorageEvent
import com.github.jameshnsears.chance.ui.tab.roll.TabRollEvent
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class ZoomRollAndroidViewModel(
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
            merge(
                TabRollEvent.sharedFlowTabRollEvent.map { },
                TabBagResetStorageEvent.sharedFlowTabBagResetStorageEvent.map { }
            ).collect {
                Timber.d("collect.TabRollEvent|TabBagResetStorageEvent")
                updateStateFlowZoom()
            }
        }

        viewModelScope.launch {
            ResizeEvent.sharedFlowResizeEvent.collect {
                Timber.d("collect.ResizeEvent")
                updateResize()
            }
        }
    }

    override suspend fun updateStateFlowZoom() {
        _stateFlowZoom.update {
            it.copy(
                diceBag = repositoryBag.fetch().first(),
                rollHistory = repositoryRoll.fetch().first()
            )
        }

        updateDiceBagList()
    }
}
