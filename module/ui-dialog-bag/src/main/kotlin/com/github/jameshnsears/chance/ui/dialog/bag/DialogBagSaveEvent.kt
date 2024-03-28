package com.github.jameshnsears.chance.ui.dialog.bag

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

object DialogBagSaveEvent {
    private val _sharedFlowDialogBagSave = MutableSharedFlow<Boolean>()
    val sharedFlowDialogBagSave: SharedFlow<Boolean> = _sharedFlowDialogBagSave

    suspend fun emitSave() {
        Timber.d("saveEmit")
        _sharedFlowDialogBagSave.emit(true)
    }
}
