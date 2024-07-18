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
import com.github.jameshnsears.chance.data.domain.core.settings.Settings
import com.github.jameshnsears.chance.data.domain.utility.epoch.UtilityEpochTimeGenerator
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsInterface
import com.github.jameshnsears.chance.ui.R
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagCloseEvent
import com.github.jameshnsears.chance.ui.tab.SettingsTab
import com.github.jameshnsears.chance.ui.tab.TabSettingsInterface
import com.github.jameshnsears.chance.ui.tab.TabSettingsModel
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

data class TabRollState(
    var rollTime: Boolean,
    var rollScore: Boolean,

    var diceTitle: Boolean,
    var sideNumber: Boolean,
    var behaviour: Boolean,
    var sideDescription: Boolean,
    var sideSVG: Boolean,

    var rollSound: Boolean,
)

class TabRollAndroidViewModel(
    application: Application,
    val repositorySettings: RepositorySettingsInterface,
    val repositoryBag: RepositoryBagInterface,
    val repositoryRoll: RepositoryRollInterface,
) : AndroidViewModel(application), TabSettingsInterface {
    private val _stateFlowTabRoll = MutableStateFlow(
        TabRollState(
            rollTime = Settings().rollIndexTime,
            rollScore = Settings().rollScore,
            diceTitle = Settings().diceTitle,
            sideNumber = Settings().sideNumber,
            behaviour = Settings().behaviour,
            sideDescription = Settings().sideDescription,
            sideSVG = Settings().sideSVG,
            rollSound = Settings().rollSound
        )
    )
    val stateFlowTabRoll: StateFlow<TabRollState> = _stateFlowTabRoll

    private var _diceBag: MutableStateFlow<DiceBag> = MutableStateFlow(mutableListOf())
    var diceBag: StateFlow<DiceBag> = _diceBag

    private var _undoEnabled = MutableStateFlow(false)
    var undoEnabled: StateFlow<Boolean> = _undoEnabled

    private var _rollEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var rollEnabled: StateFlow<Boolean> = _rollEnabled

    init {
        viewModelScope.launch {
            alignUndoAndRollButtonsWithDiceBag()
        }

        viewModelScope.launch {
            DialogBagCloseEvent.sharedFlowDialogBagCloseEvent.collect {
                Timber.d("collect")
                alignUndoAndRollButtonsWithDiceBag()
            }
        }

        viewModelScope.launch {
            TabBagImportEvent.sharedFlowTabBagImportEvent.collect {
                Timber.d("collect")
                alignUndoAndRollButtonsWithDiceBag()
            }
        }
    }

    private suspend fun TabRollAndroidViewModel.alignUndoAndRollButtonsWithDiceBag() {
        _diceBag.value = repositoryBag.fetch().first()

        _undoEnabled.value = isUndoPossible()

        _rollEnabled.value = isRollPossible()
    }

    private fun isRollPossible(): Boolean {
        diceBag.value.forEach {
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

            _diceBag.value = repositoryBag.fetch().first()
        }

        _rollEnabled.value = isRollPossible()
    }

    private val secureRandom: SecureRandom = SecureRandom.getInstance("SHA1PRNG")

    override fun markTabAsCurrentInSettings() {
        viewModelScope.launch {
            TabSettingsModel.markTabAsCurrentInSettings(repositorySettings, SettingsTab.TAB_ROLLS)
        }
    }

    fun diceSequenceRoll() {
        viewModelScope.launch {
            if (_stateFlowTabRoll.value.rollSound) {
                mediaPlayerRollSound()
                delay(750)
            }

            val mutex = Mutex()

            mutex.withLock {
                val newRollSequence = mutableListOf<Roll>()

                diceSequenceRoll(newRollSequence)

                diceSequenceStore(newRollSequence)

                TabRollEvent.emit()
            }
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

    suspend fun diceSequenceStore(
        newRollSequence: MutableList<Roll>
    ) {
        val rollHistory: RollHistory = LinkedHashMap()
        rollHistory[UtilityEpochTimeGenerator.now()] = newRollSequence
        rollHistory.putAll(repositoryRoll.fetch().first())

        repositoryRoll.clear()
        repositoryRoll.store(rollHistory)

        _undoEnabled.value = isUndoPossible()
    }

    fun diceSequenceRoll(newRollSequence: MutableList<Roll>) {
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

    fun settingsTime(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowTabRoll.update { it.copy(rollTime = checked) }

            val settings = repositorySettings.fetch().first()
            settings.rollIndexTime = checked
            repositorySettings.store(settings)
        }
    }

    fun settingsRollScore(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowTabRoll.update { it.copy(rollScore = checked) }

            val settings = repositorySettings.fetch().first()
            settings.rollScore = checked
            repositorySettings.store(settings)
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
            _stateFlowTabRoll.update { it.copy(diceTitle = checked) }

            val settings = repositorySettings.fetch().first()
            settings.diceTitle = checked
            repositorySettings.store(settings)
        }
    }

    fun settingsSideNumber(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowTabRoll.update { it.copy(sideNumber = checked) }

            val settings = repositorySettings.fetch().first()
            settings.sideNumber = checked
            repositorySettings.store(settings)
        }
    }

    fun settingsSideDescription(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowTabRoll.update { it.copy(sideDescription = checked) }

            val settings = repositorySettings.fetch().first()
            settings.sideDescription = checked
            repositorySettings.store(settings)
        }
    }

    fun settingsSideSVG(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowTabRoll.update { it.copy(sideSVG = checked) }

            val settings = repositorySettings.fetch().first()
            settings.sideSVG = checked
            repositorySettings.store(settings)
        }
    }

    fun settingsRollSound(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowTabRoll.update { it.copy(rollSound = checked) }

            val settings = repositorySettings.fetch().first()
            settings.rollSound = checked
            repositorySettings.store(settings)
        }
    }

    fun settingsBehaviour(checked: Boolean) {
        viewModelScope.launch {
            _stateFlowTabRoll.update { it.copy(behaviour = checked) }

            val settings = repositorySettings.fetch().first()
            settings.behaviour = checked
            repositorySettings.store(settings)
        }
    }

    fun isContentAvailableToDisplay(rolls: List<Roll>): Boolean {
        val stateFlowTabRollValue = _stateFlowTabRoll.value

        var svgExists = false
        var descriptionExists = false
        rolls.forEach {
            if (it.side.imageBase64 != "" || it.side.imageDrawableId != 0)
                svgExists = true

            if (it.side.description != "")
                descriptionExists = true
        }

        return (stateFlowTabRollValue.rollTime
                ||
                stateFlowTabRollValue.rollScore
                ||
                stateFlowTabRollValue.diceTitle
                ||
                stateFlowTabRollValue.sideNumber
                ||
                (stateFlowTabRollValue.sideDescription && descriptionExists)
                ||
                (stateFlowTabRollValue.sideSVG && svgExists)
                )
    }
}
