package com.github.jameshnsears.chance.ui.tab.rolls

import android.app.Application
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Stable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.jameshnsears.chance.data.domain.core.group.GroupHistory
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.settings.SettingsDataInterface
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.group.RepositoryGroupInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class RollSideEffect {
    object RollHaptic : RollSideEffect()
    object RollSound : RollSideEffect()
    data class ScoreTTS(val score: Int) : RollSideEffect()
    object UndoHaptic : RollSideEffect()
    object UndoAllHaptic : RollSideEffect()
}

@Stable
data class SettingsState(
    val rollIndexTime: Boolean,
    val rollScore: Boolean,
    val rollScoreTTS: Boolean,
    val diceTitle: Boolean,
    val rollBehaviour: Boolean,
    val sideNumber: Boolean,
    val sideDescription: Boolean,
    val sideSVG: Boolean,
    val shuffle: Boolean,
    val haptics: Boolean,
    val shakeToRoll: Boolean,
    val rollSound: Boolean,
    val groupTitle: Boolean,
    val layout: Boolean,
    val history: Boolean,
) {
    fun isSettingsNotEnabled() = (
        !rollIndexTime
            && !rollScore
            && !diceTitle
            && !rollBehaviour
            && !sideNumber
            && !sideDescription
            && !sideSVG
            && !groupTitle
            && !layout
            && !history
        )
}

class RollsAndroidViewModel(
    application: Application,
    val repositorySettings: RepositorySettingsInterface,
    val repositoryBag: RepositoryBagInterface,
    val repositoryRoll: RepositoryRollInterface,
    val repositoryGroup: RepositoryGroupInterface,
    private val rollsSelectionHelper: RollsSelectionHelper,
    val rollsCoreHelper: RollsCoreHelper
) : AndroidViewModel(application) {
    val stateFlowSettings: StateFlow<SettingsState> = repositorySettings.fetch()
        .map { settings ->
            SettingsState(
                rollIndexTime = settings.rollIndexTime,
                rollScore = settings.rollScore,
                rollScoreTTS = settings.rollScoreTTS,
                diceTitle = settings.diceTitle,
                rollBehaviour = settings.rollBehaviour,
                sideNumber = settings.sideNumber,
                sideDescription = settings.sideDescription,
                sideSVG = settings.sideSVG,
                haptics = settings.haptics,
                shakeToRoll = settings.shakeToRoll,
                rollSound = settings.rollSound,
                shuffle = settings.shuffle,
                groupTitle = settings.groupTitle,
                layout = settings.layout,
                history = settings.history,
            )
        }.distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsState(
                rollIndexTime = false,
                rollScore = false,
                rollScoreTTS = false,
                diceTitle = false,
                rollBehaviour = false,
                sideNumber = false,
                sideDescription = false,
                sideSVG = false,
                shuffle = false,
                haptics = false,
                shakeToRoll = false,
                rollSound = false,
                groupTitle = false,
                layout = true,
                history = false,
            )
        )

    val diceBag: StateFlow<DiceBag> = repositoryBag.fetch()
        .map { it.sortedBy { dice -> dice.displayIndex }.toMutableList() }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = mutableListOf()
        )

    val groupHistory: StateFlow<GroupHistory> = repositoryGroup.fetch()
        .map { it.sortedBy { group -> group.displayIndex } }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    private val _rollHistoryFlow = repositoryRoll.fetch()
        .distinctUntilChanged()

    val undoEnabled: StateFlow<Boolean> = combine(
        _rollHistoryFlow,
        stateFlowSettings
    ) { rollHistory, settings ->
        rollHistory.isNotEmpty() && !settings.isSettingsNotEnabled()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val rollEnabled: StateFlow<Boolean> = combine(
        diceBag,
        groupHistory,
        stateFlowSettings
    ) { diceBag, groupHistory, settings ->
        (diceBag.any { it.selected } || groupHistory.any { it.selected }) && !settings.isSettingsNotEnabled()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val rollSelectionRowScrollState = LazyListState()

    private val _sideEffectFlow = MutableSharedFlow<RollSideEffect>()
    val sideEffectFlow: SharedFlow<RollSideEffect> = _sideEffectFlow.asSharedFlow()

    private var isForeground = false

    private val lifecycleEventObserver = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                isForeground = true
                updateShakeService()
            }

            Lifecycle.Event.ON_STOP -> {
                isForeground = false
                updateShakeService()
            }

            else -> {}
        }
    }

    private val shakeToRollService = ShakeToRollService(getApplication()) {
        if (stateFlowSettings.value.shakeToRoll && rollEnabled.value) {
            rollDiceSequence()
        }
    }

    init {
        if (System.getProperty("isUnitTest") != "true") {
            ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleEventObserver)
        }

        viewModelScope.launch {
            stateFlowSettings.collect {
                updateShakeService()
            }
        }
    }

    fun markDiceAsSelected(dice: Dice, selected: Boolean) {
        rollsSelectionHelper.markDiceAsSelected(dice, selected, diceBag.value, viewModelScope)
    }

    fun markGroupAsSelected(group: Group) {
        rollsSelectionHelper.markGroupAsSelected(group, groupHistory.value, viewModelScope)
    }

    fun rollDiceSequence() {
        viewModelScope.launch {
            if (stateFlowSettings.value.haptics) {
                _sideEffectFlow.emit(RollSideEffect.RollHaptic)
            }
            if (stateFlowSettings.value.rollSound) {
                _sideEffectFlow.emit(RollSideEffect.RollSound)
            }

            val newRollSequence = mutableListOf<Roll>()
            rollsCoreHelper.generateRollDiceSequence(diceBag.value, groupHistory.value, newRollSequence)
            rollsCoreHelper.shuffleRollSequence(newRollSequence, stateFlowSettings.value.shuffle)

            rollsCoreHelper.saveNewRollSequence(newRollSequence)

            RollsEvent.emit()
        }
    }

    fun playScoreTTS(score: Int) {
        if (stateFlowSettings.value.rollScoreTTS) {
            viewModelScope.launch {
                _sideEffectFlow.emit(RollSideEffect.ScoreTTS(score))
            }
        }
    }

    override fun onCleared() {
        if (System.getProperty("isUnitTest") != "true") {
            ProcessLifecycleOwner.get().lifecycle.removeObserver(lifecycleEventObserver)
        }
        shakeToRollService.stop()
    }

    private fun updateShakeService() {
        if (isForeground && stateFlowSettings.value.shakeToRoll) {
            shakeToRollService.start()
        } else {
            shakeToRollService.stop()
        }
    }

    fun undo() {
        viewModelScope.launch {
            if (undoEnabled.value) {
                if (stateFlowSettings.value.haptics) {
                    _sideEffectFlow.emit(RollSideEffect.UndoHaptic)
                }

                repositoryRoll.removeLatest()

                RollsEvent.emit()
                // States will be updated by repository collectors
            }
        }
    }

    fun undoAll() {
        viewModelScope.launch {
            if (stateFlowSettings.value.haptics) {
                _sideEffectFlow.emit(RollSideEffect.UndoAllHaptic)
            }

            repositoryRoll.clear()
            RollsEvent.emit()
        }
    }

    private fun updateSettings(update: (SettingsDataInterface) -> Unit) {
        viewModelScope.launch {
            val settings = repositorySettings.fetch().first()
            update(settings)
            repositorySettings.store(settings)
        }
    }

    fun settingsIndexTime(checked: Boolean) = updateSettings { it.rollIndexTime = checked }
    fun settingsRollScore(checked: Boolean) = updateSettings { it.rollScore = checked }
    fun settingsRollScoreTTS(checked: Boolean) = updateSettings { it.rollScoreTTS = checked }
    fun settingsDiceTitle(checked: Boolean) = updateSettings { it.diceTitle = checked }
    fun settingsSideNumber(checked: Boolean) = updateSettings { it.sideNumber = checked }
    fun settingsSideDescription(checked: Boolean) = updateSettings { it.sideDescription = checked }
    fun settingsSideSVG(checked: Boolean) = updateSettings { it.sideSVG = checked }
    fun settingsBehaviour(checked: Boolean) = updateSettings { it.rollBehaviour = checked }
    fun settingsGroupTitle(checked: Boolean) = updateSettings { it.groupTitle = checked }
    fun settingsLayout(checked: Boolean) = updateSettings { it.layout = checked }
    fun settingsHistory(checked: Boolean) = updateSettings { it.history = checked }
    fun settingsShuffle(checked: Boolean) = updateSettings { it.shuffle = checked }
    fun settingsUseHaptics(checked: Boolean) = updateSettings { it.haptics = checked }
    fun settingsShakeToRoll(checked: Boolean) = updateSettings { it.shakeToRoll = checked }
    fun settingsRollSound(checked: Boolean) = updateSettings { it.rollSound = checked }
}
