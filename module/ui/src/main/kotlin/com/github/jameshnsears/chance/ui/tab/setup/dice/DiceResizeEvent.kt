package com.github.jameshnsears.chance.ui.tab.setup.dice

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

object DiceResizeEvent {
    private val _sharedFlowResizeEvent = MutableSharedFlow<Boolean>()
    val sharedFlowResizeEvent: SharedFlow<Boolean> = _sharedFlowResizeEvent

    suspend fun emit() {
        Timber.d("emit.DiceResizeEvent")
        _sharedFlowResizeEvent.emit(true)
    }
}
