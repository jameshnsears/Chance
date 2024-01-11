package com.github.jameshnsears.chance.ui.zoom.bag

import com.github.jameshnsears.chance.data.repository.bag.BagRepositoryInterface
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryInterface
import com.github.jameshnsears.chance.ui.zoom.ZoomViewModel

class ZoomBagViewModel(
    val settingsRepository: SettingsRepositoryInterface,
    val bagRepository: BagRepositoryInterface
) : ZoomViewModel()