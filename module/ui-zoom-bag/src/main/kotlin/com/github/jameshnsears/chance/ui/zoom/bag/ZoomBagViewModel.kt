package com.github.jameshnsears.chance.ui.zoom.bag

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.github.jameshnsears.chance.data.R
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryInterface
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Side
import com.github.jameshnsears.chance.data.zoom.model.ZoomModel

open class ZoomBagViewModel(
    val bagRepository: BagRepositoryInterface,
) : ViewModel() {
//    var _description = MutableStateFlow(fetchInitialDescription())
//    var description: StateFlow<String> = _description
//
//    fun fetchInitialDescription(): String = bagModel.fetchDiceDescription(diceIndex)
//
//    fun updateCurrentDescription(description: String) {
//        Timber.d("description=", description)
//        _description.value = description
//    }

    /////////////////

    // TODO might not need a ZoomModel!
    val zoomModel = ZoomModel()

    fun scale() = 75.dp    // 75; 50; 25

    fun scaleValuePaddingTop(dice: Dice): Dp {
        return when (dice.sides.size) {
            2 -> 0.dp
            4 -> (25.dp * scale().value / 100)
            6 -> 0.dp
            8 -> (30.dp * scale().value / 100)
            10 -> (15.dp * scale().value / 100)
            12 -> (15.dp * scale().value / 100)
            else -> (48.dp * scale().value / 100)
        }
    }

    fun scaleTextFontSize(dice: Dice): TextUnit {
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

    fun sideAppearance(dice: Dice): Int {
        return when (dice.sides.size) {
            2 -> R.drawable.d2
            6 -> R.drawable.d6
            10 -> R.drawable.d10
            12 -> R.drawable.d12
            else -> R.drawable.d4_d8_d20
        }
    }

    fun textStringsIdAvailable(side: Side) = side.descriptionStringsId != 0

    fun imageDrawableIdAvailable(side: Side) = side.imageDrawableId != 0
}