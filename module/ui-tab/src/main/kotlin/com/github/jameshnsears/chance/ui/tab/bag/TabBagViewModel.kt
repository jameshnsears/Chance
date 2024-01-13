package com.github.jameshnsears.chance.ui.tab.bag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.repository.bag.BagRepositoryInterface
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryInterface
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TabBagViewModel(
    val settingsRepository: SettingsRepositoryInterface,
    val bagRepository: BagRepositoryInterface,
) : ViewModel() {
    fun bagDemoBag(selected: Boolean = true) {
        viewModelScope.launch {
            val settings = settingsRepository.fetch().first()
            settings.bagDemoBag = selected
            settingsRepository.store(settings)
        }
    }

    suspend fun export(): String {
        // TODO combine all json from all 3 repos + wrap in an []
        // i.e. [ {a}, {b}, {c} ]
        return settingsRepository.export()
    }

    suspend fun import(json: String) {
        // TODO extract all json for all 3 repos
        settingsRepository.import(json)
    }
}
