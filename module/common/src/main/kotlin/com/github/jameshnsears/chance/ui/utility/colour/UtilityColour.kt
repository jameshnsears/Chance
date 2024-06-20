package com.github.jameshnsears.chance.ui.utility.colour

import androidx.compose.ui.graphics.Color

class UtilityColour {
    companion object {
        fun makeColor(hexColour: String) = Color(android.graphics.Color.parseColor("#${hexColour}"))
    }
}