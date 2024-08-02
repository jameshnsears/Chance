package com.github.jameshnsears.chance.ui.tab.roll

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

object TabRollEvent {
    private val _sharedFlowTabRollUndoEvent = MutableSharedFlow<Boolean>()
    val sharedFlowTabRollEvent: SharedFlow<Boolean> = _sharedFlowTabRollUndoEvent

    suspend fun emit() {
        Timber.d("emit.TabRollEvent")
        _sharedFlowTabRollUndoEvent.emit(true)
    }
}
