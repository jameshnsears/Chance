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
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.ui.tab.DisplayIndexEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

@Stable
data class ZoomState(
    val resizeViewDp: Dp,
    val diceBag: DiceBag,
    val rollHistory: RollHistory,
    val entriesList: List<Pair<Int, Map.Entry<Long, List<Roll>>>> = emptyList(),
    val firstVisibleItemIndex: Int = 0,
    val firstVisibleItemScrollOffset: Int = 0,
    val horizontalScrollPositions: Map<String, Pair<Int, Int>> = emptyMap(),
    val rollIndexTime: Boolean = false,
    val rollScore: Boolean = false,
    val rollScoreTTS: Boolean = false,
    val diceTitle: Boolean = false,
    val rollBehaviour: Boolean = false,
    val sideNumber: Boolean = true,
    val sideDescription: Boolean = false,
    val sideSVG: Boolean = true,
    val groupTitle: Boolean = false,
)

abstract class ZoomAndroidViewModel(
    application: Application,
    val repositorySettings: RepositorySettingsInterface,
    val repositoryBag: RepositoryBagInterface,
    val repositoryRoll: RepositoryRollInterface,
) : AndroidViewModel(application) {
    val tapOffset = mutableStateOf(Offset.Zero)

    private val _scrollState = MutableStateFlow(
        Triple(0, 0, emptyMap<String, Pair<Int, Int>>())
    )

    val stateFlowZoom: StateFlow<ZoomState> = combine(
        repositorySettings.fetch(),
        repositoryBag.fetch(),
        repositoryRoll.fetch().map { rollHistory ->
            val sortedHistory = LinkedHashMap<Long, List<Roll>>()
            rollHistory.keys.sortedDescending().forEach { key ->
                sortedHistory[key] = rollHistory[key]!!
            }
            sortedHistory
        },
        _scrollState
    ) { settings, diceBag, rollHistory, scrollData ->
        ZoomState(
            resizeViewDp = resizeViewAsDp(settings.resizeZoom),
            diceBag = diceBag.sortedBy { it.displayIndex }.toMutableList(),
            rollHistory = rollHistory,
            entriesList = rollHistory.entries
                .mapIndexed { index, entry -> index to entry }
                .filter { (_, entry) ->
                    isContentAvailableToDisplay(entry.value, settings)
                },
            firstVisibleItemIndex = scrollData.first,
            firstVisibleItemScrollOffset = scrollData.second,
            horizontalScrollPositions = scrollData.third,
            rollIndexTime = settings.rollIndexTime,
            rollScore = settings.rollScore,
            rollScoreTTS = settings.rollScoreTTS,
            diceTitle = settings.diceTitle,
            rollBehaviour = settings.rollBehaviour,
            sideNumber = settings.sideNumber,
            sideDescription = settings.sideDescription,
            sideSVG = settings.sideSVG,
            groupTitle = settings.groupTitle,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ZoomState(
            resizeViewDp = 0.dp,
            diceBag = mutableListOf(),
            rollHistory = LinkedHashMap()
        )
    )

    private fun isContentAvailableToDisplay(
        rolls: List<Roll>,
        settings: com.github.jameshnsears.chance.data.domain.core.settings.SettingsDataInterface
    ): Boolean {
        var svgExists = false
        var descriptionExists = false

        rolls.forEach {
            if (it.side.imageBase64.isNotEmpty() || it.side.imageDrawableId != 0)
                svgExists = true

            if (it.side.description.isNotEmpty())
                descriptionExists = true
        }

        return (settings.rollIndexTime
            ||
            settings.rollScore
            ||
            settings.rollScoreTTS
            ||
            settings.diceTitle
            ||
            settings.rollBehaviour
            ||
            settings.sideNumber
            ||
            (settings.sideDescription
                && descriptionExists
                )
            ||
            (settings.sideSVG
                && svgExists
                )
            ||
            settings.groupTitle
            )
    }

    val diceBagList: StateFlow<List<Dice>> = stateFlowZoom
        .map { it.diceBag }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getScrollPositionFlow(diceUuid: String) = stateFlowZoom
        .map { it.horizontalScrollPositions[diceUuid] ?: (0 to 0) }
        .distinctUntilChanged()

    private var diceUuidCache: MutableMap<String, Dice> = mutableMapOf()

    init {
        viewModelScope.launch {
            diceBagList.collect { list ->
                diceUuidCache.clear()
                list.forEach { dice ->
                    diceUuidCache[dice.uuid] = dice
                }
            }
        }
    }

    private fun resizeViewAsDp(resizeZoom: Float): Dp {
        val defaultViewSize = 65.dp
        return defaultViewSize * (1.0f + (resizeZoom - 1.0f) * 0.32f)
    }

    fun saveScrollPosition(index: Int, offset: Int) {
        _scrollState.update {
            it.copy(first = index, second = offset)
        }
    }

    fun saveHorizontalScrollPosition(diceUuid: String, index: Int, offset: Int) {
        _scrollState.update {
            it.copy(third = it.third + (diceUuid to (index to offset)))
        }
    }

    fun fetchDiceFromUuidCache(rollDiceUuid: String): Dice? {
        return diceUuidCache[rollDiceUuid]
    }

    fun sideNumberFontSizeSp() = 17.sp

    fun rollScoreFontSizeSp(resizeViewDp: Dp) = (36 * (resizeViewDp.value / 80f)).sp

    fun drawableForDiceSides(dice: Dice): Int {
        return when (dice.sides.size) {
            4 -> R.drawable.d4_d8_d20
            6 -> R.drawable.d6
            8 -> R.drawable.d4_d8_d20
            10 -> R.drawable.d10
            12 -> R.drawable.d12
            20 -> R.drawable.d4_d8_d20
            else -> R.drawable.d2_d1000
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

    private val imageRequestCache = ConcurrentHashMap<String, ImageRequest>()

    suspend fun sideSvgImageRequestAsync(side: Side): ImageRequest = withContext(Dispatchers.Default) {
        val cacheKey = side.imageBase64
        // Return cached result if available (fast path - no lock)
        imageRequestCache[cacheKey]?.let { return@withContext it }

        // Use the async version from UtilitySvgSerializer
        val result = UtilitySvgSerializer.imageRequestFromBase64StringAsync(getApplication(), side)
        imageRequestCache[cacheKey] = result
        result
    }

    fun move(fromIndex: Int, toIndex: Int, commit: Boolean = true) {
        if (fromIndex == toIndex) return

        val currentList = diceBagList.value.toMutableList()
        if (fromIndex in currentList.indices && toIndex in currentList.indices) {
            val item = currentList.removeAt(fromIndex)
            currentList.add(toIndex, item)

            // Update displayIndex for all items in the list
            currentList.forEachIndexed { index, dice ->
                dice.displayIndex = index
            }

            if (commit) {
                viewModelScope.launch {
                    repositoryBag.store(currentList)
                    DisplayIndexEvent.emit()
                }
            }
        }
    }

    fun moveUp(dice: Dice) {
        val currentList = diceBagList.value
        val index = currentList.indexOfFirst { it.uuid == dice.uuid }
        if (index > 0) {
            move(index, index - 1)
        }
    }

    fun moveDown(dice: Dice) {
        val currentList = diceBagList.value
        val index = currentList.indexOfFirst { it.uuid == dice.uuid }
        if (index != -1 && index < currentList.size - 1) {
            move(index, index + 1)
        }
    }
}
