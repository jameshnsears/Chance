package com.github.jameshnsears.chance.ui.dialog.bag

import com.github.jameshnsears.chance.ui.MutableSharedFlowEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

object DialogBagCloseEvent : MutableSharedFlowEvent() {
    private val _sharedFlowDialogBagCloseEvent = MutableSharedFlow<Boolean>()
    val sharedFlowDialogBagCloseEvent: SharedFlow<Boolean> = _sharedFlowDialogBagCloseEvent

    suspend fun emit() {
        Timber.d("emit.DialogBagCloseEvent")
        _sharedFlowDialogBagCloseEvent.emit(true)
    }
}
