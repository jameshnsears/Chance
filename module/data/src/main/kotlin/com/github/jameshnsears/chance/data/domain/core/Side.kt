package com.github.jameshnsears.chance.data.domain.core

import java.util.UUID

data class Side(
    var uuid: String = UUID.randomUUID().toString(),

    var number: Int = 0,
    var numberColour: String = "FFFFFFFF",
    var imageDrawableId: Int = 0,
    var imageBase64: String = "",
    var description: String = "",
    var descriptionColour: String = "FF000000"
) {
    fun copy(): Side {
        return Side(
            uuid,
            number,
            numberColour,
            imageDrawableId,
            imageBase64,
            description,
            descriptionColour
        )
    }
}
