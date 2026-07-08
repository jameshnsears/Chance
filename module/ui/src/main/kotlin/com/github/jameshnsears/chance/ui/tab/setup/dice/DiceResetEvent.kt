package com.github.jameshnsears.chance.ui.tab.setup.dice

import com.github.jameshnsears.chance.ui.MutableSharedFlowEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

object DiceResetEvent : MutableSharedFlowEvent() {
    private val _sharedFlowTabBagResetEvent = MutableSharedFlow<Boolean>()
    val sharedFlowTabBagResetEvent: SharedFlow<Boolean> = _sharedFlowTabBagResetEvent

    suspend fun emit() {
        Timber.d("emit.DiceResetEvent")
        _sharedFlowTabBagResetEvent.emit(true)
    }
}
