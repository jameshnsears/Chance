package com.github.jameshnsears.chance.ui.dialog.bag

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.jameshnsears.chance.ui.dialog.bag.card.BagCardDice
import com.github.jameshnsears.chance.ui.dialog.bag.card.BagCardSide
import com.github.jameshnsears.chance.ui.dialog.dice.R


@Composable
fun DialogBag(
    showDialog: MutableState<Boolean>,
    viewModel: DialogBagAndroidViewModelInterface,
) {
    Dialog(
        onDismissRequest = { showDialog.value = false },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            DialogBagLayout(
                showDialog,
                viewModel,
            )
        }
    }
}

@Composable
fun DialogBagLayout(
    showDialog: MutableState<Boolean>,
    dialogBagAndroidViewModelInterface: DialogBagAndroidViewModelInterface,
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            IconButton(onClick = { showDialog.value = false }) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = ""
                )
            }

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(
                text = stringResource(R.string.dialog_bag_title),
                textAlign = TextAlign.Center,
                fontSize = 22.sp
            )

            TextButton(onClick = {
                dialogBagAndroidViewModelInterface.save()
                showDialog.value = false
            }) {
                Text(
                    text = stringResource(R.string.dialog_bag_save),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp),
                    textAlign = TextAlign.End,
                    fontSize = 14.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
        ) {
            BagCardSide(dialogBagAndroidViewModelInterface)

            BagCardDice(dialogBagAndroidViewModelInterface)
        }
    }
}
