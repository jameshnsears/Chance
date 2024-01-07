package com.github.jameshnsears.chance.ui.zoom.bag

import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryInterface
import com.github.jameshnsears.chance.ui.zoom.ZoomViewModel

class ZoomBagViewModel(
    val bagRepository: BagRepositoryInterface
) : ZoomViewModel() {
    override fun zoom() {}

}