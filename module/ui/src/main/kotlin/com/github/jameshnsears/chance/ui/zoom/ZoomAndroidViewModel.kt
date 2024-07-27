package com.github.jameshnsears.chance.ui.zoom

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.R
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.domain.utility.svg.UtilitySvgSerializer
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagCloseEvent
import com.github.jameshnsears.chance.ui.tab.bag.TabBagImportEvent
import com.github.jameshnsears.chance.ui.tab.roll.TabRollEvent
import com.github.jameshnsears.chance.ui.utility.colour.UtilityColour
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

data class ZoomState(
    var resizeView: Dp,
    var diceBag: DiceBag,
    var diceBagDice: Dice,
    var rollHistory: RollHistory
)

class ZoomAndroidViewModel(
    application: Application,
    val repositorySettings: RepositorySettingsInterface,
    val repositoryBag: RepositoryBagInterface,
    val repositoryRoll: RepositoryRollInterface,
) : AndroidViewModel(application) {
    private val _stateFlowZoom = MutableStateFlow(
        ZoomState(
            resizeView = 0.dp,
            diceBag = mutableListOf(),
            diceBagDice = Dice(),
            rollHistory = LinkedHashMap()
        )
    )
    val stateFlowZoom: StateFlow<ZoomState> = _stateFlowZoom

    init {
        viewModelScope.launch {
            updateStateFlowZoom()
        }

        viewModelScope.launch {
            DialogBagCloseEvent.sharedFlowDialogBagCloseEvent.collect {
                Timber.d("collect")

                updateStateFlowZoom()
            }
        }

        viewModelScope.launch {
            TabBagImportEvent.sharedFlowTabBagImportEvent.collect {
                Timber.d("collect")

                updateStateFlowZoom()
            }
        }

        viewModelScope.launch {
            TabRollEvent.sharedFlowTabRollEvent.collect {
                Timber.d("collect")
                _stateFlowZoom.update {
                    it.copy(
                        rollHistory = repositoryRoll.fetch().first()
                    )
                }
            }
        }
    }

    private suspend fun updateStateFlowZoom() {
        _stateFlowZoom.update {
            it.copy(
                resizeView = resizeViewAsDp(repositorySettings.fetch().first().resize),
                diceBag = repositoryBag.fetch().first(),
                rollHistory = repositoryRoll.fetch().first()
            )
        }
    }

    fun refreshAfterImport() {
        viewModelScope.launch {
            _stateFlowZoom.update {
                it.copy(
                    diceBag = repositoryBag.fetch().first(),
                    rollHistory = repositoryRoll.fetch().first()
                )
            }
        }
    }

    fun resizeView(resize: Int) {
        _stateFlowZoom.value = _stateFlowZoom.value.copy(
            resizeView = resizeViewAsDp(resize)
        )
    }

    private fun resizeViewAsDp(resize: Int): Dp {
        val defaultViewSize = 80.dp

        return when (resize) {
            1 -> defaultViewSize * 0.75f
            2 -> defaultViewSize * 0.8f
            3 -> defaultViewSize * 0.9f
            5 -> defaultViewSize * 1.1f
            6 -> defaultViewSize * 1.2f
            7 -> defaultViewSize * 1.25f
            else -> defaultViewSize
        }
    }

    fun sideImageShapeNumberFontSize() = 17.sp

    fun sideImageShapeNumberShape(dice: Dice): Int {
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
            ColorFilter.tint(UtilityColour.makeColor(hexColour))
    }

    fun sideColor(hexColour: String): Color {
        return if (hexColour == "")
            Color.White
        else
            UtilityColour.makeColor(hexColour)
    }

    fun diceContainsAtLeastOneSideWithDescription(dice: Dice): Boolean {
        dice.sides.forEach {
            it.description
            if (it.description != "")
                return true
        }

        return false
    }

    fun sideImageSVG(side: Side) =
        UtilitySvgSerializer.imageRequestFromBase64String(getApplication(), side.imageBase64)
}
