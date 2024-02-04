package com.github.jameshnsears.chance.data.repository.bag

import android.content.Context
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.DiceBag

class DiceBagRepository private constructor(val context: Context?) :
    DiceBagRepositoryInterface {
    companion object {
        private var instance: DiceBagRepository? = null

        fun getInstance(context: Context?): DiceBagRepository {
            if (instance == null) {
                instance = DiceBagRepository(context)
            }
            return instance!!
        }
    }

    override suspend fun fetch(): DiceBag {
        TODO("use explicit coroutine here")
    }

    override suspend fun fetch(epoch: Long): Dice {
        TODO("use explicit coroutine here")
    }

    override suspend fun store(newDiceBag: DiceBag) {
        TODO("use explicit coroutine here")
    }

    override suspend fun export(): String {
        TODO("Not yet implemented")
    }

    override suspend fun import(json: String) {
        TODO("Not yet implemented")
    }
}
