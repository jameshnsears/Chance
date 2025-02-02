package com.github.jameshnsears.chance.ui.dialog.bag.composable

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.R
import com.github.jameshnsears.chance.ui.dialog.confirm.composable.DialogConfirm

@Composable
fun TextButtonDelete(
    diceTitleIsUnique: Boolean,
    dialogBagAndroidViewModel: DialogBagAndroidViewModel,
    showDialog: MutableState<Boolean>
) {
    val showDialogConfirm = remember { mutableStateOf(false) }

    TextButton(
        enabled = diceTitleIsUnique,
        onClick = {
            showDialogConfirm.value = true
        },
    ) {
        Text(
            text = stringResource(R.string.dialog_bag_delete),
            textAlign = TextAlign.End,
            fontSize = 16.sp,
        )
    }

    if (showDialogConfirm.value) {
        DialogConfirm(
            openDialog = showDialogConfirm.value,
            onDismissRequest = {
                showDialogConfirm.value = false
            },
            onConfirmation = {
                dialogBagAndroidViewModel.delete()
                showDialog.value = false
            },
            title = stringResource(R.string.dialog_bag_delete_confirmation),
            text = stringResource(R.string.dialog_bag_delete_confirmation_question)
        )
    }
}

@Composable
fun TextButtonClone(
    diceTitleIsUnique: Boolean,
    dialogBagAndroidViewModel: DialogBagAndroidViewModel,
    showDialog: MutableState<Boolean>
) {
    TextButton(
        enabled = diceTitleIsUnique,
        onClick = {
            dialogBagAndroidViewModel.clone()
            showDialog.value = false
        },
    ) {
        Text(
            text = stringResource(R.string.dialog_bag_clone),
            textAlign = TextAlign.End,
            fontSize = 16.sp,
        )
    }
}

@Composable
fun TextButtonSave(
    diceTitleIsUnique: Boolean,
    dialogBagAndroidViewModel: DialogBagAndroidViewModel,
    showDialog: MutableState<Boolean>
) {
    TextButton(
        enabled = diceTitleIsUnique,
        onClick = {
            dialogBagAndroidViewModel.save()
            showDialog.value = false
        },
    ) {
        Text(
            text = stringResource(R.string.dialog_bag_save),
            textAlign = TextAlign.End,
            fontSize = 16.sp,
        )
    }
}
