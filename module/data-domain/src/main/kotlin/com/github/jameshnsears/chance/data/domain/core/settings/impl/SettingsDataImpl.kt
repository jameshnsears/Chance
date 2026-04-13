package com.github.jameshnsears.chance.data.domain.core.settings.impl

import com.github.jameshnsears.chance.data.domain.core.settings.SettingsDataInterface

class SettingsDataImpl(
    override var resize: Int = 2,

    override var rollIndexTime: Boolean = false,
    override var rollScore: Boolean = false,

    override var diceTitle: Boolean = false,
    override var sideNumber: Boolean = false,
    override var rollBehaviour: Boolean = false,
    override var sideDescription: Boolean = false,
    override var sideSVG: Boolean = true,

    override var rollSound: Boolean = false,
    override var shuffle: Boolean = false
) : SettingsDataInterface
