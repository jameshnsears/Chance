package com.github.jameshnsears.chance.ui.dialog.bag

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.ui.dialog.bag.card.dice.BagCardDice
import com.github.jameshnsears.chance.ui.dialog.bag.card.dice.CardDiceService
import com.github.jameshnsears.chance.ui.dialog.bag.card.roll.BagCardRoll
import com.github.jameshnsears.chance.ui.dialog.bag.card.roll.CardRollService
import com.github.jameshnsears.chance.ui.dialog.bag.card.side.BagCardSide
import com.github.jameshnsears.chance.ui.dialog.bag.card.side.CardSideService

@Composable
fun DialogBagTabLayout(
    showDialog: MutableState<Boolean>,
    dialogBagAndroidViewModel: DialogBagAndroidViewModel
) {
    val selectedTabIndex = rememberSaveable { mutableIntStateOf(1) }
    val clickCounter = remember { mutableIntStateOf(0) }

    val tabs = listOf(
        TabItem(
            stringResource(R.string.dialog_bag_dice),
            ImageVector.vectorResource(R.drawable.dice),
            TabTestTag.TAB_DICE
        ),

        TabItem(
            stringResource(R.string.dialog_bag_side_title),
            ImageVector.vectorResource(R.drawable.dice_side),
            TabTestTag.TAB_SIDE
        ),

        TabItem(
            stringResource(R.string.dialog_bag_roll),
            ImageVector.vectorResource(R.drawable.dice_roll),
            TabTestTag.TAB_ROLL
        )
    )

    Column(
        modifier = Modifier.safeDrawingPadding()
    ) {
        PrimaryTabRow(selectedTabIndex = selectedTabIndex.intValue) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    modifier = Modifier.testTag(tab.testTag),
                    selected = selectedTabIndex.intValue == index,
                    onClick = {
                        selectedTabIndex.intValue = index
                        clickCounter.intValue++
                    },
                    icon = {
                        TabIcon(tab, index, selectedTabIndex.intValue, clickCounter.intValue)
                    },
                    text = { Text(tab.title) }
                )
            }
        }

        DialogBagTabContent(
            modifier = Modifier
                .fillMaxSize(),
            dialogBagAndroidViewModel,
            dialogBagAndroidViewModel.cardDiceService,
            dialogBagAndroidViewModel.cardSideService,
            dialogBagAndroidViewModel.cardRollService,
            selectedTabIndex = selectedTabIndex.intValue,
            showDialog,
        )
    }
}

@Composable
fun TabIcon(tab: TabItem, index: Int, selectedTabIndex: Int, clickCounter: Int) {
    val initialValue = when (tab.testTag) {
        TabTestTag.TAB_SIDE -> 1.4f
        else -> 0f
    }
    val animation = remember { Animatable(initialValue) }
    LaunchedEffect(clickCounter) {
        if (selectedTabIndex == index) {
            if (clickCounter > 0) {
                when (tab.testTag) {
                    TabTestTag.TAB_DICE -> {
                        animation.snapTo(0f)
                        animation.animateTo(360f, tween(1000, easing = LinearEasing))
                    }

                    TabTestTag.TAB_SIDE -> {
                        animation.animateTo(0.2f, tween(500, easing = LinearEasing))
                        animation.animateTo(2.2f, tween(500, easing = LinearEasing))
                        animation.animateTo(1.4f, tween(500, easing = LinearEasing))
                    }

                    TabTestTag.TAB_ROLL -> {
                        animation.snapTo(0f)
                        animation.animateTo(2 * PI.toFloat(), tween(1000, easing = LinearEasing))
                    }
                }
            }
        } else {
            animation.snapTo(initialValue)
        }
    }

    Icon(
        modifier = Modifier.graphicsLayer {
            when (tab.testTag) {
                TabTestTag.TAB_DICE -> {
                    rotationY = animation.value
                    scaleX = 1.4f
                    scaleY = 1.4f
                }
                TabTestTag.TAB_SIDE -> {
                    scaleX = animation.value
                    scaleY = animation.value
                }

                TabTestTag.TAB_ROLL -> {
                    val angle = animation.value
                    translationX = (sin(angle) * 40).dp.toPx()
                    val scale = 1f + (cos(angle) * 0.4f)
                    scaleX = scale
                    scaleY = scale
                }
            }
        },
        imageVector = tab.icon,
        contentDescription = tab.title
    )
}

@Composable
fun DialogBagTabContent(
    modifier: Modifier,
    dialogBagAndroidViewModel: DialogBagAndroidViewModel,
    cardDiceService: CardDiceService,
    cardSideService: CardSideService,
    cardRollService: CardRollService,
    selectedTabIndex: Int,
    showDialog: MutableState<Boolean>
) {
    val stateFlowCardDice =
        cardDiceService.stateFlowCardDice.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val stateFlowCardSide =
        cardSideService.stateFlowCardSide.collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val stateFlowCardRoll =
        cardRollService.stateFlowCardRoll.collectAsStateWithLifecycle(
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
            .padding(2.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .focusTarget()
                .padding(end = 4.dp),
        ) {
            IconButton(
                modifier = Modifier.testTag(ButtonFeatureTestTag.BUTTON_FEATURE_CANCEL),
                onClick = {
                    showDialog.value = false
                }) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = stringResource(R.string.close),
                )
            }

            Spacer(Modifier.weight(1f))

            if (selectedTabIndex == 0) {
                TextButtonDelete(
                    diceCanBeDeleted,
                    dialogBagAndroidViewModel,
                    showDialog
                )

                TextButtonClone(
                    diceCanBeCloned,
                    dialogBagAndroidViewModel,
                    stateFlowCardDice.value,
                    stateFlowCardRoll.value,
                    showDialog
                )
            }

            TextButtonSave(
                diceCanBeSaved,
                dialogBagAndroidViewModel,
                stateFlowCardDice.value,
                stateFlowCardRoll.value,
                stateFlowCardSide.value,
                showDialog
            )
        }

        Row(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
        ) {
            when (selectedTabIndex) {
                0 -> DiceContent(modifier, cardDiceService)
                1 -> SideContent(modifier, cardSideService)
                2 -> BehaviourContent(modifier, cardRollService)
            }
        }
    }
}

@Composable
fun DiceContent(
    modifier: Modifier,
    cardDiceService: CardDiceService
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val insets = if (isLandscape) PaddingValues(0.dp) else WindowInsets.navigationBars.asPaddingValues()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(insets)
    ) {
        BagCardDice(cardDiceService)
    }
}

@Composable
fun SideContent(
    modifier: Modifier,
    cardSideService: CardSideService
) {
    val insets = WindowInsets.navigationBars.asPaddingValues()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(insets)
    ) {
        BagCardSide(cardSideService)
    }
}

@Composable
fun BehaviourContent(
    modifier: Modifier,
    cardRollService: CardRollService
) {
    val insets = WindowInsets.navigationBars.asPaddingValues()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(insets)
    ) {
        BagCardRoll(cardRollService)
    }
}

data class TabItem(val title: String, val icon: ImageVector, val testTag: String)
