package com.github.jameshnsears.chance.ui.zoom.roll

import com.github.jameshnsears.chance.data.repository.roll.RollRepositoryInterface
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryInterface
import com.github.jameshnsears.chance.ui.zoom.ZoomViewModel

class ZoomRollViewModel(
    settingsRepository: SettingsRepositoryInterface,
    val rollRepository: RollRepositoryInterface,
) : ZoomViewModel()
