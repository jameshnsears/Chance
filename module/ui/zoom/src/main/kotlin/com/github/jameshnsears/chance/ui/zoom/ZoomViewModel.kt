package com.github.jameshnsears.chance.ui.zoom

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

    /*
    1 / description     |       6       |       5       |   ...

    ---- detail view...
                            Character

                             Text

                             Image

    -----------------
     */

    fun text(side: Side) {
        /*
        return one of the following

        side.text
        side.textStringsId == stringResource(id = ...)
        */
    }

    fun image(side: Side) {
        /*
        return one of the following

        side.imagePath
        side.imageDrawableId == painterResource(id = ...)
         */

    }
}