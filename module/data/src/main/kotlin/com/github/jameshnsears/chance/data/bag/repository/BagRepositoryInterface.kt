package com.github.jameshnsears.chance.data.bag.repository

import com.github.jameshnsears.chance.data.domain.Dice

interface BagRepositoryInterface {
    fun fetch(): List<Dice>
    fun store(newBag: List<Dice>)
}