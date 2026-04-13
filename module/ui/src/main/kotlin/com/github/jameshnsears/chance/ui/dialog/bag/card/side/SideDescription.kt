package com.github.jameshnsears.chance.ui.dialog.bag.card.side

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.ui.R
import com.github.jameshnsears.chance.ui.dialog.bag.card.BagCardColourSample
import com.github.jameshnsears.chance.ui.dialog.colour.DialogColourPicker


@Composable
fun SideDescription(
    cardSideAndroidViewModel: CardSideAndroidViewModel
) {
    val stateFlowCardSide =
        cardSideAndroidViewModel.stateFlowCardSide.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val sideDescription = stateFlowCardSide.value.sideDescription

    val diceSidesFewerThanSdeNumber = stateFlowCardSide.value.diceSidesFewerThanSideNumber

    OutlinedTextField(
        value = sideDescription,
        onValueChange = {
            if (it.length <= 25) {
                cardSideAndroidViewModel.sideDescription(it)
            }
        },
        label = { Text(stringResource(R.string.dialog_bag_side_description)) },
        singleLine = true,
        modifier = Modifier
            .testTag(SideTestTag.SIDE_DESCRIPTION)
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth(),
        enabled = !diceSidesFewerThanSdeNumber,
        trailingIcon = {
            if (sideDescription.isNotEmpty()) {
                IconButton(onClick = {
                    cardSideAndroidViewModel.sideDescription("")
                }
                ) {
                    Icon(
                        painterResource(id = R.drawable.cancel),
                        contentDescription = ""
                    )
                }
            }
        }
    )

    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = "",
        )
    }

    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.dialog_bag_side_description_info),
        )
    }

    SideDescriptionColour(cardSideAndroidViewModel)

    SideDescriptionAndColourApplyToAll(
        stateFlowCardSide.value.sideApplyToAllDescription,
        SideTestTag.SIDE_APPLY_DESCRIPTION,
        cardSideAndroidViewModel::sideApplyToAllDescription,
        R.string.dialog_bag_side_description_apply_to_all,
        diceSidesFewerThanSdeNumber
    )
}

@Composable
fun SideDescriptionColour(cardSideAndroidViewModel: CardSideAndroidViewModel) {
    val showDialogColourPicker = rememberSaveable { mutableStateOf(false) }

    val stateFlowCardSide =
        cardSideAndroidViewModel.stateFlowCardSide.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val descriptionColour = stateFlowCardSide.value.sideDescriptionColour

    val diceSidesFewerThanSdeNumber = stateFlowCardSide.value.diceSidesFewerThanSideNumber

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 12.dp, bottom = 8.dp),
    ) {
        Button(
            onClick = { showDialogColourPicker.value = true },
            modifier = Modifier
                .width(180.dp)
                .testTag(SideTestTag.SIDE_DESCRIPTION_COLOUR),
            enabled = !diceSidesFewerThanSdeNumber
        ) {
            Icon(
                painterResource(id = R.drawable.palette),
                contentDescription = "",
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_side_description_colour))
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(modifier = Modifier.padding(end = 8.dp)) {
            BagCardColourSample(descriptionColour)
        }
    }

    if (showDialogColourPicker.value) {
        DialogColourPicker(
            showDialogColourPicker,
            stringResource(R.string.dialog_bag_colour_picker_description),
            descriptionColour,
            cardSideAndroidViewModel::sideDescriptionColour,
        )
    }
}


@Composable
fun SideDescriptionAndColourApplyToAll(
    sideApplyToAll: Boolean,
    testTag: String,
    sideApplyToAllFunction: (Boolean) -> Unit,
    stringResourceId: Int = R.string.dialog_bag_side_image_apply_to_all,
    diceSidesFewerThanSideNumber: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 0.dp, end = 2.dp)
            .testTag(testTag)
            .clickable(enabled = !diceSidesFewerThanSideNumber) {
                sideApplyToAllFunction(!sideApplyToAll)
            }
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(stringResourceId)
        )

        Switch(
            checked = sideApplyToAll,
            onCheckedChange = {
                sideApplyToAllFunction(it)
            },
            enabled = !diceSidesFewerThanSideNumber
        )
    }
}
