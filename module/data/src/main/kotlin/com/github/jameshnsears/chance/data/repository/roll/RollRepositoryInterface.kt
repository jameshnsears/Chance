package com.github.jameshnsears.chance.data.repository.roll

import com.github.jameshnsears.chance.data.domain.RollHistory
import com.github.jameshnsears.chance.data.repository.ImportExportRepositoryInterface

interface RollRepositoryInterface : ImportExportRepositoryInterface {
    fun fetch(): RollHistory
    fun store(newRollHistory: RollHistory)
}
