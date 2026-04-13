package com.github.jameshnsears.chance.ui.dialog.bag.card.side

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.ui.dialog.bag.card.BagCardColourSample
import com.github.jameshnsears.chance.ui.dialog.colour.DialogColourPicker

@Composable
fun BagCardSide(cardSideService: CardSideService) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 8.dp)
    ) {
        OutlinedCard(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp,
            ),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                SideNumber(cardSideService)

                SideColour(cardSideService)

                HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                )

                SideImageSVG(cardSideService)

                HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                )

                SideDescription(cardSideService)
            }
        }
    }
}

@Composable
fun SideNumber(cardSideService: CardSideService) {
    val stateFlowCardSide =
        cardSideService.stateFlowCardSide.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    Text(
        text = "${stringResource(R.string.dialog_bag_side)} ${stateFlowCardSide.value.sideNumber}",
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .padding(top = 4.dp, bottom = 8.dp)
            .testTag(SideTestTag.SIDE_NUMBER),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    )
}

@Composable
fun SideColour(
    cardSideService: CardSideService
) {
    val showDialogColourPicker = rememberSaveable { mutableStateOf(false) }

    val stateFlowCardSide =
        cardSideService.stateFlowCardSide.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val sideNumberColour = stateFlowCardSide.value.sideNumberColour

    val diceSidesFewerThanSdeNumber = stateFlowCardSide.value.diceSidesFewerThanSideNumber

    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = { showDialogColourPicker.value = true },
            modifier = Modifier
                .width(160.dp)
                .testTag(SideTestTag.SIDE_COLOUR),
            enabled = !diceSidesFewerThanSdeNumber
        ) {
            Icon(
                painterResource(id = R.drawable.palette),
                contentDescription = stringResource(R.string.colour),
                modifier = Modifier.size(24.dp),
            )

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(R.string.dialog_bag_side_colour))
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(modifier = Modifier.padding(end = 8.dp)) {
            BagCardColourSample(sideNumberColour)
        }
    }

    SideDescriptionAndColourApplyToAll(
        stateFlowCardSide.value.sideApplyToAllNumberColour,
        SideTestTag.SIDE_APPLY_NUMBER_COLOUR,
        cardSideService::sideApplyToAllNumberColour,
        R.string.dialog_bag_side_colour_apply_to_all,
        diceSidesFewerThanSdeNumber
    )

    if (showDialogColourPicker.value) {
        DialogColourPicker(
            showDialogColourPicker,
            stringResource(R.string.dialog_bag_colour_picker_side),
            sideNumberColour,
            cardSideService::sideNumberColour,
        )
    }
}
