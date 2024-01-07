package com.github.jameshnsears.chance.ui.zoom.roll

import com.github.jameshnsears.chance.data.roll.repository.RollRepositoryInterface
import com.github.jameshnsears.chance.ui.zoom.ZoomViewModel

class ZoomRollViewModel(
    val bagRepository: RollRepositoryInterface,
) : ZoomViewModel() {
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
typealias RollSequenceEpoch = Long

typealias RollSequence = List<Roll>

typealias RollHistory = LinkedHashMap<RollSequenceEpoch, RollSequence>

data class Roll(
    val dice: Dice,
    val side: Side
)
     */

    /*
                                    Dice Title
                                    Side #
    RollSequenceEpoch   Total
                                    Image
                                    Description

                vs.

                                    Dice Title
    RollSequenceEpoch   Total       Image
                                    Description
     */

    fun title(active: Boolean = true) {
    }

    fun sideNumber(active: Boolean = true) {
    }

    fun total(active: Boolean = true) {
    }

    fun history(active: Boolean = true) {
    }

    fun historyClear() {
    }

    fun sound(active: Boolean = true) {
    }
}