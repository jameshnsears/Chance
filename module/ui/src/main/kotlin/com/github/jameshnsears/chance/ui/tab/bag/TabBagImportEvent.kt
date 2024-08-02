package com.github.jameshnsears.chance.ui.tab.bag

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

object TabBagImportEvent {
    private val _sharedFlowTabBagImportEvent = MutableSharedFlow<Boolean>()
    val sharedFlowTabBagImportEvent: SharedFlow<Boolean> = _sharedFlowTabBagImportEvent

    suspend fun emit() {
        Timber.d("emit.TabBagImportEvent")
        _sharedFlowTabBagImportEvent.emit(true)
    }
}
