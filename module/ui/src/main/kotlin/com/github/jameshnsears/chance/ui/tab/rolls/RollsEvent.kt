package com.github.jameshnsears.chance.ui.tab.rolls

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

object RollsEvent {
    private val _sharedFlowTabRollUndoEvent = MutableSharedFlow<Long>()
    val sharedFlowTabRollEvent: SharedFlow<Long> = _sharedFlowTabRollUndoEvent

    suspend fun emit() {
        Timber.d("emit.RollsEvent")
        _sharedFlowTabRollUndoEvent.emit(System.currentTimeMillis())
    }
}
