package com.github.jameshnsears.chance.ui.dialog.bag

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun DialogBag(
    showDialog: MutableState<Boolean>,
    dialogBagAndroidViewModel: DialogBagAndroidViewModel
) {
    Dialog(
        onDismissRequest = {
            showDialog.value = false
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            DialogBagTabLayout(
                showDialog,
                dialogBagAndroidViewModel,
            )
        }
    }
}
