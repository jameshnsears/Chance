package com.github.jameshnsears.chance.ui.zoom.roll

import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.repository.bag.DiceBagRepositoryInterface
import com.github.jameshnsears.chance.data.repository.roll.RollRepositoryInterface
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryInterface
import com.github.jameshnsears.chance.ui.zoom.ZoomViewModel
import kotlinx.coroutines.launch

class ZoomRollViewModel(
    private val settingsRepository: SettingsRepositoryInterface,
    bagRepository: DiceBagRepositoryInterface,
    rollRepository: RollRepositoryInterface,
) : ZoomViewModel(bagRepository) {
    init {
        viewModelScope.launch {
            _rollHistory.value = rollRepository.fetch()
        }
    }
}
