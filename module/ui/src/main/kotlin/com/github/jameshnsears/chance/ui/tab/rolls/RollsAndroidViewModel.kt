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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import java.security.SecureRandom

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
)

class RollsAndroidViewModel(
    application: Application,
    val repositorySettings: RepositorySettingsInterface,
    val repositoryBag: RepositoryBagInterface,
    val repositoryRoll: RepositoryRollInterface,
    val repositoryGroup: RepositoryGroupInterface,
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
                groupTitle = true,
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
        rollHistory.isNotEmpty() && !isSettingsNotEnabled(settings)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val rollEnabled: StateFlow<Boolean> = combine(
        diceBag,
        groupHistory,
        stateFlowSettings
    ) { diceBag, groupHistory, settings ->
        (diceBag.any { it.selected } || groupHistory.any { it.selected }) && !isSettingsNotEnabled(settings)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val rollSelectionRowScrollState = LazyListState()

    private val _sideEffectFlow = MutableSharedFlow<RollSideEffect>()
    val sideEffectFlow: SharedFlow<RollSideEffect> = _sideEffectFlow.asSharedFlow()

    private val mutex = Mutex()

    private val secureRandom = SecureRandom()

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

    val rollsSequenceHelper = RollsSequenceHelper(repositoryRoll)

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
        Timber.d("uuid=${dice.uuid}; selected=${selected}")

        viewModelScope.launch {
            mutex.withLock {
                val updatedDiceBag: DiceBag = mutableListOf()

                diceBag.value.forEach {
                    val existingDice = it.copy()

                    if (dice.uuid == it.uuid) {
                        existingDice.selected = selected
                    }

                    updatedDiceBag.add(existingDice)
                }

                repositoryBag.store(updatedDiceBag)
            }
        }
    }

    fun markGroupAsSelected(group: Group) {
        Timber.d("group.uuid=${group.uuid}")

        viewModelScope.launch {
            mutex.withLock {
                val updatedHistory = groupHistory.value.map {
                    if (it.uuid == group.uuid) it.copy(selected = !it.selected) else it
                }
                repositoryGroup.store(updatedHistory)
            }
        }
    }

    private fun isSettingsNotEnabled(settings: SettingsState) = (
        !settings.rollIndexTime
            && !settings.rollScore
            && !settings.diceTitle
            && !settings.rollBehaviour
            && !settings.sideNumber
            && !settings.sideDescription
            && !settings.sideSVG
            && !settings.groupTitle
        )

    fun rollDiceSequence() {
        viewModelScope.launch {
            if (stateFlowSettings.value.haptics) {
                _sideEffectFlow.emit(RollSideEffect.RollHaptic)
            }
            if (stateFlowSettings.value.rollSound) {
                _sideEffectFlow.emit(RollSideEffect.RollSound)
            }

            val newRollSequence = mutableListOf<Roll>()
            generateRollDiceSequence(newRollSequence)
            shuffleRollSequence(newRollSequence)

            rollsSequenceHelper.saveNewRollSequence(newRollSequence)

            RollsEvent.emit()
            // States will be updated by repository collectors
        }
    }

    fun shuffleRollSequence(rollSequence: MutableList<Roll>) {
        if (stateFlowSettings.value.shuffle) {
            if (rollSequence.size > 1) {
                rollSequence.shuffle()

                // order the multipleIndex into ASC order for any dice cluster
                rollSequence
                    .groupBy { it.uuidDice }
                    .forEach { (_, rolls) ->
                        rolls.forEachIndexed { index, roll ->
                            roll.multiplierIndex = index + 1
                        }
                    }
            }
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

    fun generateRollDiceSequence(newRollSequence: MutableList<Roll>) {
        val diceToRoll = getDiceToRoll()
        val diceCountMap = mutableMapOf<String, Int>()

        diceToRoll.forEach { (dice, uuidGroup) ->
            val count = diceCountMap.getOrDefault(dice.uuid, 0)
            newRollSequence.addAll(rollDice(dice, count * dice.multiplierValue, uuidGroup))
            diceCountMap[dice.uuid] = count + 1
        }
    }

    private fun getDiceToRoll(): List<Pair<Dice, String>> {
        val selectedDice = diceBag.value.filter { it.selected }.map { it to "" }

        val diceMap = diceBag.value.associateBy { it.uuid }
        val groupDice = groupHistory.value
            .filter { it.selected }
            .flatMap { group ->
                group.uuidDice.mapNotNull { diceMap[it]?.to(group.uuid) }
            }

        return selectedDice + groupDice
    }

    private fun rollDice(dice: Dice, indexOffset: Int = 0, uuidGroup: String = ""): List<Roll> {
        val diceRolls = mutableListOf<Roll>()
        for (indexMultiplier in 1..dice.multiplierValue) {
            var randomSide = randomSide(dice)
            diceRolls.add(
                Roll(
                    uuidDice = dice.uuid,
                    side = randomSide,
                    multiplierIndex = indexMultiplier + indexOffset,
                    score = randomSide.number,
                    uuidGroup = uuidGroup
                )
            )

            if (dice.explode) {
                var indexExplode = 0
                val explosionDepth = 5
                while (indexExplode < explosionDepth && rollsSequenceHelper.diceCanExplode(dice, randomSide)) {
                    indexExplode++
                    randomSide = randomSide(dice)
                    diceRolls.add(
                        Roll(
                            uuidDice = dice.uuid,
                            side = randomSide,
                            multiplierIndex = indexMultiplier,
                            explodeIndex = indexExplode,
                            score = randomSide.number,
                            uuidGroup = uuidGroup
                        )
                    )
                }
            }
        }

        if (dice.modifyScore && diceRolls.isNotEmpty()) {
            val lastRoll = diceRolls.last()
            lastRoll.scoreAdjustment = dice.modifyScoreValue
            lastRoll.score += dice.modifyScoreValue
        }

        return diceRolls
    }

    fun randomSide(dice: Dice) = dice.sides[secureRandom.nextInt(dice.sides.size)]

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

    fun settingsIndexTime(checked: Boolean) {
        viewModelScope.launch {
            val settings = repositorySettings.fetch().first()
            settings.rollIndexTime = checked
            repositorySettings.store(settings)
        }
    }

    fun settingsRollScore(checked: Boolean) {
        viewModelScope.launch {
            val settings = repositorySettings.fetch().first()
            settings.rollScore = checked
            repositorySettings.store(settings)
        }
    }

    fun settingsRollScoreTTS(checked: Boolean) {
        viewModelScope.launch {
            val settings = repositorySettings.fetch().first()
            settings.rollScoreTTS = checked
            repositorySettings.store(settings)
        }
    }

    fun settingsDiceTitle(checked: Boolean) {
        viewModelScope.launch {
            val settings = repositorySettings.fetch().first()
            settings.diceTitle = checked
            repositorySettings.store(settings)
        }
    }

    fun settingsSideNumber(checked: Boolean) {
        viewModelScope.launch {
            val settings = repositorySettings.fetch().first()
            settings.sideNumber = checked
            repositorySettings.store(settings)
        }
    }

    fun settingsSideDescription(checked: Boolean) {
        viewModelScope.launch {
            val settings = repositorySettings.fetch().first()
            settings.sideDescription = checked
            repositorySettings.store(settings)
        }
    }

    fun settingsSideSVG(checked: Boolean) {
        viewModelScope.launch {
            val settings = repositorySettings.fetch().first()
            settings.sideSVG = checked
            repositorySettings.store(settings)
        }
    }

    fun settingsBehaviour(checked: Boolean) {
        viewModelScope.launch {
            val settings = repositorySettings.fetch().first()
            settings.rollBehaviour = checked
            repositorySettings.store(settings)
        }
    }

    fun settingsGroupTitle(checked: Boolean) {
        viewModelScope.launch {
            val settings = repositorySettings.fetch().first()
            settings.groupTitle = checked
            repositorySettings.store(settings)
        }
    }

    fun settingsShuffle(checked: Boolean) {
        viewModelScope.launch {
            val settings = repositorySettings.fetch().first()
            settings.shuffle = checked
            repositorySettings.store(settings)
        }
    }

    fun settingsUseHaptics(checked: Boolean) {
        viewModelScope.launch {
            val settings = repositorySettings.fetch().first()
            settings.haptics = checked
            repositorySettings.store(settings)
        }
    }

    fun settingsShakeToRoll(checked: Boolean) {
        viewModelScope.launch {
            val settings = repositorySettings.fetch().first()
            settings.shakeToRoll = checked
            repositorySettings.store(settings)
        }
    }

    fun settingsRollSound(checked: Boolean) {
        viewModelScope.launch {
            val settings = repositorySettings.fetch().first()
            settings.rollSound = checked
            repositorySettings.store(settings)
        }
    }

    fun isContentAvailableToDisplay(rolls: List<Roll>, settings: SettingsState): Boolean {
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
            (settings.sideDescription && descriptionExists)
            ||
            (settings.sideSVG && svgExists)
            ||
            settings.groupTitle
            )
    }
}
