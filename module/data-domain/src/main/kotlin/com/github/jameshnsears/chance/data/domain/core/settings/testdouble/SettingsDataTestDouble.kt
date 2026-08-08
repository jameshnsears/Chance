package com.github.jameshnsears.chance.data.domain.core.settings.testdouble

import com.github.jameshnsears.chance.data.domain.core.settings.SettingsDataInterface

class SettingsDataTestDouble(
    override var resizeZoom: Float = 2.0f,

    override var rollIndexTime: Boolean = false,
    override var rollScore: Boolean = true,
    override var groupTitle: Boolean = true,
    override var diceTitle: Boolean = true,
    override var rollBehaviour: Boolean = true,

    override var layout: Boolean = false,
    override var history: Boolean = false,

    override var sideNumber: Boolean = true,
    override var sideSVG: Boolean = false,
    override var sideDescription: Boolean = true,

    override var shuffle: Boolean = false,

    override var haptics: Boolean = false,
    override var rollSound: Boolean = false,
    override var rollScoreTTS: Boolean = false,
    override var shakeToRoll: Boolean = false,
) : SettingsDataInterface {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SettingsDataInterface) return false

        if (resizeZoom != other.resizeZoom) return false
        if (rollIndexTime != other.rollIndexTime) return false
        if (rollScore != other.rollScore) return false
        if (rollScoreTTS != other.rollScoreTTS) return false
        if (diceTitle != other.diceTitle) return false
        if (sideNumber != other.sideNumber) return false
        if (rollBehaviour != other.rollBehaviour) return false
        if (sideDescription != other.sideDescription) return false
        if (sideSVG != other.sideSVG) return false
        if (haptics != other.haptics) return false
        if (rollSound != other.rollSound) return false
        if (shuffle != other.shuffle) return false
        if (shakeToRoll != other.shakeToRoll) return false
        if (groupTitle != other.groupTitle) return false
        if (layout != other.layout) return false
        if (history != other.history) return false

        return true
    }

    override fun hashCode(): Int {
        var result = resizeZoom.hashCode()
        result = 31 * result + rollIndexTime.hashCode()
        result = 31 * result + rollScore.hashCode()
        result = 31 * result + rollScoreTTS.hashCode()
        result = 31 * result + diceTitle.hashCode()
        result = 31 * result + sideNumber.hashCode()
        result = 31 * result + rollBehaviour.hashCode()
        result = 31 * result + sideDescription.hashCode()
        result = 31 * result + sideSVG.hashCode()
        result = 31 * result + haptics.hashCode()
        result = 31 * result + rollSound.hashCode()
        result = 31 * result + shuffle.hashCode()
        result = 31 * result + shakeToRoll.hashCode()
        result = 31 * result + groupTitle.hashCode()
        result = 31 * result + layout.hashCode()
        result = 31 * result + history.hashCode()
        return result
    }
}
