package com.github.jameshnsears.chance.ui.zoom.rolls

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.core.group.GroupHistory
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.group.RepositoryGroupInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
    val groupHistory: StateFlow<GroupHistory> = repositoryGroup.fetch()
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _lockedRollIndices = MutableStateFlow<Set<Int>>(emptySet())
    val lockedRollIndices: StateFlow<Set<Int>> = _lockedRollIndices

    fun toggleLock(index: Int) {
        _lockedRollIndices.update { current ->
            if (current.contains(index)) current - index else current + index
        }
    }

    init {
        viewModelScope.launch {
            stateFlowZoom.map { it.rollHistory.keys.firstOrNull() }
                .distinctUntilChanged()
                .collect {
                    _lockedRollIndices.value = emptySet()
                }
        }
    }
}
