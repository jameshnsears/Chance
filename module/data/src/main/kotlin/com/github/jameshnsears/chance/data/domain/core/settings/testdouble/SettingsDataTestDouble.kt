package com.github.jameshnsears.chance.data.domain.core.settings.testdouble

import com.github.jameshnsears.chance.data.domain.core.settings.SettingsDataInterface

class SettingsDataTestDouble(
    override var resize: Int = 2,

    override var rollIndexTime: Boolean = true,
    override var rollScore: Boolean = true,

    override var diceTitle: Boolean = true,
    override var sideNumber: Boolean = true,
    override var rollBehaviour: Boolean = true,
    override var sideDescription: Boolean = true,
    override var sideSVG: Boolean = true,

    override var rollSound: Boolean = true
) : SettingsDataInterface
