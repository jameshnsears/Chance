package com.github.jameshnsears.chance.ui.dialog.bag.card

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModelInterface
import com.github.jameshnsears.chance.ui.dialog.bag.card.colour.DialogColourPicker
import com.github.jameshnsears.chance.ui.dialog.dice.R
import kotlin.math.roundToInt

class BagCardDiceTestTag {
    companion object {
        const val diceSides = "diceSides"
        const val diceTitle = "diceTitle"
        const val diceColour = "diceColour"
        const val diceClone = "diceClone"
        const val diceDelete = "diceDelete"
    }
}

@Composable
fun BagCardDice(
    dialogBagAndroidViewModelInterface: DialogBagAndroidViewModelInterface,
) {
    val diceSidesSliderPosition =
        remember { mutableFloatStateOf(dialogBagAndroidViewModelInterface.diceSidesSliderPosition.value) }

    ElevatedCard(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),

        ) {
        Column(
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.dialog_bag_dice),
                modifier = Modifier
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            DiceSides(
                stringResource(R.string.dialog_bag_dice_sides),
                BagCardDiceSliderSides(LocalContext.current).values(),
                diceSidesSliderPosition,
                dialogBagAndroidViewModelInterface::diceSidesSliderPosition,
                BagCardDiceTestTag.diceSides
            )

            DiceTitle(dialogBagAndroidViewModelInterface)

            DiceColour(dialogBagAndroidViewModelInterface)

            DiceCloneDelete(dialogBagAndroidViewModelInterface)
        }
    }
}

@Composable
fun DiceSides(
    sliderTitle: String,
    sliderDisplayValues: List<String>,
    sliderPosition: MutableFloatState,
    diceSidesSliderPosition: (Float) -> Unit,
    testTag: String
) {
    Row {
        Text(
            "${sliderTitle}: ${
                sliderDisplayValues[sliderPosition.floatValue.roundToInt()]
            }",
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Slider(
            value = sliderPosition.floatValue,
            onValueChange = {
                sliderPosition.floatValue = it
                diceSidesSliderPosition(it)
            },
            valueRange = 0f..sliderDisplayValues.lastIndex.toFloat(),
            steps = sliderDisplayValues.lastIndex - 1,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.testTag(testTag),
        )
    }
}

@Composable
fun DiceTitle(dialogBagAndroidViewModelInterface: DialogBagAndroidViewModelInterface) {
    val diceTitle by dialogBagAndroidViewModelInterface.diceTitle.collectAsStateWithLifecycle()

    OutlinedTextField(
        value = diceTitle,
        onValueChange = { dialogBagAndroidViewModelInterface.diceTitle(it) },
        label = { Text(stringResource(R.string.dialog_bag_dice_title)) },
        singleLine = true,
        modifier = Modifier
            .testTag(BagCardDiceTestTag.diceTitle)
            .fillMaxWidth()
    )

    Icon(
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
        imageVector = Icons.Outlined.Info,
        contentDescription = ""
    )

    Text(stringResource(R.string.dialog_bag_dice_title_info))
}

@Composable
fun DiceColour(dialogBagAndroidViewModelInterface: DialogBagAndroidViewModelInterface) {
    val diceColour by dialogBagAndroidViewModelInterface.diceColour.collectAsStateWithLifecycle()

    val paletteIcon = painterResource(id = R.drawable.palette_fill0_wght400_grad0_opsz24)

    val showDialogColourPicker = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(top = 16.dp, bottom = 8.dp)
    ) {
        Button(
            onClick = {
                showDialogColourPicker.value = true
            },
            modifier = Modifier
                .width(140.dp)
                .testTag(BagCardDiceTestTag.diceColour)

        ) {
            Icon(
                paletteIcon,
                contentDescription = stringResource(R.string.dialog_bag_dice_colour),
                modifier = Modifier.size(24.dp)
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_dice_colour))
        }

        Text(diceColour)
    }

    if (showDialogColourPicker.value) {
        DialogColourPicker(
            showDialogColourPicker,
            stringResource(R.string.dialog_bag_colour_dice)
        )
    }
}

@Composable
fun DiceCloneDelete(
    dialogBagAndroidViewModelInterface: DialogBagAndroidViewModelInterface
) {
    val cloneDice = painterResource(id = R.drawable.content_copy_fill0_wght400_grad0_opsz24)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = { },
            modifier = Modifier
                .padding(8.dp)
                .width(140.dp)
                .testTag(BagCardDiceTestTag.diceClone)
        ) {
            Icon(
                cloneDice,
                contentDescription = stringResource(R.string.dialog_bag_dice_delete),
                modifier = Modifier.size(24.dp)
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_dice_clone))
        }

        if (dialogBagAndroidViewModelInterface.diceCanBeDeleted()) {
            Button(
                onClick = { },
                modifier = Modifier
                    .padding(8.dp)
                    .width(140.dp)
                    .testTag(BagCardDiceTestTag.diceDelete)
            ) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = stringResource(R.string.dialog_bag_dice_delete)
                )

                Spacer(Modifier.size(ButtonDefaults.IconSpacing))

                Text(stringResource(R.string.dialog_bag_dice_delete))
            }
        }
    }
}
