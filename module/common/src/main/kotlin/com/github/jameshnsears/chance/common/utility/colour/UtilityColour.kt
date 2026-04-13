package com.github.jameshnsears.chance.common.utility.colour

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

class UtilityColour {
    companion object {
        fun makeColor(hexColour: String) = Color("#${hexColour}".toColorInt())
    }
}
