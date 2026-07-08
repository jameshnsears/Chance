package com.github.jameshnsears.chance.ui.zoom

import android.app.Application
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import coil.request.ImageRequest
import com.github.jameshnsears.chance.common.utility.colour.UtilityColour
import com.github.jameshnsears.chance.data.common.utility.svg.UtilitySvgSerializer
import com.github.jameshnsears.chance.data.domain.R
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.ui.dialog.dice.DialogDiceCloseEvent
import com.github.jameshnsears.chance.ui.tab.DisplayIndexEvent
import com.github.jameshnsears.chance.ui.tab.SetupImportEvent
import com.github.jameshnsears.chance.ui.tab.setup.dice.DiceResetEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

@Stable
data class ZoomState(
    val resizeViewDp: Dp,
    val diceBag: DiceBag,
    val rollHistory: RollHistory,
    val firstVisibleItemIndex: Int = 0,
    val firstVisibleItemScrollOffset: Int = 0,
    val horizontalScrollPositions: Map<String, Pair<Int, Int>> = emptyMap(),
)

abstract class ZoomAndroidViewModel(
    application: Application,
    val repositorySettings: RepositorySettingsInterface,
    val repositoryBag: RepositoryBagInterface,
    val repositoryRoll: RepositoryRollInterface,
) : AndroidViewModel(application) {
    val tapOffset = mutableStateOf(Offset.Zero)

    protected val _stateFlowZoom = MutableStateFlow(
        ZoomState(
            resizeViewDp = 0.dp,
            diceBag = mutableListOf(),
            rollHistory = LinkedHashMap()
        )
    )
    val stateFlowZoom: StateFlow<ZoomState> = _stateFlowZoom

    private val _diceBagList = MutableStateFlow<List<Dice>>(emptyList())
    val diceBagList: StateFlow<List<Dice>> = _diceBagList

    private var diceUuidCache: MutableMap<String, Dice> = mutableMapOf()

    init {
        viewModelScope.launch {
            DialogDiceCloseEvent.sharedFlowDialogBagCloseEvent.collect {
                Timber.d("collect.DialogDiceCloseEvent")
                updateStateFlowZoom()
            }
        }

        viewModelScope.launch {
            merge(
                SetupImportEvent.sharedFlowTabBagImportEvent.map { },
                DiceResetEvent.sharedFlowTabBagResetEvent.map { },
                DisplayIndexEvent.sharedFlowDisplayIndexEvent.map { }
            ).collect {
                Timber.d("collect.SetupImportEvent|DiceResetEvent|DisplayIndexEvent")
                updateResize()
                updateStateFlowZoom()
            }
        }
    }

    protected suspend fun updateResize() {
        val settings = repositorySettings.fetch().firstOrNull()
        if (settings != null) {
            _stateFlowZoom.update {
                it.copy(
                    resizeViewDp = resizeViewAsDp(settings.resizeZoom),
                )
            }
        }
    }

    open suspend fun updateStateFlowZoom() {}

    protected suspend fun updateDiceBagList(diceBagToUse: DiceBag? = null) {
        diceUuidCache.clear()

        var diceBag = diceBagToUse ?: _stateFlowZoom.value.diceBag
        if (diceBag.isEmpty()) {
            val fetchedDiceBag = repositoryBag.fetch().firstOrNull()
            if (fetchedDiceBag != null) {
                if (diceBagToUse == null) {
                    _stateFlowZoom.update {
                        it.copy(
                            diceBag = fetchedDiceBag.sortedBy { it.displayIndex }.toMutableList()
                        )
                    }
                }
                diceBag = fetchedDiceBag.sortedBy { it.displayIndex }.toMutableList()
            }
        } else {
            diceBag = diceBag.sortedBy { it.displayIndex }.toMutableList()
        }

        // Build list more efficiently - avoid repeated list creation via +=
        val newDiceList = mutableListOf<Dice>()
        for (dice in diceBag) {
            newDiceList.add(dice)
            diceUuidCache[dice.uuid] = dice
        }
        _diceBagList.value = newDiceList
    }

    fun refreshAfterImport() {
        viewModelScope.launch {
            val diceBag = repositoryBag.fetch().firstOrNull()
            val rollHistory = repositoryRoll.fetch().firstOrNull()

            updateDiceBagList(diceBag)

            _stateFlowZoom.update {
                it.copy(
                    diceBag = diceBag ?: mutableListOf(),
                    rollHistory = rollHistory ?: LinkedHashMap()
                )
            }
        }
    }

    fun setResizeView(resizeZoom: Float) {
        _stateFlowZoom.value = _stateFlowZoom.value.copy(
            resizeViewDp = resizeViewAsDp(resizeZoom)
        )
    }

    private fun resizeViewAsDp(resizeZoom: Float): Dp {
        val defaultViewSize = 65.dp
        return defaultViewSize * (1.0f + (resizeZoom - 1.0f) * 0.32f)
    }

    fun saveScrollPosition(index: Int, offset: Int) {
        _stateFlowZoom.update {
            it.copy(
                firstVisibleItemIndex = index,
                firstVisibleItemScrollOffset = offset
            )
        }
    }

    fun saveHorizontalScrollPosition(diceUuid: String, index: Int, offset: Int) {
        _stateFlowZoom.update {
            it.copy(
                horizontalScrollPositions = it.horizontalScrollPositions + (diceUuid to (index to offset))
            )
        }
    }

    fun fetchDiceFromUuidCache(rollDiceUuid: String): Dice? {
        return diceUuidCache[rollDiceUuid]
    }

    fun sideNumberFontSizeSp() = 17.sp

    fun rollScoreFontSizeSp(resizeViewDp: Dp) = (36 * (resizeViewDp.value / 80f)).sp

    fun drawableForDiceSides(dice: Dice): Int {
        return when (dice.sides.size) {
            2 -> R.drawable.d2
            6 -> R.drawable.d6
            10 -> R.drawable.d10
            12 -> R.drawable.d12
            else -> R.drawable.d4_d8_d20
        }
    }

    fun sideColorFilter(hexColor: String): ColorFilter {
        return if (hexColor == "")
            ColorFilter.tint(Color.Black)
        else
            ColorFilter.tint(UtilityColour.makeColor(hexColor))
    }

    fun sideColor(hexColor: String): Color {
        return if (hexColor == "")
            Color.White
        else
            UtilityColour.makeColor(hexColor)
    }

    fun hasSideWithDescription(dice: Dice): Boolean {
        return dice.sides.any { it.description.isNotBlank() }
    }

    private val imageRequestCache = mutableMapOf<String, ImageRequest>()

    fun sideSvgImageRequest(side: Side): ImageRequest {
        val cacheKey = side.imageBase64
        // Return cached result if available (fast path - no lock)
        imageRequestCache[cacheKey]?.let { return it }

        // For synchronous API, create result and cache it
        val result = UtilitySvgSerializer.imageRequestFromBase64String(getApplication(), side)
        imageRequestCache[cacheKey] = result
        return result
    }

    fun move(fromIndex: Int, toIndex: Int, commit: Boolean = true) {
        if (fromIndex == toIndex) return

        val currentList = _diceBagList.value.toMutableList()
        if (fromIndex in currentList.indices && toIndex in currentList.indices) {
            val item = currentList.removeAt(fromIndex)
            currentList.add(toIndex, item)

            // Update displayIndex for all items in the list
            currentList.forEachIndexed { index, dice ->
                dice.displayIndex = index
            }

            _diceBagList.value = currentList
            _stateFlowZoom.update { it.copy(diceBag = currentList) }

            if (commit) {
                viewModelScope.launch {
                    repositoryBag.store(currentList)
                    DisplayIndexEvent.emit()
                }
            }
        }
    }

    fun moveUp(dice: Dice) {
        val currentList = _diceBagList.value
        val index = currentList.indexOfFirst { it.uuid == dice.uuid }
        if (index > 0) {
            move(index, index - 1)
        }
    }

    fun moveDown(dice: Dice) {
        val currentList = _diceBagList.value
        val index = currentList.indexOfFirst { it.uuid == dice.uuid }
        if (index != -1 && index < currentList.size - 1) {
            move(index, index + 1)
        }
    }
}
