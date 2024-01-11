package com.github.jameshnsears.chance.data.repository.bag

import android.content.Context
import com.github.jameshnsears.chance.data.domain.Dice

class BagRepository private constructor(val context: Context?) :
    BagRepositoryInterface {
    companion object {
        private var instance: BagRepository? = null

        fun getInstance(context: Context?): BagRepository {
            if (instance == null) {
                instance = BagRepository(context)
            }
            return instance!!
        }
    }

    override fun fetch(): List<Dice> {
        TODO("Not yet implemented")
    }

    override fun store(newBag: List<Dice>) {
        TODO("Not yet implemented")
    }

    override suspend fun export(): String {
        TODO("Not yet implemented")
    }

    override suspend fun import(json: String) {
        TODO("Not yet implemented")
    }
}
