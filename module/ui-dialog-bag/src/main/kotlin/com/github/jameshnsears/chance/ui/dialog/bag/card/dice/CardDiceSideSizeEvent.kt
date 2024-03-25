package com.github.jameshnsears.chance.ui.dialog.bag.card.dice

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

object CardDiceSideSizeEvent {
    private val _sharedFlowDiceSidesSize = MutableSharedFlow<Int>()
    val sharedFlowDiceSidesSize: SharedFlow<Int> = _sharedFlowDiceSidesSize

    suspend fun emitSideSize(sideSize: Int) {
        Timber.d("sideSize.emit=$sideSize")
        _sharedFlowDiceSidesSize.emit(sideSize)
    }
}
