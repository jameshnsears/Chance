package com.github.jameshnsears.chance.ui.zoom.roll

import com.github.jameshnsears.chance.data.roll.repository.RollRepositoryInterface
import com.github.jameshnsears.chance.ui.zoom.ZoomViewModel

class ZoomRollViewModel(
    val rollRepository: RollRepositoryInterface,
) : ZoomViewModel() {
    override fun zoom() {}
}