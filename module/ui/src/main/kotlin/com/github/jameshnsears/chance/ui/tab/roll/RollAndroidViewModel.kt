package com.github.jameshnsears.chance.ui.tab.roll

import android.app.Application
import androidx.compose.runtime.Stable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.repo.api.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repo.api.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repo.api.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagCloseEvent
import com.github.jameshnsears.chance.ui.tab.bag.BagImportEvent
import com.github.jameshnsears.chance.ui.tab.bag.BagResetEvent
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

@Stable
data class SettingsState(
    var rollIndexTime: Boolean,
    var rollScore: Boolean,

    var diceTitle: Boolean,
    var rollBehaviour: Boolean,
    var sideNumber: Boolean,
    var sideDescription: Boolean,
    var sideSVG: Boolean,

    var shuffle: Boolean,
    var rollSound: Boolean,
)

class RollAndroidViewModel(
    application: Application,
    val repositorySettings: RepositorySettingsInterface,
    val repositoryBag: RepositoryBagInterface,
    val repositoryRoll: RepositoryRollInterface,
) : AndroidViewModel(application) {
    val _stateFlowSettingsData = MutableStateFlow(
        SettingsState(
            rollIndexTime = false,
            rollScore = false,
            diceTitle = false,
            rollBehaviour = false,
            sideNumber = false,
            sideDescription = false,
            sideSVG = false,
            shuffle = false,
            rollSound = false,
        )
    )
    val stateFlowSettings: StateFlow<SettingsState> = _stateFlowSettingsData

    private var _diceBag: MutableStateFlow<DiceBag> = MutableStateFlow(mutableListOf())
    var diceBag: StateFlow<DiceBag> = _diceBag

    var _undoEnabled = MutableStateFlow(false)
    var undoEnabled: StateFlow<Boolean> = _undoEnabled

    private var _rollEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var rollEnabled: StateFlow<Boolean> = _rollEnabled

    init {
        viewModelScope.launch {
            alignSettings()
            alignBottomSheetWithStorage()
        }

        viewModelScope.launch {
            DialogBagCloseEvent.sharedFlowDialogBagCloseEvent.collect {
                Timber.d("collect.DialogBagCloseEvent.TabRollAndroidViewModel")
                alignBottomSheetWithStorage()
            }
        }

        viewModelScope.launch {
            merge(
                BagImportEvent.sharedFlowTabBagImportEvent.map { },
                BagResetEvent.sharedFlowTabBagResetEvent.map { }
            ).collect {
                Timber.d("collect.TabBagImportEvent|TabBagResetStorageEvent.TabRollAndroidViewModel")
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
                diceTitle = settings.diceTitle,
                sideNumber = settings.sideNumber,
                rollBehaviour = settings.rollBehaviour,
                sideDescription = settings.sideDescription,
                sideSVG = settings.sideSVG,
                rollSound = settings.rollSound,
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

        _diceBag.value = repositoryBag.fetch().first()

        return _diceBag.value.any { it.selected }
    }

    private val mutex = Mutex()

    fun markDiceAsSelected(dice: Dice, selected: Boolean) {
        Timber.d("epoch=${dice.epoch}; selected=${selected}")

        viewModelScope.launch {
            mutex.withLock {
                val updatedDiceBag: DiceBag = mutableListOf()

                _diceBag.value.forEach {
                    val existingDice = it.copy()

                    if (dice.epoch == it.epoch) {
                        existingDice.selected = selected
                    }

                    updatedDiceBag.add(existingDice)
                }

                repositoryBag.store(updatedDiceBag)

                _diceBag.value = updatedDiceBag

                _undoEnabled.value = isUndoPossible()

                if (isSettingsNotEnabled())
                    _rollEnabled.value = false
                else {
                    _rollEnabled.value = _diceBag.value.any { it.selected }
                }
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
        )

    private val secureRandom = SecureRandom()

    fun rollDiceSequence() {
        viewModelScope.launch {
            _rollEnabled.value = false
            _undoEnabled.value = false

            playRollSound()

            val newRollSequence = mutableListOf<Roll>()

            rollDiceSequence(newRollSequence)

            shuffleRollSequence(newRollSequence)

            rollSequenceHelper.saveNewRollSequence(newRollSequence)

            _undoEnabled.value = true
            _rollEnabled.value = true

            RollEvent.emit()
        }
    }

    fun shuffleRollSequence(rollSequence: MutableList<Roll>) {
        if (_stateFlowSettingsData.value.shuffle) {
            if (_diceBag.value.count { it.selected } > 1) {
                rollSequence.shuffle()

                // order the multipleIndex into ASC order for any dice cluster
                rollSequence
                    .groupBy { it.diceEpoch }
                    .forEach { (_, rolls) ->
                        val sortedIndices = rolls.map { it.multiplierIndex }.sorted()
                        rolls.forEachIndexed { index, roll ->
                            roll.multiplierIndex = sortedIndices[index]
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

        settings.diceTitle = _stateFlowSettingsData.value.diceTitle
        settings.sideNumber = _stateFlowSettingsData.value.sideNumber
        settings.rollBehaviour = _stateFlowSettingsData.value.rollBehaviour
        settings.sideDescription = _stateFlowSettingsData.value.sideDescription
        settings.sideSVG = _stateFlowSettingsData.value.sideSVG

        settings.rollSound = _stateFlowSettingsData.value.rollSound

        repositorySettings.store(settings)
    }

    suspend fun playRollSound() {
        if (_stateFlowSettingsData.value.rollSound) {
            rollSoundPlayer.play()
            delay(750)
        }
    }

    private val rollSoundPlayer: RollSoundPlayer = RollSoundPlayer(getApplication())

    override fun onCleared() {
        super.onCleared()
        rollSoundPlayer.release()
    }

    fun rollDiceSequence(newRollSequence: MutableList<Roll>) {
        if (_diceBag.value.any { it.selected }) {

            _diceBag.value.forEach { dice ->
                if (dice.selected) {

                    for (indexMultiplier in 1..dice.multiplierValue) {
                        var randomSide = randomSide(dice)

                        newRollSequence.add(
                            0,
                            Roll(
                                diceEpoch = dice.epoch,
                                side = randomSide,
                                multiplierIndex = indexMultiplier,
                                score = randomSide.number
                            ),
                        )

                        if (dice.explode) {
                            var indexExplode = 0
                            val explosionDepth = 5

                            while (indexExplode != explosionDepth) {
                                if (rollSequenceHelper.diceCanExplode(dice, randomSide)) {
                                    indexExplode++

                                    randomSide = randomSide(dice)

                                    newRollSequence.add(
                                        0,
                                        Roll(
                                            diceEpoch = dice.epoch,
                                            side = randomSide,
                                            multiplierIndex = indexMultiplier,
                                            explodeIndex = indexExplode,
                                            score = randomSide.number
                                        ),
                                    )
                                }

                                if (!rollSequenceHelper.diceCanExplode(dice, randomSide))
                                    break
                                else {
                                    randomSide = randomSide(dice)
                                }
                            }
                        }
                    }

                    if (dice.modifyScore) {
                        newRollSequence[0].scoreAdjustment = dice.modifyScoreValue
                        newRollSequence[0].score = (newRollSequence[0].score + dice.modifyScoreValue)
                    }
                }
            }

            newRollSequence.reverse()
        }
    }

    val rollSequenceHelper = RollSequenceHelper(repositoryRoll)

    fun randomSide(dice: Dice) = dice.sides[secureRandom.nextInt(dice.sides.size)]

    fun undo() {
        viewModelScope.launch {
            if (_undoEnabled.value) {
                _undoEnabled.value = false
                _rollEnabled.value = false

                val rollHistory = repositoryRoll.fetch().first()

                rollHistory.remove(rollHistory.keys.first())

                repositoryRoll.store(rollHistory)

                RollEvent.emit()

                _undoEnabled.value = rollHistory.isNotEmpty()
                _rollEnabled.value = _diceBag.value.any { it.selected }
            }
        }
    }

    fun undoAll() {
        viewModelScope.launch {
            _undoEnabled.value = false
            repositoryRoll.clear()
            RollEvent.emit()
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

    fun settingsShuffle(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(shuffle = checked) }
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
            if (it.side.imageBase64.isNotEmpty())
                svgExists = true

            if (it.side.description.isNotEmpty())
                descriptionExists = true
        }

        return (settings.rollIndexTime
            ||
            settings.rollScore
            ||
            settings.diceTitle
            ||
            settings.sideNumber
            ||
            (settings.sideDescription && descriptionExists)
            ||
            (settings.sideSVG && svgExists)
            )
    }
}
