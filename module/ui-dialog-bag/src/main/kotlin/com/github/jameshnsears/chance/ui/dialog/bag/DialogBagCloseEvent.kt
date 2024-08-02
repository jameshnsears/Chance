package com.github.jameshnsears.chance.ui.dialog.bag

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

object DialogBagCloseEvent {
    private val _sharedFlowDialogBagCloseEvent = MutableSharedFlow<Boolean>()
    val sharedFlowDialogBagCloseEvent: SharedFlow<Boolean> = _sharedFlowDialogBagCloseEvent

    suspend fun emit() {
        Timber.d("emit.DialogBagCloseEvent")
        _sharedFlowDialogBagCloseEvent.emit(true)
    }
}
