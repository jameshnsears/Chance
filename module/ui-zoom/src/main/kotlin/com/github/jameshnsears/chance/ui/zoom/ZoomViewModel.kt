package com.github.jameshnsears.chance.ui.zoom

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryInterface
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Side
import com.github.jameshnsears.chance.data.zoom.model.ZoomModel

open class ZoomViewModel(
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

    fun bagDemo() = bagRepository.bagDemo

    fun scale() = 100.dp    // 75; 50; 25

    fun scaleTextPaddingTop(dice: Dice): Dp {
        when (scale()) {
            100.dp -> { // done
                return when (dice.sides.size) {
                    4 -> 25.dp
                    8  -> 30.dp
                    10 -> 15.dp
                    12 -> 15.dp
                    20 -> 40.dp
                    else -> 0.dp
                }
            }

            50.dp -> {
                return when (dice.sides.size) {
                    10 -> 12.dp
                    4, 8, 20 -> 20.dp
                    else -> 0.dp
                }
            }

            else -> {
                return when (dice.sides.size) {
                    10 -> 25.dp
                    4, 8, 20 -> 40.dp
                    else -> 0.dp
                }
            }
        }
    }

    fun scaleTextFontSize(dice: Dice): TextUnit {
        when (scale()) {
            100.dp -> { // done
                return when (dice.sides.size) {
                    2 -> 72.sp
                    4 -> 60.sp
                    6 -> 60.sp
                    8 -> 50.sp
                    10 -> 44.sp
                    20 -> 40.sp
                    else -> 48.sp
                }
            }

            50.dp -> {
                return when (dice.sides.size) {
                    2 -> 30.sp
                    10 -> 18.sp
                    4, 8, 20 -> 18.sp
                    else -> 18.sp
                }
            }

            else -> {
                return when (dice.sides.size) {
                    2 -> 15.sp
                    10 -> 9.sp
                    4, 8, 20 -> 9.sp
                    else -> 9.sp
                }
            }
        }
    }

    fun textStringsIdAvailable(side: Side) = side.textStringsId != 0

    fun imageDrawableIdAvailable(side: Side) = side.imageDrawableId != 0
}