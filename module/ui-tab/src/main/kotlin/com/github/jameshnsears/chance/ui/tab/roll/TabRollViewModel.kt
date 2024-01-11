package com.github.jameshnsears.chance.ui.tab.roll

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Roll
import com.github.jameshnsears.chance.data.repository.roll.RollRepositoryInterface
import com.github.jameshnsears.chance.data.repository.settings.SettingsRepositoryInterface
import com.github.jameshnsears.chance.utils.epoch.EpochTime
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.security.SecureRandom

class TabRollViewModel(
    val settingsRepository: SettingsRepositoryInterface,
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

    /*
                                        Title
                                        Side # + Side Image
    History timestamp       Total
                                        Image
                                        Description
     */


    private val random: SecureRandom = SecureRandom.getInstance("SHA1PRNG")

    fun selectedDice() {
    }

    fun roll(dice: List<Dice>) {
        val mutex = Mutex()

        viewModelScope.launch {
            mutex.withLock {

                val rollSequence = mutableListOf<Roll>()
                dice.forEach { die ->
                    rollSequence.add(
                        Roll(
                            die,
                            randomSide(die)
                        )
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
