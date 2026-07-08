package com.github.jameshnsears.chance.ui.dialog.dice.card.dice

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.ui.dialog.colour.DialogColourPicker
import com.github.jameshnsears.chance.ui.dialog.dice.card.BagCardColourSample
import kotlin.math.roundToInt


@Composable
fun BagCardDice(
    cardDiceService: CardDiceService,
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 8.dp),
    ) {
        OutlinedCard(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
        ) {
            Column(modifier = Modifier.padding(top = 12.dp)) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    DiceTitle(cardDiceService)
                }

                HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                )

                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                    DiceSides(cardDiceService)

                    DiceColour(cardDiceService)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiceSides(
    cardDiceService: CardDiceService
) {
    val context = LocalContext.current
    val sliderDisplayValues = remember(context) { CardDiceSliderSides(context).values().reversed() }

    val state by cardDiceService.stateFlowCardDice.collectAsStateWithLifecycle()

    DiceSidesSlider(cardDiceService, state, sliderDisplayValues)

    DiceSidesInfo(state.diceSiderInfoColour)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiceSidesSlider(
    cardDiceService: CardDiceService,
    state: CardDiceState,
    sliderDisplayValues: List<String>
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isInteracting by interactionSource.collectIsDraggedAsState()

    val sliderState = rememberSliderState(
        value = state.diceSiderPosition,
        steps = sliderDisplayValues.lastIndex - 1,
        valueRange = 0f..sliderDisplayValues.lastIndex.toFloat()
    )

    LaunchedEffect(state.diceSiderPosition) {
        if (!isInteracting && sliderState.value != state.diceSiderPosition) {
            sliderState.value = state.diceSiderPosition
        }
    }

    // Call diceSidesSize only when dragging has stopped
    LaunchedEffect(isInteracting) {
        if (!isInteracting) {
            val discreteValue = sliderState.value.roundToInt()
            val sideSize = sliderDisplayValues[discreteValue]
            if (sideSize.toInt() != state.diceSidesSize) {
                cardDiceService.diceSidesSize(sideSize)
            }
        }
    }

    Row(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
    ) {
        Icon(
            imageVector = Icons.Outlined.FormatListNumbered,
            contentDescription = "",
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            "${stringResource(R.string.dialog_bag_dice_sides)}: ${sliderDisplayValues[sliderState.value.roundToInt()]}",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .width(80.dp)
        )
    }

    Column(
        modifier = Modifier
            .padding(start = 18.dp, end = 18.dp, top = 12.dp)
            .fillMaxWidth(),
    ) {
        Slider(
            state = sliderState,
            interactionSource = interactionSource,
            thumb = {
                Label(
                    label = {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(38.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.inverseSurface,
                                    shape = CircleShape
                                )
                        ) {
                            Text(
                                text = sliderDisplayValues[sliderState.value.roundToInt()],
                                color = MaterialTheme.colorScheme.inverseOnSurface,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    },
                    interactionSource = interactionSource
                ) {
                    SliderDefaults.Thumb(
                        interactionSource = interactionSource,
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                        )
                    )
                }
            },
            track = {
                SliderDefaults.Track(
                    sliderState = it,
                    colors = SliderDefaults.colors(
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        activeTickColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        inactiveTickColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                )
            },
            modifier = Modifier
                .padding(bottom = 4.dp)
                .minimumInteractiveComponentSize()
                .testTag(CardDiceTestTag.DICE_SIDES),
        )
    }
}

@Composable
private fun DiceSidesInfo(sliderInfoColour: Float) {
    Row(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = stringResource(R.string.info),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = sliderInfoColour),
        )
    }

    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.dialog_bag_dice_sides_info),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = sliderInfoColour),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
fun DiceTitle(cardDiceService: CardDiceService) {
    val stateFlowCardDice =
        cardDiceService.stateFlowCardDice.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val diceTitle = stateFlowCardDice.value.diceTitle

    OutlinedTextField(
        value = diceTitle,
        onValueChange = {
            if (it.length <= 25)
                cardDiceService.diceTitle(it)
        },
        label = { Text(stringResource(R.string.dialog_bag_dice_title)) },
        supportingText = {
            Text(
                text = "${diceTitle.length} / 25",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Title,
                contentDescription = "",
            )
        },
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
            .testTag(CardDiceTestTag.DICE_TITLE),
        trailingIcon = {
            if (diceTitle.isNotEmpty()) {
                IconButton(
                    onClick = {
                        cardDiceService.diceTitle(
                            ""
                        )
                    },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(
                        painterResource(id = R.drawable.cancel),
                        contentDescription = stringResource(R.string.dialog_cancel),
                    )
                }
            }
        }
    )

    Row(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = stringResource(R.string.info),
        )
    }

    Row(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.dialog_bag_dice_title_info_0),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
fun DiceColour(cardDiceService: CardDiceService) {
    val showDialogColourPicker = rememberSaveable { mutableStateOf(false) }

    val stateFlowCardDice =
        cardDiceService.stateFlowCardDice.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val diceColour = stateFlowCardDice.value.diceColour

    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = {
                showDialogColourPicker.value = true
            },
            modifier = Modifier
                .width(180.dp)
                .minimumInteractiveComponentSize()
                .testTag(CardDiceTestTag.DICE_COLOUR),

            ) {
            val palettePainter = painterResource(id = R.drawable.palette)

            val iconModifier = remember { Modifier.size(24.dp) }

            Icon(
                palettePainter,
                contentDescription = stringResource(R.string.colour),
                modifier = iconModifier,
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_dice_colour))
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(modifier = Modifier.padding(end = 4.dp)) {
            BagCardColourSample(diceColour)
        }
    }

    if (showDialogColourPicker.value) {
        DialogColourPicker(
            showDialogColourPicker,
            stringResource(R.string.dialog_bag_colour_picker_dice),
            diceColour,
            cardDiceService::diceColour,
        )
    }
}
