package com.github.jameshnsears.chance.ui.tab

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

object ResizeEvent {
    private val _sharedFlowResizeEvent = MutableSharedFlow<Boolean>()
    val sharedFlowResizeEvent: SharedFlow<Boolean> = _sharedFlowResizeEvent

    suspend fun emit() {
        Timber.d("emit.TabResizeEvent")
        _sharedFlowResizeEvent.emit(true)
    }
}
