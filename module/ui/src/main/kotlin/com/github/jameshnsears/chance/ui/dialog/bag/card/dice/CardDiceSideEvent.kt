package com.github.jameshnsears.chance.ui.dialog.bag.card.dice

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

object CardDiceSideEvent {
    private val _sharedFlowDiceSide = MutableSharedFlow<Int>()
    val sharedFlowDiceSide: SharedFlow<Int> = _sharedFlowDiceSide

    suspend fun emit(sideSize: Int) {
        Timber.d("emit.CardDiceSideEvent")
        _sharedFlowDiceSide.emit(sideSize)
    }
}
