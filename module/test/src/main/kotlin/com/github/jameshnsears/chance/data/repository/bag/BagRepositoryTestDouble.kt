package com.github.jameshnsears.chance.data.repository.bag

import com.github.jameshnsears.chance.data.domain.Dice

class BagRepositoryTestDouble private constructor() :
    BagRepositoryInterface {
    companion object {
        private var instance: BagRepositoryTestDouble? = null

        fun getInstance(): BagRepositoryTestDouble {
            if (instance == null) {
                instance = BagRepositoryTestDouble()
            }
            return instance!!
        }
    }

    private lateinit var bag: List<Dice>

    override suspend fun export(): String {
        TODO("Not yet implemented")
    }

    override suspend fun import(json: String) {
        TODO("Not yet implemented")
    }

    override fun fetch(): List<Dice> {
        return bag
    }

    override fun store(newBag: List<Dice>) {
        bag = newBag
    }
}
