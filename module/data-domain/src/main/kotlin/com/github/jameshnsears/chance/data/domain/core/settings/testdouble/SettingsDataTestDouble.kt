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

    override var rollSound: Boolean = true,
    override var shuffle: Boolean = false
) : SettingsDataInterface {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other !is SettingsDataTestDouble) return false

        if (resize != other.resize) return false
        if (rollIndexTime != other.rollIndexTime) return false
        if (rollScore != other.rollScore) return false
        if (diceTitle != other.diceTitle) return false
        if (sideNumber != other.sideNumber) return false
        if (rollBehaviour != other.rollBehaviour) return false
        if (sideDescription != other.sideDescription) return false
        if (sideSVG != other.sideSVG) return false
        if (rollSound != other.rollSound) return false
        if (shuffle != other.shuffle) return false

        return true
    }
}
