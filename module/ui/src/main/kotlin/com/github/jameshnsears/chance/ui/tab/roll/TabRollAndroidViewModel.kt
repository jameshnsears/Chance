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
import com.github.jameshnsears.chance.data.domain.core.settings.impl.SettingsDataImpl
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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import java.security.SecureRandom

data class SettingsState(
    var rollIndexTime: Boolean,
    var rollScore: Boolean,

    var diceTitle: Boolean,
    var behaviour: Boolean,
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
            rollIndexTime = SettingsDataImpl().rollIndexTime,
            rollScore = SettingsDataImpl().rollScore,
            diceTitle = SettingsDataImpl().diceTitle,
            behaviour = SettingsDataImpl().behaviour,
            sideNumber = SettingsDataImpl().sideNumber,
            sideDescription = SettingsDataImpl().sideDescription,
            sideSVG = SettingsDataImpl().sideSVG,
            rollSound = SettingsDataImpl().rollSound
        )
    )
    val stateFlowSettings: StateFlow<SettingsState> = _stateFlowSettingsData

    private var _diceBag: MutableStateFlow<DiceBag> = MutableStateFlow(mutableListOf())
    var diceBag: StateFlow<DiceBag> = _diceBag

    private var _undoEnabled = MutableStateFlow(false)
    var undoEnabled: StateFlow<Boolean> = _undoEnabled

    private var _rollEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var rollEnabled: StateFlow<Boolean> = _rollEnabled

    // TODO LeakCanary is currently disabled: test class org.junit.Test was found in classpath

    init {
        viewModelScope.launch {
            alignUndoAndRollButtonsBasedOnSettings()
        }

        viewModelScope.launch {
            DialogBagCloseEvent.sharedFlowDialogBagCloseEvent.collect {
                Timber.d("collect")
                alignUndoAndRollButtonsBasedOnSettings()
            }
        }

        viewModelScope.launch {
            TabBagImportEvent.sharedFlowTabBagImportEvent.collect {
                Timber.d("collect")
                alignSettings()
                alignUndoAndRollButtonsBasedOnSettings()
            }
        }
    }

    private suspend fun alignUndoAndRollButtonsBasedOnSettings() {
        _diceBag.value = repositoryBag.fetch().first()

        val settings = repositorySettings.fetch().first()

        if (!settings.rollIndexTime
            && !settings.rollScore
            && !settings.diceTitle
            && !settings.sideNumber
            && !settings.behaviour
            && !settings.sideDescription
            && !settings.sideSVG
        ) {
            _undoEnabled.value = false
            _rollEnabled.value = false
        } else {
            _undoEnabled.value = isUndoPossible()
            _rollEnabled.value = isRollPossible()
        }
    }

    private suspend fun alignSettings() {
        viewModelScope.launch {
            val settings = repositorySettings.fetch().first()

            _stateFlowSettingsData.update {
                it.copy(
                    rollIndexTime = settings.rollIndexTime,
                    rollScore = settings.rollScore,
                    diceTitle = settings.diceTitle,
                    sideNumber = settings.sideNumber,
                    behaviour = settings.behaviour,
                    sideDescription = settings.sideDescription,
                    sideSVG = settings.sideSVG,
                    rollSound = settings.rollSound
                )
            }
        }
    }

    private fun isRollPossible(): Boolean {
        _diceBag.value.forEach {
            if (it.selected)
                return true
        }
        return false
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

            alignUndoAndRollButtonsBasedOnSettings()
        }
    }

    private val secureRandom: SecureRandom = SecureRandom.getInstance("SHA1PRNG")

    fun rollDiceSequence() {
        viewModelScope.launch {
            playRollSound()

            val mutex = Mutex()

            mutex.withLock {
                val newRollSequence = mutableListOf<Roll>()

                rollDiceSequence(newRollSequence)

                saveDiceSequence(newRollSequence)

                TabRollEvent.emit()
            }
        }
    }

    private suspend fun playRollSound() {
        if (_stateFlowSettingsData.value.rollSound) {
            mediaPlayerRollSound()
            delay(750)
        }
    }

    private lateinit var mediaPlayer: MediaPlayer

    private fun mediaPlayerRollSound() {
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

    suspend fun saveDiceSequence(
        newRollSequence: MutableList<Roll>
    ) {
        val rollHistory: RollHistory = LinkedHashMap()
        rollHistory[UtilityEpochTimeGenerator.now()] = newRollSequence
        rollHistory.putAll(repositoryRoll.fetch().first())

        repositoryRoll.clear()
        repositoryRoll.store(rollHistory)

        _undoEnabled.value = isUndoPossible()
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

    private suspend fun isUndoPossible() = repositoryRoll.fetch().first().size != 0

    fun undo() {
        viewModelScope.launch {
            if (isUndoPossible()) {
                val rollHistory: RollHistory = LinkedHashMap()
                rollHistory.putAll(repositoryRoll.fetch().first())
                rollHistory.remove(rollHistory.keys.first())

                repositoryRoll.store(rollHistory)

                _undoEnabled.value = isUndoPossible()

                TabRollEvent.emit()
            }
        }
    }

    fun settingsIndexTime(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(rollIndexTime = checked) }

            val settings = repositorySettings.fetch().first()
            settings.rollIndexTime = checked
            repositorySettings.store(settings)

            alignUndoAndRollButtonsBasedOnSettings()
        }
    }

    fun settingsRollScore(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(rollScore = checked) }

            val settings = repositorySettings.fetch().first()
            settings.rollScore = checked
            repositorySettings.store(settings)

            alignUndoAndRollButtonsBasedOnSettings()
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

            val settings = repositorySettings.fetch().first()
            settings.diceTitle = checked
            repositorySettings.store(settings)

            alignUndoAndRollButtonsBasedOnSettings()
        }
    }

    fun settingsSideNumber(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(sideNumber = checked) }

            val settings = repositorySettings.fetch().first()
            settings.sideNumber = checked
            repositorySettings.store(settings)

            alignUndoAndRollButtonsBasedOnSettings()
        }
    }

    fun settingsSideDescription(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(sideDescription = checked) }

            val settings = repositorySettings.fetch().first()
            settings.sideDescription = checked
            repositorySettings.store(settings)

            alignUndoAndRollButtonsBasedOnSettings()
        }
    }

    fun settingsSideSVG(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(sideSVG = checked) }

            val settings = repositorySettings.fetch().first()
            settings.sideSVG = checked
            repositorySettings.store(settings)

            alignUndoAndRollButtonsBasedOnSettings()
        }
    }

    fun settingsRollSound(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(rollSound = checked) }

            val settings = repositorySettings.fetch().first()
            settings.rollSound = checked
            repositorySettings.store(settings)

            alignUndoAndRollButtonsBasedOnSettings()
        }
    }

    fun settingsBehaviour(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowSettingsData.update { it.copy(behaviour = checked) }

            val settings = repositorySettings.fetch().first()
            settings.behaviour = checked
            repositorySettings.store(settings)

            alignUndoAndRollButtonsBasedOnSettings()
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
