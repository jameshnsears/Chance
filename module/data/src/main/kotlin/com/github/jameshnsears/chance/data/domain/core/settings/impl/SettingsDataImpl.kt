package com.github.jameshnsears.chance.data.domain.core.settings.impl

import com.github.jameshnsears.chance.data.domain.core.settings.SettingsDataInterface

class SettingsDataImpl(
    override var resize: Int = 4,

    override var rollIndexTime: Boolean = false,
    override var rollScore: Boolean = true,

    override var diceTitle: Boolean = true,
    override var sideNumber: Boolean = true,
    override var behaviour: Boolean = true,
    override var sideDescription: Boolean = true,
    override var sideSVG: Boolean = true,

    override var rollSound: Boolean = false
) : SettingsDataInterface
