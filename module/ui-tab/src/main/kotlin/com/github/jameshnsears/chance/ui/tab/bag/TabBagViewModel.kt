package com.github.jameshnsears.chance.ui.tab.bag

import androidx.lifecycle.ViewModel
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryInterface
import com.github.jameshnsears.chance.data.settings.repository.SettingsRepositoryInterface

class TabBagViewModel(
    val settingsRepository: SettingsRepositoryInterface,
    val bagRepository: BagRepositoryInterface,
) : ViewModel() {
    fun demoBag(selected: Boolean = true) {

    }

    fun export() {

    }

    fun import() {

    }
}
