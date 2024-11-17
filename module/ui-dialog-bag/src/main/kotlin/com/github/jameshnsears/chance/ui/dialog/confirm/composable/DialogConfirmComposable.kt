package com.github.jameshnsears.chance.ui.dialog.confirm.composable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.github.jameshnsears.chance.ui.dialog.bag.R

@Composable
fun DialogConfirm(
    openDialog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    title: String,
    text: String
) {
    if (openDialog) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = title) },
            text = { Text(text = text) },
            confirmButton = {
                TextButton(
                    onClick = onConfirmation
                ) {
                    Text(stringResource(R.string.dialog_bag_delete_confirmation_ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    Text(stringResource(R.string.dialog_bag_delete_confirmation_cancel))
                }
            }
        )
    }
}
