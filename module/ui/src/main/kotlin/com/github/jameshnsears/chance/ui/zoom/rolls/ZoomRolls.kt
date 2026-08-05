package com.github.jameshnsears.chance.ui.zoom.rolls

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.group.GroupHistory
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.ui.tab.rolls.RollsAndroidViewModel
import com.github.jameshnsears.chance.ui.tab.rolls.RollsEvent
import com.github.jameshnsears.chance.ui.zoom.ZoomSideDescription
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.max
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ZoomRoll(
    rollsAndroidViewModel: RollsAndroidViewModel,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
) {
    val stateFlowZoom by zoomRollsAndroidViewModel.stateFlowZoom.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    val rollHistory = stateFlowZoom.rollHistory
    val entriesList = stateFlowZoom.entriesList

    val seenRollEvents = remember { mutableSetOf<Long>() }
    var rollEventHappened by remember { mutableStateOf(false) }

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = stateFlowZoom.firstVisibleItemIndex,
        initialFirstVisibleItemScrollOffset = stateFlowZoom.firstVisibleItemScrollOffset
    )

    val groupHistory by zoomRollsAndroidViewModel.groupHistory.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    LaunchedEffect(Unit) {
        RollsEvent.sharedFlowTabRollEvent.collect {
            rollEventHappened = true
            listState.scrollToItem(0)
        }
    }

    LaunchedEffect(entriesList.size) {
        if (rollEventHappened) {
            listState.scrollToItem(0)
        }
    }

    if (!rollEventHappened) {
        seenRollEvents.addAll(rollHistory.keys)
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            Pair(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset)
        }.collect { (index, offset) ->
            zoomRollsAndroidViewModel.saveScrollPosition(index, offset)
        }
    }


    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        if (rollHistory.isEmpty()) {
            ZoomRollEmptyState()
        } else {
            ZoomRollHistoryList(
                listState,
                entriesList,
                seenRollEvents,
                rollHistory.size,
                groupHistory,
                rollsAndroidViewModel,
                zoomRollsAndroidViewModel,
                stateFlowZoom.resizeViewDp
            )
        }
    }
}

@Composable
private fun ZoomRollEmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag(ZoomRollsTestTag.LAZY_COLUMN_EMPTY),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.tab_roll_empty),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun ZoomRollHistoryList(
    listState: androidx.compose.foundation.lazy.LazyListState,
    entriesList: List<Pair<Int, Map.Entry<Long, List<Roll>>>>,
    seenRollEvents: MutableSet<Long>,
    rollHistorySize: Int,
    groupHistory: GroupHistory,
    rollsAndroidViewModel: RollsAndroidViewModel,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
    resizeViewDp: Dp
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag(ZoomRollsTestTag.LAZY_COLUMN),
        state = listState,
        contentPadding = PaddingValues(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 128.dp)
    ) {
        itemsIndexed(
            items = entriesList,
            key = { _, (_, entry) -> entry.key }
        ) { indexSequence, (originalIndex, rollSequence) ->
            ZoomRollHistoryItem(
                indexSequence,
                originalIndex,
                rollSequence,
                seenRollEvents,
                rollHistorySize,
                groupHistory,
                rollsAndroidViewModel,
                zoomRollsAndroidViewModel,
                resizeViewDp,
                entriesList.size
            )
        }
    }
}

@Composable
private fun ZoomRollHistoryItem(
    indexSequence: Int,
    originalIndex: Int,
    rollSequence: Map.Entry<Long, List<Roll>>,
    seenRollEvents: MutableSet<Long>,
    rollHistorySize: Int,
    groupHistory: GroupHistory,
    rollsAndroidViewModel: RollsAndroidViewModel,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
    resizeViewDp: Dp,
    entriesListSize: Int
) {
    val isNewRollEvent = remember(rollSequence.key) {
        indexSequence == 0 && !seenRollEvents.contains(rollSequence.key)
    }

    val stateFlowZoom by zoomRollsAndroidViewModel.stateFlowZoom.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    val scrollPosition = stateFlowZoom.horizontalScrollPositions[rollSequence.key.toString()] ?: (0 to 0)

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = scrollPosition.first,
        initialFirstVisibleItemScrollOffset = scrollPosition.second
    )

    LaunchedEffect(listState) {
        snapshotFlow {
            Pair(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset)
        }.collect { (index, offset) ->
            zoomRollsAndroidViewModel.saveHorizontalScrollPosition(rollSequence.key.toString(), index, offset)
        }
    }

    LaunchedEffect(rollSequence.key) {
        if (indexSequence == 0) {
            seenRollEvents.add(rollSequence.key)
        }
    }

    Column {
        if (stateFlowZoom.rollIndexTime)
            RollIndexTime(
                rollHistorySize - originalIndex,
                rollSequence
            )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.testTag(ZoomRollsTestTag.LAZY_COLUMN_NOT_EMPTY)
        ) {
            RollScore(
                rollsAndroidViewModel,
                zoomRollsAndroidViewModel,
                rollSequence,
                resizeViewDp,
                stateFlowZoom.rollScore,
                isNewRollEvent
            )

            LazyRow(
                state = listState,
                contentPadding = PaddingValues(horizontal = 0.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .weight(1f),
            ) {
                itemsIndexed(
                    rollSequence.value,
                    key = { index, item ->
                        "${item.side}_$index"
                    }
                ) { _, roll ->
                    RollDetails(
                        zoomRollsAndroidViewModel,
                        roll,
                        zoomRollsAndroidViewModel.fetchDiceFromUuidCache(roll.uuidDice),
                        groupHistory,
                        resizeViewDp,
                    )
                }
            }
        }

        if (indexSequence < entriesListSize - 1)
            HorizontalDivider(Modifier.padding(top = 12.dp, bottom = 12.dp))
    }
}

@Composable
private fun RollIndexTime(
    position: Int,
    rollHistory: Map.Entry<Long, List<Roll>>
) {
    Row(
        modifier = Modifier.padding(top = 12.dp, bottom = 8.dp),
    ) {
        Text(
            text = "$position : ${
                SimpleDateFormat("EEEE, dd MMMM HH:mm:ss", LocalLocale.current.platformLocale)
                    .format(Date(rollHistory.key))
            }",
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun RollScore(
    rollsAndroidViewModel: RollsAndroidViewModel,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
    rollSequence: Map.Entry<Long, List<Roll>>,
    resizeViewDp: Dp,
    showScore: Boolean,
    isNewRollEvent: Boolean = false
) {
    RollScoreContent(
        rollsAndroidViewModel,
        zoomRollsAndroidViewModel,
        rollSequence,
        resizeViewDp,
        showScore,
        isNewRollEvent
    )
}

@Composable
private fun RollScoreContent(
    rollsAndroidViewModel: RollsAndroidViewModel,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
    rollSequence: Map.Entry<Long, List<Roll>>,
    resizeViewDp: Dp,
    showScore: Boolean,
    isNewRollEvent: Boolean
) {
    var displayedScore by remember(rollSequence.key, isNewRollEvent) {
        mutableIntStateOf(if (isNewRollEvent) 0 else rollSequence.value.sumOf { it.score })
    }

    if (isNewRollEvent) {
        LaunchedEffect(rollSequence.key) {
            val rolls = rollSequence.value
            var currentScore = 0
            rolls.forEachIndexed { index, roll ->
                currentScore += roll.score
                displayedScore = currentScore
                val delayTime = (75L - (index * 10L)).coerceAtLeast(10L)
                delay(delayTime.milliseconds)
            }

            rollsAndroidViewModel.playScoreTTS(currentScore)
        }
    }

    if (showScore) {
        Column(
            Modifier
                .padding(end = 12.dp, bottom = 8.dp)
                .widthIn(min = resizeViewDp * 0.8f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shadowElevation = 6.dp
            ) {
                Box(
                    modifier = Modifier
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            val size = max(placeable.width, placeable.height)
                            layout(size, size) {
                                placeable.placeRelative(
                                    (size - placeable.width) / 2,
                                    (size - placeable.height) / 2
                                )
                            }
                        }
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = displayedScore.toString(),
                        fontSize = zoomRollsAndroidViewModel.rollScoreFontSizeSp(resizeViewDp),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun RollDetails(
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
    roll: Roll,
    dice: Dice?,
    groupHistory: GroupHistory,
    resizeViewDp: Dp,
) {
    RollDetailsContent(
        zoomRollsAndroidViewModel,
        roll,
        dice,
        groupHistory,
        resizeViewDp
    )
}

@Composable
private fun RollDetailsContent(
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
    roll: Roll,
    dice: Dice?,
    groupHistory: GroupHistory,
    resizeViewDp: Dp,
) {
    val stateFlowZoom by zoomRollsAndroidViewModel.stateFlowZoom.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (stateFlowZoom.groupTitle) RollGroupTitle(groupHistory, roll)

        if (stateFlowZoom.diceTitle && dice != null) RollDiceTitle(dice, roll)

        if (stateFlowZoom.rollBehaviour) ZoomSideRollBehaviour(
            roll,
            resizeViewDp
        )

        RollImages(
            stateFlowZoom.sideNumber,
            stateFlowZoom.sideSVG,
            zoomRollsAndroidViewModel,
            dice,
            roll,
            resizeViewDp
        )

        if (stateFlowZoom.sideDescription && dice != null) {
            Box(Modifier.padding(bottom = 12.dp)) {
                ZoomSideDescription(
                    zoomRollsAndroidViewModel,
                    dice,
                    roll.side
                )
            }
        }
    }
}

@Composable
private fun RollGroupTitle(groupHistory: GroupHistory, roll: Roll) {
    Box(
        modifier = Modifier.width(110.dp),
        contentAlignment = Alignment.Center
    ) {
        val title = groupHistory.find { it.uuid == roll.uuidGroup }?.name ?: ""

        if (title.isNotEmpty()) {
            Row(Modifier.padding(top = 12.dp, bottom = 8.dp)) {
                Column {
                    Column {
                        if (UtilityFeature.isEnabled(UtilityFeature.Flag.UI_SHOW_UUID)) {
                            Row {
                                Text(
                                    title,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        lineBreak = LineBreak.Paragraph
                                    )
                                )
                            }

                            Row {
                                Text(
                                    roll.uuidGroup,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                        } else {
                            Text(
                                title,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    lineBreak = LineBreak.Paragraph
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RollDiceTitle(dice: Dice, roll: Roll) {
    Box(
        modifier = Modifier.width(110.dp),
        contentAlignment = Alignment.Center
    ) {
        if (dice.title.isNotEmpty()) {
            Row(Modifier.padding(top = 12.dp, bottom = 8.dp)) {
                Column {
                    if (UtilityFeature.isEnabled(UtilityFeature.Flag.UI_SHOW_UUID)) {
                        Row {
                            Text(
                                dice.title,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    lineBreak = LineBreak.Paragraph
                                )
                            )
                        }

                        Row {
                            Text(
                                dice.uuid,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Row {
                            Text(
                                roll.side.uuid,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                    } else {
                        Text(
                            dice.title,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleSmall.copy(
                                lineBreak = LineBreak.Paragraph
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RollImages(
    settingsSideNumber: Boolean,
    settingsSideSVG: Boolean,
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
    dice: Dice?,
    roll: Roll,
    resizeViewDp: Dp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (settingsSideNumber) ZoomRollFaceImageShape(
            zoomRollsAndroidViewModel,
            dice,
            roll.side,
            resizeViewDp
        )

        if (settingsSideSVG) ZoomRollFaceImageSVG(
            zoomRollsAndroidViewModel,
            roll.side,
            resizeViewDp
        )
    }
}

@Composable
fun ZoomSideRollBehaviour(
    roll: Roll,
    resizeView: Dp
) {
    val rollSelectionIconColour = MaterialTheme.colorScheme.onSurface

    Box {
        Row(
            modifier = Modifier.padding(top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ZoomSideRollBehaviourIcon(
                resizeView,
                rollSelectionIconColour,
                R.drawable.dice_roll_multiplier,
                "${roll.multiplierIndex}"
            )

            if (roll.explodeIndex != 0) {
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))

                ZoomSideRollBehaviourIcon(
                    resizeView,
                    rollSelectionIconColour,
                    R.drawable.dice_roll_explode,
                    "${roll.explodeIndex}"
                )
            }

            if (roll.scoreAdjustment != 0) {
                ZoomSideRollBehaviourIcon(
                    resizeView,
                    rollSelectionIconColour,
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
        text = text,
        color = rollSelectionIconColour,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun ZoomRollFaceImageShape(
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
    dice: Dice?,
    side: Side,
    resizeView: Dp
) {
    Box {
        Image(
            painter = painterResource(
                if (dice != null) zoomRollsAndroidViewModel.drawableForDiceSides(dice)
                else com.github.jameshnsears.chance.data.domain.R.drawable.d6
            ),
            contentDescription = side.description,
            modifier = Modifier
                .size(resizeView)
                .padding(top = 8.dp),
            colorFilter = zoomRollsAndroidViewModel.sideColorFilter(dice?.colour ?: ""),
            contentScale = ContentScale.Crop
        )

        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 0.dp),
            fontSize = zoomRollsAndroidViewModel.sideNumberFontSizeSp(),
            text = "${side.number}",
            color = zoomRollsAndroidViewModel.sideColor(side.numberColour),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ZoomRollFaceImageSVG(
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
    side: Side,
    resizeView: Dp
) {
    val modifier = Modifier
        .size(resizeView)
        .padding(top = 8.dp)

    if (side.imageBase64 != "") {
        val imageRequest by produceState<ImageRequest?>(initialValue = side.imageRequest, side.imageBase64) {
            if (value == null) {
                value = zoomRollsAndroidViewModel.sideSvgImageRequestAsync(side)
            }
        }

        if (imageRequest != null) {
            AsyncImage(
                model = imageRequest,
                contentDescription = side.description,
                modifier = modifier
            )
        }
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
