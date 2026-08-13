package com.github.jameshnsears.chance.ui.tab.setup.dice

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

object DiceResizeEvent {
    private val _sharedFlowResizeEvent = MutableSharedFlow<Float>(extraBufferCapacity = 10)
    val sharedFlowResizeEvent: SharedFlow<Float> = _sharedFlowResizeEvent

    suspend fun emit(resizeZoom: Float) {
        Timber.d("emit.DiceResizeEvent: $resizeZoom")
        _sharedFlowResizeEvent.emit(resizeZoom)
    }

    fun tryEmit(resizeZoom: Float) {
        Timber.d("tryEmit.DiceResizeEvent: $resizeZoom")
        _sharedFlowResizeEvent.tryEmit(resizeZoom)
    }
}
