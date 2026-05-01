package com.github.jameshnsears.chance.ui.zoom.roll

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.ui.tab.roll.RollAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.roll.SettingsState
import com.github.jameshnsears.chance.ui.zoom.ZoomSideDescription
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun ZoomRoll(
    rollAndroidViewModel: RollAndroidViewModel,
    zoomRollAndroidViewModel: ZoomRollAndroidViewModel,
) {
    val stateFlowZoom =
        zoomRollAndroidViewModel.stateFlowZoom.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val rollHistory = stateFlowZoom.value.rollHistory

    val stateFlowTabRoll =
        rollAndroidViewModel.stateFlowSettings.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = stateFlowZoom.value.firstVisibleItemIndex,
        initialFirstVisibleItemScrollOffset = stateFlowZoom.value.firstVisibleItemScrollOffset
    )

    androidx.compose.runtime.LaunchedEffect(listState) {
        androidx.compose.runtime.snapshotFlow {
            Pair(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset)
        }.collect { (index, offset) ->
            zoomRollAndroidViewModel.saveScrollPosition(index, offset)
        }
    }

    // Use entries directly without materializing to list for better performance
    val entriesList = remember(stateFlowZoom.value.rollHistory) {
        stateFlowZoom.value.rollHistory.entries.toList()
    }

    LazyColumn(
        modifier = Modifier
            .padding(top = 8.dp, start = 0.dp, bottom = 125.dp, end = 8.dp)
            .testTag(ZoomRollTestTag.LAZY_COLUMN),
        state = listState,
    ) {
        if (entriesList.isEmpty()) {
            item {
                Box(modifier = Modifier.testTag(ZoomRollTestTag.LAZY_COLUMN_EMPTY)) {
                }
            }
        } else {
            itemsIndexed(
                items = entriesList,
                key = { index, item -> "${item.key}_${index}" }
            ) { index, rollSequence ->
                if (stateFlowTabRoll.value.rollIndexTime)
                    RollIndexTime(
                        rollHistory.size - index,
                        rollSequence
                    )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.testTag(ZoomRollTestTag.LAZY_COLUMN_NOT_EMPTY)
                ) {
                    if (stateFlowTabRoll.value.rollScore)
                        RollScore(rollAndroidViewModel, rollSequence)

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.padding(bottom = 12.dp),
                    ) {
                        itemsIndexed(
                            rollSequence.value,
                            key = { index, item ->
                                "${item.side}_${index}"
                            }
                        ) { _, roll ->
                            RollDetails(
                                zoomRollAndroidViewModel,
                                roll,
                                zoomRollAndroidViewModel.fetchDiceFromEpochCache(roll.diceEpoch)!!,
                                stateFlowTabRoll.value,
                                stateFlowZoom.value.resizeViewDp
                            )
                        }
                    }
                }

                if (index < rollHistory.entries.size - 1
                    &&
                    rollAndroidViewModel.isContentAvailableToDisplay(rollSequence.value)
                )
                    HorizontalDivider(Modifier.padding(bottom = 12.dp))
            }
        }
    }
}

@Composable
private fun RollIndexTime(
    position: Int,
    rollHistory: MutableMap.MutableEntry<Long, List<Roll>>
) {
    Row(
        modifier = Modifier.padding(start = 8.dp),
    ) {
        Text(
            text = "$position : ${
                SimpleDateFormat("dd, MMMM HH:mm:ss", LocalLocale.current.platformLocale)
                    .format(Date(rollHistory.key))
            }"
        )
    }
}

@Composable
private fun RollScore(
    rollAndroidViewModel: RollAndroidViewModel,
    rollSequence: MutableMap.MutableEntry<Long, List<Roll>>
) {
    Column(
        Modifier
            .padding(start = 8.dp, end = 12.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = rollAndroidViewModel.rollSequenceHelper.rollSequenceScore(rollSequence),
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun RollDetails(
    zoomRollAndroidViewModel: ZoomRollAndroidViewModel,
    roll: Roll,
    dice: Dice,
    settingsState: SettingsState,
    resizeViewDp: Dp
) {
    val settingsDiceTitle = settingsState.diceTitle

    val settingsSideNumber = settingsState.sideNumber

    val settingsBehaviour = settingsState.rollBehaviour

    val settingsSideDescription = settingsState.sideDescription

    val settingsSideSVG = settingsState.sideSVG

    Column(
        Modifier.padding(start = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (settingsDiceTitle)
            Row(Modifier.padding(4.dp)) {
                if (UtilityFeature.isEnabled(UtilityFeature.Flag.UI_SHOW_EPOCH_UUID)) {
                    Column {
                        Text(dice.title)
                        Text("${dice.epoch}")
                        Text(dice.uuid)
                        Text(roll.side.uuid)
                    }
                } else {
                    Text(dice.title)
                }
            }

        if (settingsBehaviour) ZoomSideRollBehaviour(zoomRollAndroidViewModel, roll, resizeViewDp)

        if (settingsSideNumber) ZoomRollSideImageShape(
            zoomRollAndroidViewModel,
            dice,
            roll.side,
            resizeViewDp
        )

        if (settingsSideSVG) ZoomRollSideImageSVG(
            zoomRollAndroidViewModel,
            roll.side,
            resizeViewDp
        )

        if (settingsSideDescription) ZoomSideDescription(zoomRollAndroidViewModel, dice, roll.side)
    }
}

@Composable
fun ZoomSideRollBehaviour(
    zoomRollAndroidViewModel: ZoomRollAndroidViewModel,
    roll: Roll,
    resizeView: Dp
) {
    val rollSelectionIconColour = if (isSystemInDarkTheme()) Color.White else Color.Black

    Box {
        Row(
            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ZoomSideRollBehaviourIcon(
                resizeView,
                rollSelectionIconColour,
                zoomRollAndroidViewModel,
                R.drawable.dice_roll_multiplier,
                "${roll.multiplierIndex}"
            )

            if (roll.explodeIndex != 0) {
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))

                ZoomSideRollBehaviourIcon(
                    resizeView,
                    rollSelectionIconColour,
                    zoomRollAndroidViewModel,
                    R.drawable.dice_roll_explode,
                    "${roll.explodeIndex}"
                )
            }

            if (roll.scoreAdjustment != 0) {
                ZoomSideRollBehaviourIcon(
                    resizeView,
                    rollSelectionIconColour,
                    zoomRollAndroidViewModel,
                    R.drawable.dice_roll_add_subtract,
                    "${roll.scoreAdjustment}"
                )
            }
        }
    }
}

@Composable
private fun ZoomSideRollBehaviourIcon(
    resizeView: Dp,
    rollSelectionIconColour: Color,
    zoomRollAndroidViewModel: ZoomRollAndroidViewModel,
    drawableId: Int,
    text: String,
) {
    Icon(
        painterResource(id = drawableId),
        contentDescription = text,
        modifier = Modifier.size(resizeView / 4),
        tint = rollSelectionIconColour
    )

    Text(
        fontSize = zoomRollAndroidViewModel.sideNumberFontSizeSp(),
        text = text,
        color = rollSelectionIconColour,
    )
}

@Composable
fun ZoomRollSideImageShape(
    zoomRollAndroidViewModel: ZoomRollAndroidViewModel,
    dice: Dice,
    side: Side,
    resizeView: Dp
) {
    Box {
        Image(
            painter = painterResource(zoomRollAndroidViewModel.drawableForDiceSides(dice)),
            contentDescription = side.description,
            modifier = Modifier
                .size(resizeView)
                .padding(top = 8.dp),
            colorFilter = zoomRollAndroidViewModel.sideColorFilter(dice.colour),
            contentScale = ContentScale.Crop
        )

        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 0.dp),
            fontSize = zoomRollAndroidViewModel.sideNumberFontSizeSp(),
            text = "${side.number}",
            color = zoomRollAndroidViewModel.sideColor(side.numberColour),
        )
    }
}

@Composable
fun ZoomRollSideImageSVG(
    zoomRollAndroidViewModel: ZoomRollAndroidViewModel,
    side: Side,
    resizeView: Dp
) {
    val modifier = Modifier
        .size(resizeView)
        .padding(top = 8.dp)

    if (side.imageBase64 != "") {
        val imageRequest: ImageRequest = remember {
            zoomRollAndroidViewModel.sideSvgImageRequest(side)
        }

        AsyncImage(
            model = imageRequest,
            contentDescription = side.description,
            modifier = modifier
        )
    } else {
        if (side.imageDrawableId != 0) {
            Image(
                painter = painterResource(id = side.imageDrawableId),
                contentDescription = side.description,
                modifier = modifier
            )
        }
    }
}
