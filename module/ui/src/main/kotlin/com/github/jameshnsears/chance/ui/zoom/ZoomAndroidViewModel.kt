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
import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.domain.state.DiceBag
import com.github.jameshnsears.chance.data.domain.state.RollHistory
import com.github.jameshnsears.chance.data.domain.state.Side
import com.github.jameshnsears.chance.data.domain.utility.svg.UtilitySvgSerializer
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagCloseEvent
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
    val repositoryBag: RepositoryBagInterface,
    val repositoryRoll: RepositoryRollInterface,
) : AndroidViewModel(application) {
    private val _stateFlowZoom = MutableStateFlow(
        ZoomState(
            resizeView = 80.dp,
            diceBag = mutableListOf(),
            diceBagDice = Dice(),
            rollHistory = LinkedHashMap()
        )
    )
    val stateFlowZoom: StateFlow<ZoomState> = _stateFlowZoom

    init {
        viewModelScope.launch {
            _stateFlowZoom.update {
                it.copy(
                    diceBag = repositoryBag.fetch().first(),
                    rollHistory = repositoryRoll.fetch().first()
                )
            }
        }

        viewModelScope.launch {
            DialogBagCloseEvent.sharedFlowDialogBagCloseEvent.collect {
                Timber.d("collect")

                removeRollSequenceWithDiceThatBeenDeleted()

                _stateFlowZoom.update {
                    it.copy(
                        diceBag = repositoryBag.fetch().first(),
                        rollHistory = repositoryRoll.fetch().first()
                    )
                }
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

    suspend fun removeRollSequenceWithDiceThatBeenDeleted() {
        val diceBagEpochs: MutableList<Long> = mutableListOf()
        repositoryBag.fetch().first().forEach {
            Timber.d("epoch.available=${it.epoch}")
            diceBagEpochs.add(it.epoch)
        }

        val currentRollHistory = repositoryRoll.fetch().first()
        val rollHistoryWithValidDice = RollHistory()
        currentRollHistory.keys.forEach { rollSequenceEpoch ->

            val rolls = currentRollHistory.getValue(rollSequenceEpoch)

            var diceEpochMissing = false
            rolls.forEach { roll ->
                if (!diceBagEpochs.contains(roll.diceEpoch)) {
                    Timber.d("epoch.missing=${roll.diceEpoch}")
                    diceEpochMissing = true
                }
            }

            if (!diceEpochMissing)
                rollHistoryWithValidDice[rollSequenceEpoch] = rolls
        }
        repositoryRoll.store(rollHistoryWithValidDice)
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

    fun resizeView(zoom: Int) {
        val defaultViewSize = 80.dp

        _stateFlowZoom.update {
            it.copy(
                resizeView = when (zoom) {
                    1 -> defaultViewSize * 0.75f
                    2 -> defaultViewSize * 0.8f
                    3 -> defaultViewSize * 0.9f
                    5 -> defaultViewSize * 1.1f
                    6 -> defaultViewSize * 1.2f
                    7 -> defaultViewSize * 1.25f
                    else -> defaultViewSize
                }
            )
        }
    }

    fun sideImageNumberFontSize() = 17.sp

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
