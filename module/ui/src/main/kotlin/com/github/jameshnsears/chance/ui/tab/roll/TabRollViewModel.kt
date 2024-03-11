package com.github.jameshnsears.chance.ui.tab.roll

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.domain.state.DiceBag
import com.github.jameshnsears.chance.data.domain.state.Roll
import com.github.jameshnsears.chance.data.domain.utility.epoch.UtilityEpochTimeGenerator
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.data.repository.roll.RepositoryRollInterface
import com.github.jameshnsears.chance.data.repository.settings.RepositorySettingsInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.security.SecureRandom

data class TabRollState(
    var settingsTime: Boolean,
    var settingsScore: Boolean,
    var settingsDiceTitle: Boolean,
    var settingsSideNumber: Boolean
)

class TabRollViewModel(
    val repositorySettings: RepositorySettingsInterface,
    repositoryBag: RepositoryBagInterface,
    var repositoryRoll: RepositoryRollInterface,
) : ViewModel() {
    private val _stateFlow = MutableStateFlow(
        TabRollState(
            settingsTime = false,
            settingsScore = false,
            settingsDiceTitle = false,
            settingsSideNumber = true
        )
    )
    val tabRollStateFlow: StateFlow<TabRollState> = _stateFlow

    private var _settingsRollSound = MutableStateFlow(false)
    var settingsRollSound: StateFlow<Boolean> = _settingsRollSound

    private var _diceBag: MutableStateFlow<DiceBag> = MutableStateFlow(mutableListOf())
    var diceBag: StateFlow<DiceBag> = _diceBag

    private val _stateSequential = MutableStateFlow(false)
    val stateSequential: StateFlow<Boolean> = _stateSequential

    init {
        viewModelScope.launch {
            _diceBag.value = repositoryBag.fetch().first()
        }
    }

    private val random: SecureRandom = SecureRandom.getInstance("SHA1PRNG")

    fun roll(dice: DiceBag) {
        val mutex = Mutex()

        viewModelScope.launch {
            mutex.withLock {

                val rollSequence = mutableListOf<Roll>()
                dice.forEach { die ->
                    rollSequence.add(
                        Roll(
                            die.epoch,
                            randomSide(die),
                        ),
                    )
                }

                val rollHistory = repositoryRoll.fetch().first()
                rollHistory[UtilityEpochTimeGenerator.now()] = rollSequence
                repositoryRoll.store(rollHistory)
            }
        }
    }

    private fun randomSide(dice: Dice) = dice.sides[random.nextInt(dice.sides.size)]

    fun undo() {
        // disable if there is only 1 roll sequence
    }

    fun roll() {
    }

    fun sequential(checked: Boolean) {
        _stateSequential.value = checked
    }

    fun showTime(checked: Boolean) {
        _stateFlow.update { it.copy(settingsTime = checked) }
    }

    fun showScore(checked: Boolean) {
        _stateFlow.update { it.copy(settingsScore = checked) }
    }

    fun showDiceTitle(checked: Boolean) {
        _stateFlow.update { it.copy(settingsDiceTitle = checked) }
    }

    fun showSideImageNumber(checked: Boolean) {
        _stateFlow.update { it.copy(settingsSideNumber = checked) }
    }

    fun makeRollSound(checked: Boolean) {
        _settingsRollSound.value = checked
    }
}
