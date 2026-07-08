package com.github.jameshnsears.chance.data.domain.core.settings


interface SettingsDataInterface {
    var resizeZoom: Float

    var rollIndexTime: Boolean
    var rollScore: Boolean
    var rollScoreTTS: Boolean

    var diceTitle: Boolean
    var sideNumber: Boolean
    var rollBehaviour: Boolean
    var sideDescription: Boolean
    var sideSVG: Boolean

    var haptics: Boolean
    var shakeToRoll: Boolean
    var rollSound: Boolean
    var shuffle: Boolean
    var groupTitle: Boolean
}
