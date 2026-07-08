package com.github.jameshnsears.chance.ui.tab.rolls

import android.app.Application
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
import com.github.jameshnsears.chance.ui.dialog.dice.DialogDiceCloseEvent
import com.github.jameshnsears.chance.ui.tab.DisplayIndexEvent
import com.github.jameshnsears.chance.ui.tab.GroupEvent
import com.github.jameshnsears.chance.ui.tab.HapticHelper
import com.github.jameshnsears.chance.ui.tab.SetupImportEvent
import com.github.jameshnsears.chance.ui.tab.setup.dice.DiceResetEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import java.security.SecureRandom
import kotlin.time.Duration.Companion.milliseconds

@Stable
data class SettingsState(
    var rollIndexTime: Boolean,
    var rollScore: Boolean,
    var rollScoreTTS: Boolean,

    var diceTitle: Boolean,
    var rollBehaviour: Boolean,
    var sideNumber: Boolean,
    var sideDescription: Boolean,
    var sideSVG: Boolean,

    var shuffle: Boolean,
    var haptics: Boolean,
    var shakeToRoll: Boolean,
    var rollSound: Boolean,

    var groupTitle: Boolean,
)

class RollsAndroidViewModel(
    application: Application,
    val repositorySettings: RepositorySettingsInterface,
    val repositoryBag: RepositoryBagInterface,
    val repositoryRoll: RepositoryRollInterface,
    val repositoryGroup: RepositoryGroupInterface,
) : AndroidViewModel(application) {
    val _stateFlowSettingsData = MutableStateFlow(
        SettingsState(
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
        )
    )
    val stateFlowSettings: StateFlow<SettingsState> = _stateFlowSettingsData

    private var _diceBag: MutableStateFlow<DiceBag> = MutableStateFlow(mutableListOf())
    var diceBag: StateFlow<DiceBag> = _diceBag

    private var _groupHistory: MutableStateFlow<GroupHistory> = MutableStateFlow(emptyList())
    var groupHistory: StateFlow<GroupHistory> = _groupHistory

    var _undoEnabled = MutableStateFlow(false)
    var undoEnabled: StateFlow<Boolean> = _undoEnabled

    private var _rollEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var rollEnabled: StateFlow<Boolean> = _rollEnabled

    private val mutex = Mutex()

    private val secureRandom = SecureRandom()

    val hapticHelper = HapticHelper(getApplication())

    private val rollsSoundPlayer = RollsSoundPlayer(getApplication())

    private val rollsScoreTtsPlayer = RollsScoreTtsPlayer(getApplication())

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
        if (_stateFlowSettingsData.value.shakeToRoll && _rollEnabled.value) {
            rollDiceSequence()
        }
    }

    val rollsSequenceHelper = RollsSequenceHelper(repositoryRoll)

    init {
        if (System.getProperty("isUnitTest") != "true") {
            ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleEventObserver)
        }

        viewModelScope.launch {
            alignSettings()
            alignBottomSheetWithStorage()
        }

        viewModelScope.launch {
            repositoryGroup.fetch().collect {
                _groupHistory.value = it.sortedBy { group -> group.displayIndex }
            }
        }

        viewModelScope.launch {
            stateFlowSettings.collect {
                updateShakeService()
            }
        }

        viewModelScope.launch {
            DialogDiceCloseEvent.sharedFlowDialogBagCloseEvent.collect {
                Timber.d("collect.DialogDiceCloseEvent")
                alignBottomSheetWithStorage()
            }
        }

        viewModelScope.launch {
            merge(
                SetupImportEvent.sharedFlowTabBagImportEvent.map { },
                DiceResetEvent.sharedFlowTabBagResetEvent.map { },
                DisplayIndexEvent.sharedFlowDisplayIndexEvent.map { },
                GroupEvent.sharedFlowGroupEvent.map { }
            ).collect {
                Timber.d("collect.SetupImportEvent|DiceResetEvent|DisplayIndexEvent|GroupEvent")
                alignSettings()
                alignBottomSheetWithStorage()
            }
        }
    }

    private suspend fun alignSettings() {
        val settings = repositorySettings.fetch().first()

        _stateFlowSettingsData.update {
            it.copy(
                rollIndexTime = settings.rollIndexTime,
                rollScore = settings.rollScore,
                rollScoreTTS = settings.rollScoreTTS,
                diceTitle = settings.diceTitle,
                sideNumber = settings.sideNumber,
                rollBehaviour = settings.rollBehaviour,
                sideDescription = settings.sideDescription,
                sideSVG = settings.sideSVG,
                haptics = settings.haptics,
                shakeToRoll = settings.shakeToRoll,
                rollSound = settings.rollSound,
                shuffle = settings.shuffle,
                groupTitle = settings.groupTitle,
            )
        }
    }

    private suspend fun alignBottomSheetWithStorage() {
        _undoEnabled.value = isUndoPossible()
        Timber.d("_undoEnabled.value=${_undoEnabled.value}")

        _rollEnabled.value = isRollPossible()
        Timber.d("_rollEnabled.value=${_rollEnabled.value}")
    }

    private suspend fun isUndoPossible(): Boolean {
        if (isSettingsNotEnabled())
            return false

        if (repositoryRoll.fetch().first().isNotEmpty())
            return true

        return false
    }

    private suspend fun isRollPossible(): Boolean {
        if (isSettingsNotEnabled())
            return false

        _diceBag.value = repositoryBag.fetch().first().sortedBy { it.displayIndex }.toMutableList()

        return _diceBag.value.any { it.selected } || _groupHistory.value.any { it.selected }
    }


    fun markDiceAsSelected(dice: Dice, selected: Boolean) {
        Timber.d("uuid=${dice.uuid}; selected=${selected}")

        viewModelScope.launch {
            mutex.withLock {
                val updatedDiceBag: DiceBag = mutableListOf()

                _diceBag.value.forEach {
                    val existingDice = it.copy()

                    if (dice.uuid == it.uuid) {
                        existingDice.selected = selected
                    }

                    updatedDiceBag.add(existingDice)
                }

                repositoryBag.store(updatedDiceBag)

                _diceBag.value = updatedDiceBag

                _undoEnabled.value = isUndoPossible()
                _rollEnabled.value = isRollPossible()
            }
        }
    }

    fun markGroupAsSelected(group: Group) {
        Timber.d("group.uuid=${group.uuid}")

        viewModelScope.launch {
            mutex.withLock {
                val updatedHistory = _groupHistory.value.map {
                    if (it.uuid == group.uuid) it.copy(selected = !it.selected) else it
                }
                repositoryGroup.store(updatedHistory)
                _groupHistory.value = updatedHistory

                _undoEnabled.value = isUndoPossible()
                _rollEnabled.value = isRollPossible()
            }
        }
    }

    private fun isSettingsNotEnabled() = (
        !stateFlowSettings.value.rollIndexTime
            && !stateFlowSettings.value.rollScore
            && !stateFlowSettings.value.diceTitle
            && !stateFlowSettings.value.rollBehaviour
            && !stateFlowSettings.value.sideNumber
            && !stateFlowSettings.value.sideDescription
            && !stateFlowSettings.value.sideSVG
            && !stateFlowSettings.value.groupTitle
        )

    fun rollDiceSequence() {
        viewModelScope.launch {
            _rollEnabled.value = false
            _undoEnabled.value = false

            playRollHaptic()
            playRollSound()

            val newRollSequence = mutableListOf<Roll>()
            rollDiceSequence(newRollSequence)
            shuffleRollSequence(newRollSequence)

            rollsSequenceHelper.saveNewRollSequence(newRollSequence)

            _undoEnabled.value = true
            _rollEnabled.value = true

            RollsEvent.emit()
        }
    }

    fun shuffleRollSequence(rollSequence: MutableList<Roll>) {
        if (_stateFlowSettingsData.value.shuffle) {
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

    fun dismissSettingsDialog() {
        viewModelScope.launch {
            saveSettings()
            alignBottomSheetWithStorage()
        }
    }

    private suspend fun saveSettings() {
        val settings = repositorySettings.fetch().first()

        settings.rollIndexTime = _stateFlowSettingsData.value.rollIndexTime
        settings.rollScore = _stateFlowSettingsData.value.rollScore
        settings.rollScoreTTS = _stateFlowSettingsData.value.rollScoreTTS

        settings.diceTitle = _stateFlowSettingsData.value.diceTitle
        settings.sideNumber = _stateFlowSettingsData.value.sideNumber
        settings.rollBehaviour = _stateFlowSettingsData.value.rollBehaviour
        settings.sideDescription = _stateFlowSettingsData.value.sideDescription
        settings.sideSVG = _stateFlowSettingsData.value.sideSVG

        settings.rollSound = _stateFlowSettingsData.value.rollSound
        settings.haptics = _stateFlowSettingsData.value.haptics
        settings.shakeToRoll = _stateFlowSettingsData.value.shakeToRoll
        settings.shuffle = _stateFlowSettingsData.value.shuffle
        settings.groupTitle = _stateFlowSettingsData.value.groupTitle

        repositorySettings.store(settings)
    }

    suspend fun playRollHaptic() {
        if (_stateFlowSettingsData.value.haptics) {
            Timber.d("playRollHaptic")
            hapticHelper.playRollHaptic()
        }
    }

    suspend fun playUndoHaptic() {
        if (_stateFlowSettingsData.value.haptics) {
            Timber.d("playUndoHaptic")
            hapticHelper.playUndoHaptic()
        }
    }

    suspend fun playUndoAllHaptic() {
        if (_stateFlowSettingsData.value.haptics) {
            Timber.d("playUndoAllHaptic")
            hapticHelper.playUndoAllHaptic()
        }
    }

    suspend fun playRollSound() {
        if (_stateFlowSettingsData.value.rollSound) {
            rollsSoundPlayer.play()
            delay(750.milliseconds)
        }
    }

    suspend fun playScoreTTS(score: Int) {
        if (_stateFlowSettingsData.value.rollScoreTTS) {
            delay(250.milliseconds)
            rollsScoreTtsPlayer.playScore(score)
        }
    }

    override fun onCleared() {
        if (System.getProperty("isUnitTest") != "true") {
            ProcessLifecycleOwner.get().lifecycle.removeObserver(lifecycleEventObserver)
        }
        rollsSoundPlayer.release()
        rollsScoreTtsPlayer.release()
        shakeToRollService.stop()
    }

    private fun updateShakeService() {
        if (isForeground && _stateFlowSettingsData.value.shakeToRoll) {
            shakeToRollService.start()
        } else {
            shakeToRollService.stop()
        }
    }

    fun rollDiceSequence(newRollSequence: MutableList<Roll>) {
        val diceToRoll = getDiceToRoll()
        val diceCountMap = mutableMapOf<String, Int>()

        diceToRoll.forEach { (dice, uuidGroup) ->
            val count = diceCountMap.getOrDefault(dice.uuid, 0)
            newRollSequence.addAll(rollDice(dice, count * dice.multiplierValue, uuidGroup))
            diceCountMap[dice.uuid] = count + 1
        }
    }

    private fun getDiceToRoll(): List<Pair<Dice, String>> {
        val selectedDice = _diceBag.value.filter { it.selected }.map { it to "" }

        val diceMap = _diceBag.value.associateBy { it.uuid }
        val groupDice = _groupHistory.value
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
            if (_undoEnabled.value) {
                _undoEnabled.value = false
                _rollEnabled.value = false

                playUndoHaptic()

                val rollHistory = repositoryRoll.fetch().first()

                rollHistory.remove(rollHistory.keys.first())

                repositoryRoll.store(rollHistory)

                RollsEvent.emit()

                _undoEnabled.value = rollHistory.isNotEmpty()
                _rollEnabled.value = isRollPossible()
            }
        }
    }

    fun undoAll() {
        viewModelScope.launch {
            _undoEnabled.value = false

            playUndoAllHaptic()

            repositoryRoll.clear()
            RollsEvent.emit()
        }
    }

    fun settingsIndexTime(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(rollIndexTime = checked) }
            alignBottomSheetWithStorage()
        }
    }

    fun settingsRollScore(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(rollScore = checked) }
            alignBottomSheetWithStorage()
        }
    }

    fun settingsRollScoreTTS(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(rollScoreTTS = checked) }
            alignBottomSheetWithStorage()
        }
    }

    fun settingsDiceTitle(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(diceTitle = checked) }
            alignBottomSheetWithStorage()
        }
    }

    fun settingsSideNumber(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(sideNumber = checked) }
            alignBottomSheetWithStorage()
        }
    }

    fun settingsSideDescription(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(sideDescription = checked) }
            alignBottomSheetWithStorage()
        }
    }

    fun settingsSideSVG(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(sideSVG = checked) }
            alignBottomSheetWithStorage()
        }
    }

    fun settingsBehaviour(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(rollBehaviour = checked) }
            alignBottomSheetWithStorage()
        }
    }

    fun settingsGroupTitle(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(groupTitle = checked) }
            alignBottomSheetWithStorage()
        }
    }

    fun settingsShuffle(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(shuffle = checked) }
        }
    }

    fun settingsUseHaptics(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(haptics = checked) }
        }
    }

    fun settingsShakeToRoll(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(shakeToRoll = checked) }
        }
    }

    fun settingsRollSound(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(rollSound = checked) }
        }
    }

    fun isContentAvailableToDisplay(rolls: List<Roll>): Boolean {
        val settings = _stateFlowSettingsData.value

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
