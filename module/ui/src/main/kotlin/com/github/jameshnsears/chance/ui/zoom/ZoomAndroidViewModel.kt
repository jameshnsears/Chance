package com.github.jameshnsears.chance.ui.zoom

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.R
import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.domain.state.DiceBag
import com.github.jameshnsears.chance.data.domain.state.RollHistory
import com.github.jameshnsears.chance.data.domain.state.Side
import com.github.jameshnsears.chance.data.domain.utility.svg.UtilitySvgSerializer
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class ZoomState(
    var resize: Dp,
    var diceBag: DiceBag,
    var rollHistory: RollHistory
)

class ZoomAndroidViewModel(
    application: Application,
    val repositoryBag: RepositoryBagInterface,
    repositoryRoll: RepositoryRollInterface,
) : AndroidViewModel(application) {
    private val _stateFlow = MutableStateFlow(
        ZoomState(
            resize = 0.dp,
            diceBag = mutableListOf(),
            rollHistory = LinkedHashMap()
        )
    )
    val tabRollStateFlow: StateFlow<ZoomState> = _stateFlow

    private var _resizeView: MutableStateFlow<Dp> = MutableStateFlow(resizeViewInitialPosition())
    var resizeView: StateFlow<Dp> = _resizeView

    private var _diceBag: MutableStateFlow<DiceBag> = MutableStateFlow(mutableListOf())
    var diceBag: StateFlow<DiceBag> = _diceBag

    private var _rollHistory: MutableStateFlow<RollHistory> = MutableStateFlow(LinkedHashMap())
    var rollHistory: StateFlow<RollHistory> = _rollHistory

    init {
        viewModelScope.launch {
            _diceBag.value = repositoryBag.fetch().first()
            diceBag = _diceBag

            _rollHistory.value = repositoryRoll.fetch().first()
            rollHistory = _rollHistory
        }
    }

    ////////////////////////////////

    private val resizeViewDefault = 80.dp

    private fun resizeViewInitialPosition() = 80.dp

    fun resizeView(zoom: Int) {
        when (zoom) {
            1 -> _resizeView.value = resizeViewDefault * 0.4f
            2 -> _resizeView.value = resizeViewDefault * 0.6f
            3 -> _resizeView.value = resizeViewDefault * 0.8f
            5 -> _resizeView.value = resizeViewDefault * 1.2f
            6 -> _resizeView.value = resizeViewDefault * 1.4f
            7 -> _resizeView.value = resizeViewDefault * 1.6f
            else -> _resizeView.value = resizeViewDefault
        }
    }

    ////////////////////////////////

    fun sideImageNumberPaddingTop(dice: Dice): Dp {
        return when (dice.sides.size) {
            2 -> 0.dp
            4 -> (22.dp * resizeView.value.value / 100)
            6 -> 0.dp
            8 -> (28.dp * resizeView.value.value / 100)
            10 -> (20.dp * resizeView.value.value / 100)
            12 -> (18.dp * resizeView.value.value / 100)
            else -> (42.dp * resizeView.value.value / 100)
        }
    }

    fun sideImageNumberFontSize(dice: Dice): TextUnit {
        return (40.sp * resizeView.value.value / 100)
    }

    fun sideImageNumberShape(dice: Dice): Int {
        return when (dice.sides.size) {
            2 -> R.drawable.d2
            6 -> R.drawable.d6
            10 -> R.drawable.d10
            12 -> R.drawable.d12
            else -> R.drawable.d4_d8_d20
        }
    }

    fun sideColourFilter(hexColour: String): ColorFilter {
        return if (hexColour == "")
            ColorFilter.tint(Color.Black)
        else
            ColorFilter.tint(makeColour(hexColour))
    }

    fun sideColor(hexColour: String): Color {
        return if (hexColour == "")
            Color.White
        else
            makeColour(hexColour)
    }

    fun diceContainsAtLeastOneSideWithDescription(dice: Dice): Boolean {
        dice.sides.forEach {
            it.description
            if (it.descriptionStringsId != 0 || it.description != "")
                return true
        }

        return false
    }

    private fun makeColour(hexColour: String) =
        Color(android.graphics.Color.parseColor("#${hexColour}"))

    fun sideImageSVG(side: Side) =
        UtilitySvgSerializer.imageRequestFromBase64String(getApplication(), side.imageBase64)
}
