package com.github.jameshnsears.chance.ui.dialog.bag.card.dice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.ui.dialog.bag.card.BgCardColourSample
import com.github.jameshnsears.chance.ui.dialog.bag.colour.DialogColourPicker
import com.github.jameshnsears.chance.ui.dialog.dice.R
import kotlin.math.roundToInt

class BagCardDiceTestTag {
    companion object {
        const val DICE_TITLE = "DICE_TITLE"
        const val DICE_SIDES = "DICE_SIDES"
        const val DICE_COLOUR = "DICE_COLOUR"
        const val DICE_CLONE = "DICE_CLONE"
        const val DICE_DELETE = "DICE_DELETE"
    }
}

@Composable
fun BagCardDice(
    bagCardDiceAndroidViewModel: BagCardDiceAndroidViewModel,
) {

    ElevatedCard(
        modifier = Modifier
            .padding(top = 16.dp, bottom = 16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
        ),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = stringResource(R.string.dialog_bag_dice),
                modifier = Modifier
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )

            DiceTitle(bagCardDiceAndroidViewModel)

            DiceSides(bagCardDiceAndroidViewModel)

            DiceColour(bagCardDiceAndroidViewModel)

            DiceCloneDelete(bagCardDiceAndroidViewModel)
        }
    }
}

@Composable
fun DiceSides(
    bagCardDiceAndroidViewModel: BagCardDiceAndroidViewModel
) {
    val sliderDisplayValues = BagCardDiceSliderSides(LocalContext.current).values()

    val stateFlow =
        bagCardDiceAndroidViewModel.stateFlowDice.collectAsStateWithLifecycle()

    var sliderPosition = stateFlow.value.diceSidesPosition

    Row(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)) {
        Text(
            "${stringResource(R.string.dialog_bag_dice_sides)}: ${sliderDisplayValues[sliderPosition.roundToInt()]}",
            modifier = Modifier.align(Alignment.CenterVertically),
        )

        Spacer(modifier = Modifier.width(10.dp))

        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                bagCardDiceAndroidViewModel.diceSidesPosition(it)
                bagCardDiceAndroidViewModel.diceSidesSize(sliderDisplayValues[sliderPosition.roundToInt()])
            },
            valueRange = 0f..sliderDisplayValues.lastIndex.toFloat(),
            steps = sliderDisplayValues.lastIndex - 1,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.testTag(BagCardDiceTestTag.DICE_SIDES),
        )
    }
}

@Composable
fun DiceTitle(bagCardDiceAndroidViewModel: BagCardDiceAndroidViewModel) {
    val stateFlow =
        bagCardDiceAndroidViewModel.stateFlowDice.collectAsStateWithLifecycle()

    val diceTitle = stateFlow.value.diceTitle

    OutlinedTextField(
        value = diceTitle,
        onValueChange = {
            if (it.length <= 30)
                bagCardDiceAndroidViewModel.diceTitle(it)
        },
        label = { Text(stringResource(R.string.dialog_bag_dice_title)) },
        singleLine = true,
        modifier = Modifier
            .testTag(BagCardDiceTestTag.DICE_TITLE)
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 12.dp)
            .testTag(BagCardDiceTestTag.DICE_TITLE),
        trailingIcon = {
            if (diceTitle.isNotEmpty()) {
                IconButton(onClick = {
                    bagCardDiceAndroidViewModel.diceTitle(
                        ""
                    )
                }) {
                    Icon(
                        painterResource(id = R.drawable.cancel_fill0_wght400_grad0_opsz24),
                        contentDescription = ""
                    )
                }
            }
        }
    )

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = "",
        )

        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = stringResource(R.string.dialog_bag_dice_title_info),
        )
    }

    HorizontalDivider(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 2.dp)
    )
}

@Composable
fun DiceColour(bagCardDiceAndroidViewModel: BagCardDiceAndroidViewModel) {
    val showDialogColourPicker = remember { mutableStateOf(false) }

    val stateFlow =
        bagCardDiceAndroidViewModel.stateFlowDice.collectAsStateWithLifecycle()

    val diceColour = stateFlow.value.diceColour

    Row(
        modifier = Modifier
            .padding(bottom = 4.dp)
            .fillMaxWidth()
            .testTag(BagCardDiceTestTag.DICE_COLOUR),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = {
                showDialogColourPicker.value = true
            },
            modifier = Modifier
                .width(140.dp)
                .testTag(BagCardDiceTestTag.DICE_COLOUR),

            ) {
            Icon(
                painterResource(id = R.drawable.palette_fill0_wght400_grad0_opsz24),
                contentDescription = stringResource(R.string.dialog_bag_dice_colour),
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_dice_colour))
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(modifier = Modifier.padding(end = 4.dp)) {
            BgCardColourSample(diceColour)
        }
    }

    if (showDialogColourPicker.value) {
        DialogColourPicker(
            showDialogColourPicker,
            stringResource(R.string.dialog_bag_colour_picker_dice),
            diceColour,
            bagCardDiceAndroidViewModel::diceColour,
        )
    }
}

@Composable
fun DiceCloneDelete(
    bagCardDiceAndroidViewModel: BagCardDiceAndroidViewModel,
) {
    val stateFlow =
        bagCardDiceAndroidViewModel.stateFlowDice.collectAsStateWithLifecycle()

    val diceCanBeDeleted = stateFlow.value.diceCanBeDeleted

    val diceClone = stateFlow.value.diceClone

    val diceDelete = stateFlow.value.diceDelete

    HorizontalDivider(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Checkbox(
                checked = diceClone,
                onCheckedChange = {
                    bagCardDiceAndroidViewModel.diceClone(it)
                    bagCardDiceAndroidViewModel.diceDelete(false)
                },
                modifier = Modifier.testTag(BagCardDiceTestTag.DICE_CLONE)
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Icon(
                painterResource(id = R.drawable.content_copy_fill0_wght400_grad0_opsz24),
                contentDescription = stringResource(R.string.dialog_bag_dice_clone),
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_dice_clone))
        }

        if (diceCanBeDeleted) {
            Spacer(modifier = Modifier.padding(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Checkbox(
                    checked = diceDelete,
                    onCheckedChange = {
                        bagCardDiceAndroidViewModel.diceClone(false)
                        bagCardDiceAndroidViewModel.diceDelete(it)
                    },
                    modifier = Modifier.testTag(BagCardDiceTestTag.DICE_DELETE)
                )

                Spacer(Modifier.size(ButtonDefaults.IconSpacing))

                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = stringResource(R.string.dialog_bag_dice_delete),
                )

                Spacer(Modifier.size(ButtonDefaults.IconSpacing))

                Text(stringResource(R.string.dialog_bag_dice_delete))
            }
        }
    }
}
