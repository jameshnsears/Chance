package com.github.jameshnsears.chance.ui.zoom.bag

import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.repository.bag.DiceBagRepositoryInterface
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryInterface
import com.github.jameshnsears.chance.ui.zoom.ZoomViewModel
import kotlinx.coroutines.launch

class ZoomBagViewModel(
    private val settingsRepository: SettingsRepositoryInterface,
    bagRepository: DiceBagRepositoryInterface,
) : ZoomViewModel(bagRepository) {
    init {
        viewModelScope.launch {
            _diceDiceBag.value = bagRepository.fetch()
        }
    }
}
