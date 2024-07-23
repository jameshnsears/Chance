package com.github.jameshnsears.chance.data.domain.core.settings


interface SettingsDataInterface {
    var resize: Int

    var rollIndexTime: Boolean
    var rollScore: Boolean

    var diceTitle: Boolean
    var sideNumber: Boolean
    var behaviour: Boolean
    var sideDescription: Boolean
    var sideSVG: Boolean

    var rollSound: Boolean
}