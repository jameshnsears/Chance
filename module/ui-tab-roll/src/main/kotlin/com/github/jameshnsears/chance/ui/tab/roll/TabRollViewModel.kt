package com.github.jameshnsears.chance.ui.tab.roll

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Roll
import com.github.jameshnsears.chance.data.roll.repository.RollRepositoryInterface
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.security.SecureRandom

class TabRollViewModel(
    var rollRepository: RollRepositoryInterface,
) : ViewModel() {
    private val random: SecureRandom = SecureRandom.getInstance("SHA1PRNG")

    fun rollDice(dice: List<Dice>) {
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

                rollRepository.store(rollSequence)
            }
        }
    }

    fun randomSide(dice: Dice) = dice.sides[random.nextInt(dice.sides.size)]
}
