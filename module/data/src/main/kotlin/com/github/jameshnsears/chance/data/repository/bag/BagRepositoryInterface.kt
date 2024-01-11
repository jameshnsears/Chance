package com.github.jameshnsears.chance.data.repository.bag

import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.repository.ImportExportRepositoryInterface

interface BagRepositoryInterface : ImportExportRepositoryInterface {
    fun fetch(): List<Dice>
    fun store(newBag: List<Dice>)
}