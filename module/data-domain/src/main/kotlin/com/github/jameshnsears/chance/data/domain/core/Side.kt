package com.github.jameshnsears.chance.data.domain.core

import coil.request.ImageRequest
import java.util.UUID

data class Side(
    var uuid: String = UUID.randomUUID().toString(),
    var number: Int = 0,
    var numberColour: String = "FFFFFFFF",
    var imageDrawableId: Int = 0,
    var imageBase64: String = "",
    var imageRequest: ImageRequest? = null,         // not in .proto
    var description: String = "",
    var descriptionColour: String = "FF000000",
    var imageBase64Uuid: String = ""
) {
    fun copy(): Side {
        return Side(
            uuid,
            number,
            numberColour,
            imageDrawableId,
            imageBase64,
            imageRequest,
            description,
            descriptionColour,
            imageBase64Uuid
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Side) return false

        if (uuid != other.uuid) return false
        if (number != other.number) return false
        if (numberColour != other.numberColour) return false
        if (imageDrawableId != other.imageDrawableId) return false
        if (imageBase64 != other.imageBase64) return false
        if (description != other.description) return false
        if (descriptionColour != other.descriptionColour) return false
        if (imageBase64Uuid != other.imageBase64Uuid) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uuid.hashCode()
        result = 31 * result + number
        result = 31 * result + numberColour.hashCode()
        result = 31 * result + imageDrawableId
        result = 31 * result + imageBase64.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + descriptionColour.hashCode()
        result = 31 * result + imageBase64Uuid.hashCode()
        return result
    }

    override fun toString(): String {
        return "number=$number"
    }
}
