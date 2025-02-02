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
import com.github.jameshnsears.chance.ui.tab.bag.TabBagResetStorageEvent
import com.github.jameshnsears.chance.ui.utility.colour.UtilityColour
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
    var resizeView: Dp,
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
            resizeView = 0.dp,
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
                TabBagImportEvent.sharedFlowTabBagImportEvent.map { },
                TabBagResetStorageEvent.sharedFlowTabBagResetStorageEvent.map { }
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
                resizeView = resizeViewAsDp(repositorySettings.fetch().first().resize),
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

    fun resizeView(resize: Int) {
        _stateFlowZoom.value = _stateFlowZoom.value.copy(
            resizeView = resizeViewAsDp(resize)
        )
    }

    private fun resizeViewAsDp(resize: Int): Dp {
        val defaultViewSize = 80.dp

        return when (resize) {
            1 -> defaultViewSize * 0.75f
            2 -> defaultViewSize * 1.0f
            3 -> defaultViewSize * 1.25f
            4 -> defaultViewSize * 1.5f
            5 -> defaultViewSize * 1.75f
            6 -> defaultViewSize * 2.0f
            7 -> defaultViewSize * 2.25f
            8 -> defaultViewSize * 2.5f
            9 -> defaultViewSize * 2.75f
            else -> defaultViewSize
        }
    }

    fun fetchDiceFromEpochCache(rollDiceEpoch: Long): Dice? {
        return diceEpochCache[rollDiceEpoch]
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

    private val executor: ExecutorService = Executors.newFixedThreadPool(10)

    fun sideImageSVG(side: Side): ImageRequest {
        val future = CompletableFuture<ImageRequest>()
        executor.execute {
            val result = UtilitySvgSerializer.imageRequestFromBase64String(getApplication(), side)
            future.complete(result)
        }
        return future.join()
    }
}
