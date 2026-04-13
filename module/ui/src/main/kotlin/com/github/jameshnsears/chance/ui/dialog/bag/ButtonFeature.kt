package com.github.jameshnsears.chance.ui.dialog.bag

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.ui.dialog.bag.card.dice.CardDiceState
import com.github.jameshnsears.chance.ui.dialog.bag.card.roll.CardRollState
import com.github.jameshnsears.chance.ui.dialog.bag.card.side.CardSideState
import com.github.jameshnsears.chance.ui.dialog.confirm.DialogConfirm

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
    cardDiceState: CardDiceState,
    cardRollState: CardRollState,
    showDialog: MutableState<Boolean>
) {
    TextButton(
        enabled = diceTitleIsUnique,
        onClick = {
            dialogBagAndroidViewModel.clone(cardDiceState, cardRollState)
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
    cardDiceState: CardDiceState,
    cardRollState: CardRollState,
    cardSideState: CardSideState,
    showDialog: MutableState<Boolean>
) {
    TextButton(
        enabled = diceTitleIsUnique,
        onClick = {
            dialogBagAndroidViewModel.save(cardDiceState, cardRollState, cardSideState)
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
