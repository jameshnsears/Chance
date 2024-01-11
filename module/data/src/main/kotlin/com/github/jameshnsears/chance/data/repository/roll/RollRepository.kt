package com.github.jameshnsears.chance.data.repository.roll

import android.content.Context
import com.github.jameshnsears.chance.data.domain.RollHistory

class RollRepository private constructor(val context: Context?) :
    RollRepositoryInterface {
    companion object {
        private var instance: RollRepository? = null

        fun getInstance(context: Context?): RollRepository {
            if (instance == null) {
                instance = RollRepository(context)
            }
            return instance!!
        }
    }

    override fun fetch(): RollHistory {
        TODO("Not yet implemented")
    }

    override fun store(newRollHistory: RollHistory) {
        TODO("Not yet implemented")
    }

    override suspend fun export(): String {
        TODO("Not yet implemented")
    }

    override suspend fun import(json: String) {
        TODO("Not yet implemented")
    }
}
