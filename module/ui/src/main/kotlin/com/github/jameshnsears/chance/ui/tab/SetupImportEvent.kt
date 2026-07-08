package com.github.jameshnsears.chance.ui.tab

import com.github.jameshnsears.chance.ui.MutableSharedFlowEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

object SetupImportEvent : MutableSharedFlowEvent() {
    private val _sharedFlowTabBagImportEvent = MutableSharedFlow<Boolean>()
    val sharedFlowTabBagImportEvent: SharedFlow<Boolean> = _sharedFlowTabBagImportEvent

    suspend fun emit() {
        Timber.d("emit.SetupImportEvent")
        _sharedFlowTabBagImportEvent.emit(true)
    }
}
