package com.github.jameshnsears.chance.ui.tab

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

object DisplayIndexEvent {
    private val _sharedFlowDisplayIndexEvent = MutableSharedFlow<Boolean>()
    val sharedFlowDisplayIndexEvent: SharedFlow<Boolean> = _sharedFlowDisplayIndexEvent

    suspend fun emit() {
        Timber.d("emit.DisplayIndexEvent")
        _sharedFlowDisplayIndexEvent.emit(true)
    }
}
