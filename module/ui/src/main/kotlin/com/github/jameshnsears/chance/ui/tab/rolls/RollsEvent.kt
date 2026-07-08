package com.github.jameshnsears.chance.ui.tab.rolls

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

object RollsEvent {
    private val _sharedFlowTabRollUndoEvent = MutableSharedFlow<Boolean>()
    val sharedFlowTabRollEvent: SharedFlow<Boolean> = _sharedFlowTabRollUndoEvent

    suspend fun emit() {
        Timber.d("emit.RollsEvent")
        _sharedFlowTabRollUndoEvent.emit(true)
    }
}
