package com.github.jameshnsears.chance.ui.zoom.setup.dice

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.ui.tab.setup.dice.DiceResetEvent
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class ZoomDiceAndroidViewModel(
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
            repositoryBag.fetch().collect {
                updateStateFlowZoom()
            }
        }

        viewModelScope.launch {
            updateResize()
            updateStateFlowZoom()
        }

        viewModelScope.launch {
            DiceResetEvent.sharedFlowTabBagResetEvent.collect {
                Timber.d("collect.DiceResetEvent")
                _stateFlowZoom.update {
                    it.copy(
                        firstVisibleItemIndex = 0,
                        firstVisibleItemScrollOffset = 0,
                        horizontalScrollPositions = emptyMap()
                    )
                }
                updateResize()
                updateStateFlowZoom()
            }
        }
    }

    override suspend fun updateStateFlowZoom() {
        val diceBag = repositoryBag.fetch().firstOrNull()

        updateDiceBagList(diceBag)

        if (diceBag != null) {
            _stateFlowZoom.update {
                it.copy(
                    diceBag = diceBag
                )
            }
        }
    }
}
