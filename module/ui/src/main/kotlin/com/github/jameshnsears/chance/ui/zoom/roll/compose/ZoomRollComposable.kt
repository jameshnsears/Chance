package com.github.jameshnsears.chance.ui.zoom.roll.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.ui.dialog.bag.compose.DialogBag
import com.github.jameshnsears.chance.ui.tab.roll.TabRollAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.compose.DiceTitle
import com.github.jameshnsears.chance.ui.zoom.compose.SideDescriptionRoll
import com.github.jameshnsears.chance.ui.zoom.compose.SideImageSVG
import com.github.jameshnsears.chance.ui.zoom.compose.SideImageShape
import com.github.jameshnsears.chance.ui.zoom.compose.SideRollBehaviour
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ZoomRoll(
    tabRollAndroidViewModel: TabRollAndroidViewModel,
    zoomAndroidViewModel: ZoomAndroidViewModel,
) {
    val stateFlowZoom =
        zoomAndroidViewModel.stateFlowZoom.collectAsStateWithLifecycle(
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )
    val diceBag = stateFlowZoom.value.diceBag
    val rollHistory = stateFlowZoom.value.rollHistory

    val stateFlowTabRoll =
        tabRollAndroidViewModel.stateFlowSettings.collectAsStateWithLifecycle(
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )
    val settingsTime = stateFlowTabRoll.value.rollIndexTime
    val settingsScore = stateFlowTabRoll.value.rollScore

    val listState = rememberLazyListState()
    // runs at every recomposition
    LaunchedEffect(stateFlowZoom.value.rollHistory.entries.toList()) {
        listState.scrollToItem(0)
    }

    LazyColumn(
        modifier = Modifier.padding(top = 8.dp, start = 0.dp, bottom = 110.dp, end = 8.dp),
        state = listState
    ) {
        itemsIndexed(
            items = stateFlowZoom.value.rollHistory.entries.toList(),
            key = { _, item -> item.key }
        ) { index, rollSequence ->
            if (settingsTime)
                RollIndexTime(rollHistory.size - index, rollSequence)

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (settingsScore)
                    RollScore(tabRollAndroidViewModel, rollSequence)

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.padding(bottom = 12.dp),
                ) {
                    items(
                        rollSequence.value,
                    ) { roll ->
                        for (dice: Dice in diceBag) {
                            if (dice.epoch == roll.diceEpoch) {
                                RollDetails(
                                    tabRollAndroidViewModel,
                                    zoomAndroidViewModel,
                                    roll,
                                    dice
                                )
                                break
                            }
                        }
                    }
                }
            }

            if (index < rollHistory.entries.size
                &&
                tabRollAndroidViewModel.isContentAvailableToDisplay(rollSequence.value)
            )
                HorizontalDivider(Modifier.padding(bottom = 12.dp))
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
                SimpleDateFormat("dd, MMMM HH:mm:ss", Locale.getDefault())
                    .format(Date(rollHistory.key))
            }",
            modifier = Modifier.padding(bottom = 8.dp),
        )
    }
}

@Composable
private fun RollScore(
    tabRollAndroidViewModel: TabRollAndroidViewModel,
    rollSequence: MutableMap.MutableEntry<Long, List<Roll>>
) {
    Column(
        Modifier
            .padding(start = 8.dp, end = 4.dp, bottom = 8.dp)
            .width(70.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = tabRollAndroidViewModel.diceSequenceScore(rollSequence),
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun RollDetails(
    tabRollAndroidViewModel: TabRollAndroidViewModel,
    zoomAndroidViewModel: ZoomAndroidViewModel,
    roll: Roll,
    dice: Dice
) {
    val stateFlowTabRoll =
        tabRollAndroidViewModel.stateFlowSettings.collectAsStateWithLifecycle(
            lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
        )

    val settingsDiceTitle = stateFlowTabRoll.value.diceTitle

    val settingsSideNumber = stateFlowTabRoll.value.sideNumber

    val settingsBehaviour = stateFlowTabRoll.value.behaviour

    val settingsSideDescription = stateFlowTabRoll.value.sideDescription

    val settingsSideSVG = stateFlowTabRoll.value.sideSVG

    val showDialog = remember { mutableStateOf(false) }
    val dialogDice = remember { mutableStateOf(Dice()) }
    val dialogSide = remember { mutableStateOf(Side()) }

    Column(
        Modifier.padding(start = 9.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (settingsDiceTitle) DiceTitle(dice)

        if (settingsSideNumber) SideImageShape(
            zoomAndroidViewModel,
            dice,
            roll.side,
            showDialog,
            dialogDice,
            dialogSide
        )

        if (settingsBehaviour) SideRollBehaviour(zoomAndroidViewModel, roll)

        if (settingsSideDescription) SideDescriptionRoll(zoomAndroidViewModel, roll.side)

        if (settingsSideSVG) SideImageSVG(
            zoomAndroidViewModel,
            dice,
            roll.side,
            showDialog,
            dialogDice,
            dialogSide
        )
    }


    if (showDialog.value) {
        DialogBag(
            showDialog,
            zoomAndroidViewModel.repositoryBag,
            dialogDice.value,
            dialogSide.value,
        )
    }
}
