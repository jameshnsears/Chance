package com.github.jameshnsears.chance.ui.zoom

import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryInterface
import com.github.jameshnsears.chance.data.domain.Side
import com.github.jameshnsears.chance.data.zoom.model.ZoomModel

open class ZoomViewModel(
    val bagRepository: BagRepositoryInterface,
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

    /////////////////

    // TODO might not need a ZoomModel!
    val zoomModel = ZoomModel()

    fun bagDemo() = bagRepository.bagDemo

    fun zoomSize() = 50.dp

    fun textStringsIdAvailable(side: Side): Boolean {
        /*
        return one of the following

        side.text
        side.textStringsId == stringResource(id = ...)
        */

        if (side.textStringsId == 0 )
            return false
        else
            return true
    }

    fun imageDrawableIdAvailable(side: Side): Boolean {
        if (side.imageDrawableId == 0)
            return false
        else
            return true
    }
}