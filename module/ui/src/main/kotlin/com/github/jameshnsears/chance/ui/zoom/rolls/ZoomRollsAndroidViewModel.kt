package com.github.jameshnsears.chance.ui.zoom.rolls

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.core.group.GroupHistory
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.group.RepositoryGroupInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.ui.tab.DisplayIndexEvent
import com.github.jameshnsears.chance.ui.tab.GroupEvent
import com.github.jameshnsears.chance.ui.tab.SetupImportEvent
import com.github.jameshnsears.chance.ui.tab.rolls.RollsEvent
import com.github.jameshnsears.chance.ui.tab.setup.dice.DiceResetEvent
import com.github.jameshnsears.chance.ui.tab.setup.dice.DiceResizeEvent
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class ZoomRollsAndroidViewModel(
    application: Application,
    repositorySettings: RepositorySettingsInterface,
    repositoryBag: RepositoryBagInterface,
    repositoryRoll: RepositoryRollInterface,
    val repositoryGroup: RepositoryGroupInterface,
) : ZoomAndroidViewModel(
    application,
    repositorySettings,
    repositoryBag,
    repositoryRoll,
) {
    private val _groupHistory = MutableStateFlow<GroupHistory>(listOf())

    init {
        viewModelScope.launch {
            updateResize()
            updateStateFlowZoom()
        }

        viewModelScope.launch {
            merge(
                SetupImportEvent.sharedFlowTabBagImportEvent.map { },
                DiceResetEvent.sharedFlowTabBagResetEvent.map { },
                GroupEvent.sharedFlowGroupEvent.map { }
            ).collect {
                Timber.d("collect.DiceResetEvent|GroupEvent")
                _stateFlowZoom.update {
                    it.copy(
                        horizontalScrollPositions = emptyMap()
                    )
                }
                updateStateFlowZoom()
            }
        }

        viewModelScope.launch {
            RollsEvent.sharedFlowTabRollEvent.collect {
                Timber.d("collect.RollsEvent")
                updateStateRollHistory()
            }
        }

        viewModelScope.launch {
            DiceResizeEvent.sharedFlowResizeEvent.collect {
                Timber.d("collect.DiceResizeEvent")
                updateResize()
            }
        }

        viewModelScope.launch {
            DisplayIndexEvent.sharedFlowDisplayIndexEvent.collect {
                Timber.d("collect.DisplayIndexEvent")
                updateStateFlowZoom()
            }
        }
    }

    suspend fun updateStateRollHistory() {
        val rollHistory = repositoryRoll.fetch().firstOrNull()

        _stateFlowZoom.update {
            it.copy(
                rollHistory = rollHistory ?: LinkedHashMap()
            )
        }
    }

    override suspend fun updateStateFlowZoom() {
        val diceBag = repositoryBag.fetch().firstOrNull()
        val rollHistory = repositoryRoll.fetch().firstOrNull()
        val groupHistory = repositoryGroup.fetch().firstOrNull()

        updateDiceBagList(diceBag)
        _groupHistory.value = groupHistory ?: listOf()

        _stateFlowZoom.update {
            it.copy(
                diceBag = diceBag ?: mutableListOf(),
                rollHistory = rollHistory ?: LinkedHashMap()
            )
        }
    }

    fun fetchGroupNameFromUuid(uuidGroup: String): String {
        return _groupHistory.value.find { it.uuid == uuidGroup }?.name ?: ""
    }
}
