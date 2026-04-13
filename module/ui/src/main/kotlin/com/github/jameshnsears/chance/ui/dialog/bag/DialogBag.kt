package com.github.jameshnsears.chance.ui.dialog.bag

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

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
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        ),
    ) {
        val view = LocalView.current
        SideEffect {
            (view.parent as? DialogWindowProvider)?.window?.let { window ->
                val controller = WindowCompat.getInsetsController(window, view)
                controller.hide(WindowInsetsCompat.Type.statusBars())
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }

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
