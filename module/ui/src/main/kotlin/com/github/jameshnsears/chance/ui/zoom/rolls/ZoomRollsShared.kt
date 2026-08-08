package com.github.jameshnsears.chance.ui.zoom.rolls

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.github.jameshnsears.chance.common.R
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.group.GroupHistory
import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.jameshnsears.chance.ui.tab.rolls.RollsAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.ZoomSideDescription
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.max
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ZoomRollEmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag(ZoomRollsTestTag.LAZY_COLUMN_EMPTY),
        contentAlignment = Alignment.Center,
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
fun RollIndexTime(
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
fun RollScore(
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
    val currentScore = rollSequence.value.sumOf { it.score }
    var displayedScore by remember(rollSequence.key, isNewRollEvent, currentScore) {
        mutableIntStateOf(if (isNewRollEvent) 0 else currentScore)
    }

    if (isNewRollEvent) {
        LaunchedEffect(rollSequence.key, isNewRollEvent, currentScore) {
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
fun RollDetails(
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
    roll: Roll,
    dice: Dice?,
    groupHistory: GroupHistory,
    resizeViewDp: Dp,
    isLocked: Boolean = false,
    onToggleLock: (() -> Unit)? = null,
    rollEventCount: Int = 0
) {
    RollDetailsContent(
        zoomRollsAndroidViewModel,
        roll,
        dice,
        groupHistory,
        resizeViewDp,
        isLocked,
        onToggleLock,
        rollEventCount
    )
}

@Composable
private fun RollDetailsContent(
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
    roll: Roll,
    dice: Dice?,
    groupHistory: GroupHistory,
    resizeViewDp: Dp,
    isLocked: Boolean,
    onToggleLock: (() -> Unit)?,
    rollEventCount: Int
) {
    val stateFlowZoom by zoomRollsAndroidViewModel.stateFlowZoom.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (stateFlowZoom.groupTitle) RollGroupTitle(groupHistory, roll)

        if ((stateFlowZoom.diceTitle) && dice != null) RollDiceTitle(dice, roll)

        if (stateFlowZoom.rollBehaviour) ZoomSideRollBehaviour(
            roll,
            resizeViewDp,
        )

        RollImages(
            stateFlowZoom.sideNumber,
            stateFlowZoom.sideSVG,
            zoomRollsAndroidViewModel,
            dice,
            roll,
            resizeViewDp,
            isLocked,
            onToggleLock,
            rollEventCount
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
    resizeViewDp: Dp,
    isLocked: Boolean,
    onToggleLock: (() -> Unit)?,
    rollEventCount: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (settingsSideNumber) ZoomRollFaceImageShape(
            zoomRollsAndroidViewModel,
            dice,
            roll.side,
            resizeViewDp,
            isLocked,
            onToggleLock,
            rollEventCount
        )

        if (settingsSideSVG) ZoomRollFaceImageSVG(
            zoomRollsAndroidViewModel,
            roll.side,
            resizeViewDp,
            isLocked,
            onToggleLock,
            rollEventCount
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
                roll.multiplierIndex.toString(),
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
    resizeView: Dp,
    isLocked: Boolean = false,
    onToggleLock: (() -> Unit)? = null,
    rollEventCount: Int = 0
) {
    key(if (isLocked) 0 else rollEventCount) {
        Box(
            modifier = if (onToggleLock != null) Modifier.clickable { onToggleLock() } else Modifier
        ) {
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

            if (isLocked) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Lock",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(resizeView / 3)
                        .padding(4.dp)
                        .testTag(ZoomRollsTestTag.ROLL_LOCK),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun ZoomRollFaceImageSVG(
    zoomRollsAndroidViewModel: ZoomRollsAndroidViewModel,
    side: Side,
    resizeView: Dp,
    isLocked: Boolean = false,
    onToggleLock: (() -> Unit)? = null,
    rollEventCount: Int = 0
) {
    val modifier = Modifier
        .size(resizeView)
        .padding(top = 8.dp)

    Box(
        modifier = if (onToggleLock != null) Modifier.clickable { onToggleLock() } else Modifier
    ) {
        if (side.imageBase64 != "") {
            val imageRequest by produceState(initialValue = side.imageRequest, side, if (isLocked) 0 else rollEventCount) {
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

        if (isLocked) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Lock",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(resizeView / 3)
                    .padding(4.dp)
                    .testTag(ZoomRollsTestTag.ROLL_LOCK),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
