package com.github.jameshnsears.chance.ui.dialog.bag.composable

import android.app.Application
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.repository.bag.RepositoryBagInterface
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModel
import timber.log.Timber

@Composable
fun DialogBag(
    showDialog: MutableState<Boolean>,
    repositoryBag: RepositoryBagInterface,
    dice: Dice,
    side: Side,
) {
    val application = LocalContext.current.applicationContext as Application

    Timber.d("DialogBag: dice.epoch=${dice.epoch}; side.uuid=${side.uuid}")

    Dialog(
        onDismissRequest = {
            showDialog.value = false
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            DialogBagTabLayout(
                showDialog,
                DialogBagAndroidViewModel(
                    application,
                    repositoryBag,
                    dice,
                    side,
                ),
            )
        }
    }
}
