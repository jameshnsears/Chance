package com.github.jameshnsears.chance.ui.tab.roll

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.DiceBag
import com.github.jameshnsears.chance.data.domain.Roll
import com.github.jameshnsears.chance.data.repository.bag.DiceBagRepositoryInterface
import com.github.jameshnsears.chance.data.repository.roll.RollRepositoryInterface
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryInterface
import com.github.jameshnsears.chance.ui.zoom.roll.ZoomRollViewModel
import com.github.jameshnsears.chance.utils.epoch.EpochTime
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.security.SecureRandom

class TabRollViewModel(
    val settingsRepository: SettingsRepositoryInterface,
    val bagRepository: DiceBagRepositoryInterface,
    var rollRepository: RollRepositoryInterface,
) : ViewModel() {
    //    var _description = MutableStateFlow(fetchInitialDescription())
//    var description: StateFlow<String> = _description
//
//    fun fetchInitialDescription(): String = bagModel.fetchDiceDescription(diceIndex)
//
//    fun updateCurrentDescription(description: String) {
//        Timber.d("description=", description)
//        _description.value = description
//    }

    private val random: SecureRandom = SecureRandom.getInstance("SHA1PRNG")

    val zoomRollModel = ZoomRollViewModel(
        settingsRepository,
        bagRepository,
        rollRepository,
    )

    fun selectedDice() {
    }

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

                val rollHistory = rollRepository.fetch()
                rollHistory[EpochTime.now()] = rollSequence
                rollRepository.store(rollHistory)
            }
        }
    }

    private fun randomSide(dice: Dice) = dice.sides[random.nextInt(dice.sides.size)]


    fun undo() {

    }

    fun roll() {

    }
}
