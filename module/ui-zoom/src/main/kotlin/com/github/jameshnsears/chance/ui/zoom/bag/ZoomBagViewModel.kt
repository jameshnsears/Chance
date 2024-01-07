package com.github.jameshnsears.chance.ui.zoom.bag

import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryInterface
import com.github.jameshnsears.chance.ui.zoom.ZoomViewModel

class ZoomBagViewModel(
    val bagRepository: BagRepositoryInterface
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
    fun zoom() {
    }
}