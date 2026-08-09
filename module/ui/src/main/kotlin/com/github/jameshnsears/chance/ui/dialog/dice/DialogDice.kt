package com.github.jameshnsears.chance.ui.dialog.dice

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun DialogBag(
    onClose: () -> Unit,
    dialogDiceAndroidViewModel: DialogDiceAndroidViewModel,
) {
    val view = LocalView.current
    SideEffect {
        val window = (view.parent as? DialogWindowProvider)?.window
            ?: (view.context as? android.app.Activity)?.window

        window?.let {
            val controller = WindowCompat.getInsetsController(it, view)
            controller.hide(WindowInsetsCompat.Type.statusBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        DialogBagTabLayout(
            onClose,
            dialogDiceAndroidViewModel,
        )
    }
}
