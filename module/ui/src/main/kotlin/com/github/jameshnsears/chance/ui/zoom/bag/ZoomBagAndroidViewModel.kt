package com.github.jameshnsears.chance.ui.zoom.bag

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.ui.tab.bag.BagResetEvent
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
    val showDialog: MutableState<Boolean> = mutableStateOf(false)
    val cardDice: MutableState<Dice> = mutableStateOf(Dice())
    val cardSide: MutableState<Side> = mutableStateOf(Side())

    init {
        viewModelScope.launch {
            updateResize()
            updateStateFlowZoom()
        }

        viewModelScope.launch {
            BagResetEvent.sharedFlowTabBagResetEvent.collect {
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
