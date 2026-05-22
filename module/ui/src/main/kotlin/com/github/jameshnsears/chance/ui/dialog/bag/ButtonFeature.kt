package com.github.jameshnsears.chance.ui.dialog.bag

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
        modifier = Modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            .testTag(ButtonFeatureTestTag.BUTTON_FEATURE_DELETE),
        onClick = {
            showDialogConfirm.value = true
        },
    ) {
        Text(
            text = stringResource(R.string.dialog_bag_delete),
            textAlign = TextAlign.End
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
        modifier = Modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            .padding(start = 12.dp, end = 12.dp)
            .testTag(ButtonFeatureTestTag.BUTTON_FEATURE_CLONE),
        enabled = diceTitleIsUnique,
        onClick = {
            dialogBagAndroidViewModel.clone(cardDiceState, cardRollState)
            showDialog.value = false
        },
    ) {
        Text(
            text = stringResource(R.string.dialog_bag_clone),
            textAlign = TextAlign.End
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
        modifier = Modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            .testTag(ButtonFeatureTestTag.BUTTON_FEATURE_SAVE),
        enabled = diceTitleIsUnique,
        onClick = {
            dialogBagAndroidViewModel.save(cardDiceState, cardRollState, cardSideState)
            showDialog.value = false
        },
    ) {
        Text(
            text = stringResource(R.string.dialog_bag_save),
            textAlign = TextAlign.End
        )
    }
}
