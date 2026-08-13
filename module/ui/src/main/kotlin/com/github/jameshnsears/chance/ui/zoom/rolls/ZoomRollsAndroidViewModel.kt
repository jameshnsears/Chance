package com.github.jameshnsears.chance.ui.zoom.rolls

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.core.group.GroupHistory
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.group.RepositoryGroupInterface
import com.github.jameshnsears.chance.data.repo.api.locked.RepositoryLockedInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.data.repo.api.side.RepositorySideCacheInterface
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ZoomRollsAndroidViewModel(
    application: Application,
    repositorySettings: RepositorySettingsInterface,
    repositoryBag: RepositoryBagInterface,
    repositoryRoll: RepositoryRollInterface,
    repositoryLocked: RepositoryLockedInterface,
    repositorySideCache: RepositorySideCacheInterface? = null,
    val repositoryGroup: RepositoryGroupInterface,
) : ZoomAndroidViewModel(
    application,
    repositorySettings,
    repositoryBag,
    repositoryRoll,
    repositoryLocked,
    repositorySideCache,
) {
    val groupHistory: StateFlow<GroupHistory> = repositoryGroup.fetch()
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleLock(roll: Roll) {
        val key = roll.unitKey()
        viewModelScope.launch {
            val current = stateFlowZoom.value.lockedRollIndices
            val newSet = if (current.contains(key)) current - key else current + key
            repositoryLocked.store(newSet)
        }
    }

    init {
        viewModelScope.launch {
            stateFlowZoom.map { it.rollHistory.keys.firstOrNull() }
                // the stateIn initialValue has an empty roll history, so drop the
                // placeholder (null) before observing real key changes
                .dropWhile { it == null }
                .distinctUntilChanged()
                // skip the first real key: loading existing history is not a new roll
                .drop(1)
                .collect {
                    repositoryLocked.clear()
                }
        }
    }
}
