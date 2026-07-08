package com.github.jameshnsears.chance.ui.tab

import com.github.jameshnsears.chance.ui.MutableSharedFlowEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

object GroupEvent : MutableSharedFlowEvent() {
    private val _sharedFlowGroupEvent = MutableSharedFlow<Boolean>()
    val sharedFlowGroupEvent: SharedFlow<Boolean> = _sharedFlowGroupEvent

    suspend fun emit() {
        Timber.d("emit.GroupEvent")
        _sharedFlowGroupEvent.emit(true)
    }
}
