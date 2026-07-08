package com.github.jameshnsears.chance.ui.dialog.dice

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.ui.dialog.dice.card.dice.BagCardDice
import com.github.jameshnsears.chance.ui.dialog.dice.card.dice.CardDiceService
import com.github.jameshnsears.chance.ui.dialog.dice.card.face.BagCardSide
import com.github.jameshnsears.chance.ui.dialog.dice.card.face.CardSideService
import com.github.jameshnsears.chance.ui.dialog.dice.card.roll.BagCardRoll
import com.github.jameshnsears.chance.ui.dialog.dice.card.roll.CardRollService
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun DialogBagTabLayout(
    showDialog: MutableState<Boolean>,
    dialogDiceAndroidViewModel: DialogDiceAndroidViewModel
) {
    val tabs = listOf(
        TabItem(
            stringResource(R.string.dialog_bag_dice),
            ImageVector.vectorResource(R.drawable.dice),
            DialogDiceTabTestTag.TAB_DICE
        ),

        TabItem(
            stringResource(R.string.dialog_bag_side_title),
            Icons.Outlined.FormatListNumbered,
            DialogDiceTabTestTag.TAB_SIDE
        ),

        TabItem(
            stringResource(R.string.dialog_bag_roll),
            ImageVector.vectorResource(R.drawable.rule),
            DialogDiceTabTestTag.TAB_ROLL
        )
    )

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.safeDrawingPadding()
    ) {
        PrimaryTabRow(selectedTabIndex = pagerState.currentPage) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    modifier = Modifier.testTag(tab.testTag),
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    icon = {
                        Icon(
                            modifier = Modifier.size(24.dp),
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
            dialogDiceAndroidViewModel,
            dialogDiceAndroidViewModel.cardDiceService,
            dialogDiceAndroidViewModel.cardSideService,
            dialogDiceAndroidViewModel.cardRollService,
            pagerState = pagerState,
            showDialog = showDialog,
        )
    }
}

@Composable
fun DialogBagTabContent(
    modifier: Modifier,
    dialogDiceAndroidViewModel: DialogDiceAndroidViewModel,
    cardDiceService: CardDiceService,
    cardSideService: CardSideService,
    cardRollService: CardRollService,
    pagerState: PagerState,
    showDialog: MutableState<Boolean>,
) {
    LaunchedEffect(Unit) {
        dialogDiceAndroidViewModel.refresh()
    }

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

    val selectedTabIndex = pagerState.currentPage

    var diceCanBeDeleted = stateFlowCardDice.value.diceCanBeDeleted

    var diceCanBeCloned = stateFlowCardDice.value.diceCanBeCloned

    val diceCanBeSaved = stateFlowCardDice.value.diceCanBeSaved

    if (selectedTabIndex != 0) {
        diceCanBeDeleted = false
        diceCanBeCloned = false
    }

    Column(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .focusTarget()
                .padding(top = 8.dp, bottom = 8.dp, end = 8.dp),
        ) {
            IconButton(
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .testTag(ButtonFeatureTestTag.BUTTON_FEATURE_CANCEL),
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
                    dialogDiceAndroidViewModel,
                    showDialog,
                )

                TextButtonClone(
                    diceCanBeCloned,
                    dialogDiceAndroidViewModel,
                    stateFlowCardDice.value,
                    stateFlowCardRoll.value,
                    showDialog,
                )
            }

            TextButtonSave(
                diceCanBeSaved,
                dialogDiceAndroidViewModel,
                stateFlowCardDice.value,
                stateFlowCardRoll.value,
                stateFlowCardSide.value,
                showDialog,
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = modifier
                .padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
            pageSpacing = 16.dp
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        val pageOffset = (
                            (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                            ).absoluteValue

                        // scale and alpha effect to create "padding" look during swipe
                        val scale = 1f - (0.1f * pageOffset.coerceIn(0f, 1f))
                        scaleX = scale
                        scaleY = scale
                        alpha = 1f - (0.5f * pageOffset.coerceIn(0f, 1f))
                    }
            ) {
                when (page) {
                    0 -> DiceContent(Modifier.fillMaxSize(), cardDiceService)
                    1 -> SideContent(Modifier.fillMaxSize(), cardSideService)
                    2 -> BehaviourContent(Modifier.fillMaxSize(), cardRollService)
                }
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
        modifier = modifier
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
        modifier = modifier
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
        modifier = modifier
            .padding(insets)
    ) {
        BagCardRoll(cardRollService)
    }
}

data class TabItem(val title: String, val icon: ImageVector, val testTag: String)
