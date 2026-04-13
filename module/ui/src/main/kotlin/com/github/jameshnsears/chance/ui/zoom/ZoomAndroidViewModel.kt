package com.github.jameshnsears.chance.ui.zoom

import android.app.Application
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
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagCloseEvent
import com.github.jameshnsears.chance.ui.tab.bag.BagImportEvent
import com.github.jameshnsears.chance.ui.tab.bag.BagResetEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

data class ZoomState(
    var resizeViewDp: Dp,
    var diceBag: DiceBag,
    var rollHistory: RollHistory
)

abstract class ZoomAndroidViewModel(
    application: Application,
    val repositorySettings: RepositorySettingsInterface,
    val repositoryBag: RepositoryBagInterface,
    val repositoryRoll: RepositoryRollInterface,
) : AndroidViewModel(application) {
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

    private var diceEpochCache: MutableMap<Long, Dice> = mutableMapOf()

    init {
        viewModelScope.launch {
            DialogBagCloseEvent.sharedFlowDialogBagCloseEvent.collect {
                Timber.d("collect.DialogBagCloseEvent")
                updateStateFlowZoom()
            }
        }

        viewModelScope.launch {
            merge(
                BagImportEvent.sharedFlowTabBagImportEvent.map { },
                BagResetEvent.sharedFlowTabBagResetEvent.map { }
            ).collect {
                Timber.d("collect.TabBagImportEvent|TabBagResetStorageEvent.TabRollAndroidViewModel")
                updateResize()
                updateStateFlowZoom()
            }
        }
    }

    protected suspend fun updateResize() {
        _stateFlowZoom.update {
            it.copy(
                resizeViewDp = resizeViewAsDp(repositorySettings.fetch().first().resize),
            )
        }
    }

    open suspend fun updateStateFlowZoom() {}

    protected fun updateDiceBagList() {
        viewModelScope.launch {
            _diceBagList.value = emptyList()
            diceEpochCache.clear()

            if (_stateFlowZoom.value.diceBag.size == 0) {
                _stateFlowZoom.value.diceBag = repositoryBag.fetch().first()
            }

            for (dice in _stateFlowZoom.value.diceBag) {
                _diceBagList.value += dice
                diceEpochCache[dice.epoch] = dice
            }
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

            updateDiceBagList()
        }
    }

    fun setResizeView(resize: Int) {
        _stateFlowZoom.value = _stateFlowZoom.value.copy(
            resizeViewDp = resizeViewAsDp(resize)
        )
    }

    private fun resizeViewAsDp(resize: Int): Dp {
        val defaultViewSize = 80.dp

        return when (resize) {
            1 -> defaultViewSize * 1.0f
            2 -> defaultViewSize * 1.25f
            3 -> defaultViewSize * 1.5f
            4 -> defaultViewSize * 1.75f
            5 -> defaultViewSize * 2.0f
            6 -> defaultViewSize * 2.25f
            7 -> defaultViewSize * 2.5f
            8 -> defaultViewSize * 2.75f
            9 -> defaultViewSize * 3.0f
            else -> defaultViewSize
        }
    }

    fun fetchDiceFromEpochCache(rollDiceEpoch: Long): Dice? {
        return diceEpochCache[rollDiceEpoch]
    }

    fun sideNumberFontSizeSp() = 17.sp

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

    private val executor: ExecutorService = Executors.newFixedThreadPool(10)
    private val imageRequestCache = mutableMapOf<String, ImageRequest>()

    fun sideSvgImageRequest(side: Side): ImageRequest {
        val cacheKey = side.imageBase64
        return imageRequestCache.getOrPut(cacheKey) {
            val future = CompletableFuture<ImageRequest>()
            executor.execute {
                val result = UtilitySvgSerializer.imageRequestFromBase64String(getApplication(), side)
                future.complete(result)
            }
            future.join()
        }
    }
}
