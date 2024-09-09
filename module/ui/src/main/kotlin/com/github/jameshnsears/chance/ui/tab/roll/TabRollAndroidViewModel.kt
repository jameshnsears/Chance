package com.github.jameshnsears.chance.ui.tab.roll

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.DiceRollValues
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.DiceBag
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.domain.utility.epoch.UtilityEpochTimeGenerator
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.ui.R
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagCloseEvent
import com.github.jameshnsears.chance.ui.tab.bag.TabBagImportEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.security.SecureRandom

data class SettingsState(
    var rollIndexTime: Boolean,
    var rollScore: Boolean,

    var diceTitle: Boolean,
    var rollBehaviour: Boolean,
    var sideNumber: Boolean,
    var sideDescription: Boolean,
    var sideSVG: Boolean,

    var rollSound: Boolean,
)

class TabRollAndroidViewModel(
    application: Application,
    val repositorySettings: RepositorySettingsInterface,
    val repositoryBag: RepositoryBagInterface,
    val repositoryRoll: RepositoryRollInterface,
) : AndroidViewModel(application) {
    private val _stateFlowSettingsData = MutableStateFlow(
        SettingsState(
            rollIndexTime = false,
            rollScore = false,
            diceTitle = false,
            rollBehaviour = false,
            sideNumber = false,
            sideDescription = false,
            sideSVG = false,
            rollSound = false,
        )
    )
    val stateFlowSettings: StateFlow<SettingsState> = _stateFlowSettingsData

    private var _diceBag: MutableStateFlow<DiceBag> = MutableStateFlow(mutableListOf())
    var diceBag: StateFlow<DiceBag> = _diceBag

    private var _undoEnabled = MutableStateFlow(false)
    var undoEnabled: StateFlow<Boolean> = _undoEnabled

    private var _rollEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var rollEnabled: StateFlow<Boolean> = _rollEnabled

    init {
        viewModelScope.launch {
            alignSettings()
            alignBottomSheetWithSettings()
        }

        viewModelScope.launch {
            DialogBagCloseEvent.sharedFlowDialogBagCloseEvent.collect {
                Timber.d("collect.DialogBagCloseEvent.TabRollAndroidViewModel")
                alignBottomSheetWithSettings()
            }
        }

        viewModelScope.launch {
            TabBagImportEvent.sharedFlowTabBagImportEvent.collect {
                Timber.d("collect.TabBagImportEvent.TabRollAndroidViewModel")
                alignSettings()
                alignBottomSheetWithSettings()
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

    private suspend fun alignBottomSheetWithSettings() {
        _undoEnabled.value = isUndoPossible()
        Timber.d("_undoEnabled.value=${_undoEnabled.value}")

        _rollEnabled.value = isRollPossible()
        Timber.d("_rollEnabled.value=${_rollEnabled.value}")
    }

    private suspend fun isUndoPossible(): Boolean {
        if (!stateFlowSettings.value.rollIndexTime
            && !stateFlowSettings.value.rollScore
            && !stateFlowSettings.value.diceTitle
            && !stateFlowSettings.value.rollBehaviour
            && !stateFlowSettings.value.sideNumber
            && !stateFlowSettings.value.sideDescription
            && !stateFlowSettings.value.sideSVG
        )
            return false

        return repositoryRoll.fetch().first().size != 0
    }

    private suspend fun isRollPossible(): Boolean {
        if (!stateFlowSettings.value.rollIndexTime
            && !stateFlowSettings.value.rollScore
            && !stateFlowSettings.value.diceTitle
            && !stateFlowSettings.value.rollBehaviour
            && !stateFlowSettings.value.sideNumber
            && !stateFlowSettings.value.sideDescription
            && !stateFlowSettings.value.sideSVG
        )
            return false

        _diceBag.value = repositoryBag.fetch().first()
        var selected = false
        _diceBag.value.forEach {
            if (it.selected) {
                selected = true
            }
        }
        return selected
    }

    fun markDiceAsSelected(dice: Dice, selected: Boolean) {
        Timber.d("epoch=${dice.epoch}; selected=${selected}")

        viewModelScope.launch {
            val updatedDiceBag: DiceBag = mutableListOf()

            _diceBag.value.forEach {
                val newDice = it.copy()

                if (dice.epoch == it.epoch) {
                    newDice.selected = selected
                }
                updatedDiceBag.add(newDice)
            }

            repositoryBag.store(updatedDiceBag)

            TabRollEvent.emit()

            _diceBag.value = repositoryBag.fetch().first()

            alignBottomSheetWithSettings()
        }
    }

    private val secureRandom: SecureRandom = SecureRandom.getInstance("SHA1PRNG")

    fun rollDiceSequence() {
        viewModelScope.launch {
            _rollEnabled.value = false

            playRollSound()

            val newRollSequence = mutableListOf<Roll>()

            rollDiceSequence(newRollSequence)

            saveNewRollSequence(newRollSequence)

            if (repositoryRoll.fetch().first().size % 20 == 0)
                viewModelScope.launch {
                    Timber.d("System.gc() request")
                    System.gc()
                }

            _undoEnabled.value = true

            _rollEnabled.value = true

            TabRollEvent.emit()
        }
    }

    fun dismissSettingsDialog() {
        viewModelScope.launch {
            saveSettings()
            alignBottomSheetWithSettings()
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

    private suspend fun playRollSound() {
        if (_stateFlowSettingsData.value.rollSound) {
            mediaPlayerRollSound()
            delay(750)
        }
    }

    private lateinit var mediaPlayer: MediaPlayer

    fun mediaPlayerRollSound() {
        if (!::mediaPlayer.isInitialized) {
            mediaPlayer = MediaPlayer.create(getApplication(), R.raw.roll)
        }

        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.release()
            mediaPlayer = MediaPlayer.create(getApplication(), R.raw.roll)
        }

        mediaPlayer.start()
    }

    suspend fun saveNewRollSequence(
        newRollSequence: MutableList<Roll>
    ) {
        val rollHistory: RollHistory = LinkedHashMap()
        rollHistory[UtilityEpochTimeGenerator.now()] = newRollSequence

        rollHistory.putAll(repositoryRoll.fetch().first())
        repositoryRoll.store(rollHistory)
    }

    fun rollDiceSequence(newRollSequence: MutableList<Roll>) {
        diceBag.value.forEach { dice ->
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
                            if (diceSequenceCanExplode(dice, randomSide)) {
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

                            if (!diceSequenceCanExplode(dice, randomSide))
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

    private fun diceSequenceCanExplode(
        dice: Dice,
        side: Side
    ): Boolean {
        var explode = false

        when (dice.explodeWhen) {
            DiceRollValues.explodeWhenValues[0] -> {
                // "="
                if (side.number == dice.explodeValue) {
                    explode = true
                }
            }

            DiceRollValues.explodeWhenValues[1] -> {
                // "<"
                if (side.number < dice.explodeValue) {
                    explode = true
                }
            }

            DiceRollValues.explodeWhenValues[2] -> {
                // ">"
                if (side.number > dice.explodeValue) {
                    explode = true
                }
            }
        }
        return explode
    }

    fun randomSide(dice: Dice) = dice.sides[secureRandom.nextInt(dice.sides.size)]

    fun undo() {
        viewModelScope.launch {
            val rollHistory = repositoryRoll.fetch().first()

            if (rollHistory.isNotEmpty()) {
                _undoEnabled.value = false

                rollHistory.remove(rollHistory.keys.first())

                repositoryRoll.store(rollHistory)

                TabRollEvent.emit()

                _undoEnabled.value = rollHistory.size != 0

                if (rollHistory.size % 10 == 0)
                    viewModelScope.launch {
                        Timber.d("System.gc() request")
                        System.gc()
                    }
            }
        }
    }

    fun undoAll() {
        viewModelScope.launch {
            _undoEnabled.value = false
            repositoryRoll.clear()
            TabRollEvent.emit()
        }
    }

    fun settingsIndexTime(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(rollIndexTime = checked) }
            alignBottomSheetWithSettings()
        }
    }

    fun settingsRollScore(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(rollScore = checked) }
            alignBottomSheetWithSettings()
        }
    }

    fun diceSequenceScore(rollSequence: MutableMap.MutableEntry<Long, List<Roll>>): String {
        var score = 0
        rollSequence.value.forEach {
            score += it.score
        }
        return "$score"
    }

    fun settingsDiceTitle(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(diceTitle = checked) }
            alignBottomSheetWithSettings()
        }
    }

    fun settingsSideNumber(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(sideNumber = checked) }
            alignBottomSheetWithSettings()
        }
    }

    fun settingsSideDescription(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(sideDescription = checked) }
            alignBottomSheetWithSettings()
        }
    }

    fun settingsSideSVG(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(sideSVG = checked) }
            alignBottomSheetWithSettings()
        }
    }

    fun settingsBehaviour(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(rollBehaviour = checked) }
            alignBottomSheetWithSettings()
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
            if (it.side.imageBase64 != "" || it.side.imageDrawableId != 0)
                svgExists = true

            if (it.side.description != "")
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
