package com.github.jameshnsears.chance.ui.dialog.bag.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.R
import com.github.jameshnsears.chance.ui.dialog.bag.card.dice.composable.BagCardDice
import com.github.jameshnsears.chance.ui.dialog.bag.card.roll.composable.BagCardRoll
import com.github.jameshnsears.chance.ui.dialog.bag.card.side.composable.BagCardSide

@Composable
fun DialogBagTabLayout(
    showDialog: MutableState<Boolean>,
    dialogBagAndroidViewModel: DialogBagAndroidViewModel
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    val tabs = listOf(
        TabItem(
            stringResource(R.string.dialog_bag_dice),
            ImageVector.vectorResource(R.drawable.dice)
        ),

        TabItem(
            stringResource(R.string.dialog_bag_side_title),
            ImageVector.vectorResource(R.drawable.side)
        ),

        TabItem(
            stringResource(R.string.dialog_bag_roll),
            ImageVector.vectorResource(R.drawable.behaviour)
        )
    )

    Column {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    icon = {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.title
                        )
                    },
                    text = { Text(tab.title) }
                )
            }
        }

        DialogBagTabContent(
            modifier = Modifier
                .fillMaxSize(),
            dialogBagAndroidViewModel,
            selectedTabIndex = selectedTabIndex,
            showDialog,
        )
    }
}

@Composable
fun DialogBagTabContent(
    modifier: Modifier,
    dialogBagAndroidViewModel: DialogBagAndroidViewModel,
    selectedTabIndex: Int,
    showDialog: MutableState<Boolean>
) {
    val stateFlowCardDice =
        dialogBagAndroidViewModel.cardDiceViewModel.stateFlowCardDice.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    var diceCanBeDeleted = stateFlowCardDice.value.diceCanBeDeleted

    var diceCanBeCloned = stateFlowCardDice.value.diceCanBeCloned

    val diceCanBeSaved = stateFlowCardDice.value.diceCanBeSaved

    if (selectedTabIndex != 0) {
        diceCanBeDeleted = false
        diceCanBeCloned = false
    }

    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .focusTarget()
                .padding(end = 4.dp),
        ) {
            IconButton(onClick = {
                showDialog.value = false
            }) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "",
                )
            }

            Spacer(Modifier.weight(1f))

            if (selectedTabIndex == 0) {
                TextButtonDelete(diceCanBeDeleted, dialogBagAndroidViewModel, showDialog)

                TextButtonClone(diceCanBeCloned, dialogBagAndroidViewModel, showDialog)
            }

            TextButtonSave(diceCanBeSaved, dialogBagAndroidViewModel, showDialog)
        }

        Row(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
        ) {
            when (selectedTabIndex) {
                0 -> DiceContent(modifier, dialogBagAndroidViewModel)
                1 -> SideContent(modifier, dialogBagAndroidViewModel)
                2 -> BehaviourContent(modifier, dialogBagAndroidViewModel)
            }
        }
    }
}

@Composable
fun DiceContent(
    modifier: Modifier,
    dialogBagAndroidViewModel: DialogBagAndroidViewModel
) {
    BagCardDice(dialogBagAndroidViewModel.cardDiceViewModel)
}

@Composable
fun SideContent(
    modifier: Modifier,
    dialogBagAndroidViewModel: DialogBagAndroidViewModel
) {
    BagCardSide(dialogBagAndroidViewModel.cardSideAndroidViewModel)
}

@Composable
fun BehaviourContent(
    modifier: Modifier,
    dialogBagAndroidViewModel: DialogBagAndroidViewModel
) {
    BagCardRoll(dialogBagAndroidViewModel.cardRollViewModel)
}

data class TabItem(val title: String, val icon: ImageVector)
