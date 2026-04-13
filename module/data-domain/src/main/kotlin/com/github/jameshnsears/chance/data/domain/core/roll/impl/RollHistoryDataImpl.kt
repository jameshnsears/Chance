package com.github.jameshnsears.chance.data.domain.core.roll.impl

import com.github.jameshnsears.chance.data.domain.core.bag.BagDataInterface
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistory
import com.github.jameshnsears.chance.data.domain.core.roll.RollHistoryDataInterface

class RollHistoryDataImpl(bagData: BagDataInterface) : RollHistoryDataInterface {
    override var rollHistory: RollHistory = linkedMapOf()
}
