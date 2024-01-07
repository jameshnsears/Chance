package com.github.jameshnsears.chance.ui.zoom.roll

import com.github.jameshnsears.chance.data.roll.repository.RollRepositoryInterface
import com.github.jameshnsears.chance.data.settings.repository.SettingsRepositoryInterface
import com.github.jameshnsears.chance.ui.zoom.ZoomViewModel

class ZoomRollViewModel(
    settingsRepository: SettingsRepositoryInterface,
    val rollRepository: RollRepositoryInterface,
) : ZoomViewModel()
