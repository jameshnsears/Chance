package com.github.jameshnsears.chance.ui.tab.bag

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

object TabBagResetStorageEvent {
    private val _sharedFlowTabBagResetStorageEvent = MutableSharedFlow<Boolean>()
    val sharedFlowTabBagResetStorageEvent: SharedFlow<Boolean> = _sharedFlowTabBagResetStorageEvent

    suspend fun emit() {
        Timber.d("emit.TabBagImportEvent")
        _sharedFlowTabBagResetStorageEvent.emit(true)
    }
}
