package com.github.jameshnsears.chance.ui.zoom

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.github.jameshnsears.chance.data.R
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Side

abstract class ZoomViewModel : ViewModel() {
    fun zoom() {}

    fun scale() = 75.dp

    fun sideNumberPaddingTop(dice: Dice): Dp {
        return when (dice.sides.size) {
            2 -> 0.dp
            4 -> (22.dp * scale().value / 100)
            6 -> 0.dp
            8 -> (25.dp * scale().value / 100)
            10 -> (10.dp * scale().value / 100)
            12 -> (10.dp * scale().value / 100)
            else -> (42.dp * scale().value / 100)
        }
    }

    fun sideNumberFontSize(dice: Dice): TextUnit {
        return when (dice.sides.size) {
            2 -> (72.sp * scale().value / 100)
            4 -> (60.sp * scale().value / 100)
            6 -> (60.sp * scale().value / 100)
            8 -> (50.sp * scale().value / 100)
            10 -> (44.sp * scale().value / 100)
            20 -> (40.sp * scale().value / 100)
            else -> (48.sp * scale().value / 100)
        }
    }

    fun diceShape(dice: Dice): Int {
        return when (dice.sides.size) {
            2 -> R.drawable.d2
            6 -> R.drawable.d6
            10 -> R.drawable.d10
            12 -> R.drawable.d12
            else -> R.drawable.d4_d8_d20
        }
    }

    fun diceColor(hexColour: String): ColorFilter {
        //TODO rm the if! as all should have colour filled in!
        return if (hexColour == "")
            ColorFilter.tint(Color.Black)
        else
            ColorFilter.tint(makeColour(hexColour))
    }

    fun sideColorText(hexColour: String): androidx.compose.ui.graphics.Color {
        return if (hexColour == "")
            Color.White
        else
            makeColour(hexColour)
    }

    private fun makeColour(hexColour: String) =
        Color(android.graphics.Color.parseColor("#${hexColour}"))

    fun imageDrawableIdAvailable(side: Side) = side.imageDrawableId != 0
}