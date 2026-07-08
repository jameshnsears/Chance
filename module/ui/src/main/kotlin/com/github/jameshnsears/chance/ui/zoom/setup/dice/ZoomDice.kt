package com.github.jameshnsears.chance.ui.zoom.setup.dice

import android.app.Application
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.ui.dialog.dice.DialogBag
import com.github.jameshnsears.chance.ui.dialog.dice.DialogDiceAndroidViewModel
import com.github.jameshnsears.chance.ui.dialog.dice.DialogDiceAndroidViewModelFactory
import com.github.jameshnsears.chance.ui.zoom.ZoomFaceImageSVG
import com.github.jameshnsears.chance.ui.zoom.ZoomFaceImageShape
import com.github.jameshnsears.chance.ui.zoom.ZoomSideDescription
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun ZoomBag(
    zoomDiceAndroidViewModel: ZoomDiceAndroidViewModel
) {
    val diceBagListState by zoomDiceAndroidViewModel.diceBagList
        .collectAsStateWithLifecycle(
            lifecycleOwner = LocalLifecycleOwner.current
        )

    val stateFlowZoom by zoomDiceAndroidViewModel.stateFlowZoom.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    val resizeView = stateFlowZoom.resizeViewDp

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = stateFlowZoom.firstVisibleItemIndex,
        initialFirstVisibleItemScrollOffset = stateFlowZoom.firstVisibleItemScrollOffset
    )

    LaunchedEffect(listState) {
        snapshotFlow {
            Pair(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset)
        }.collect { (index, offset) ->
            zoomDiceAndroidViewModel.saveScrollPosition(index, offset)
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
        ) {
            ZoomBagList(
                diceBagListState,
                listState,
                zoomDiceAndroidViewModel,
                resizeView
            )
        }
    }

    if (zoomDiceAndroidViewModel.showDialog.value) {
        ZoomDiceDialog(zoomDiceAndroidViewModel)
    }
}

@Composable
private fun ZoomBagList(
    diceBagListState: List<com.github.jameshnsears.chance.data.domain.core.Dice>,
    listState: androidx.compose.foundation.lazy.LazyListState,
    zoomDiceAndroidViewModel: ZoomDiceAndroidViewModel,
    resizeView: androidx.compose.ui.unit.Dp
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 88.dp)
    ) {
        itemsIndexed(
            items = diceBagListState,
            key = { _, item -> item.uuid }
        ) { index, dice ->
            ZoomDiceItem(
                index,
                dice,
                diceBagListState.size,
                zoomDiceAndroidViewModel,
                resizeView,
                listState
            )
        }
    }
}

@Composable
private fun LazyItemScope.ZoomDiceItem(
    index: Int,
    dice: com.github.jameshnsears.chance.data.domain.core.Dice,
    diceBagSize: Int,
    zoomDiceAndroidViewModel: ZoomDiceAndroidViewModel,
    resizeView: androidx.compose.ui.unit.Dp,
    listState: androidx.compose.foundation.lazy.LazyListState
) {
    Column(
        modifier = Modifier.animateItem()
    ) {
        ZoomDiceHeader(
            index,
            dice,
            diceBagSize,
            zoomDiceAndroidViewModel,
            listState
        )

        ZoomDiceSidesRow(
            dice,
            zoomDiceAndroidViewModel,
            resizeView
        )

        if (index < diceBagSize - 1)
            HorizontalDivider(Modifier.padding(top = 12.dp, bottom = 12.dp))
    }
}

@Composable
private fun ZoomDiceHeader(
    index: Int,
    dice: com.github.jameshnsears.chance.data.domain.core.Dice,
    diceBagSize: Int,
    zoomDiceAndroidViewModel: ZoomDiceAndroidViewModel,
    listState: androidx.compose.foundation.lazy.LazyListState
) {
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (UtilityFeature.isEnabled(UtilityFeature.Flag.UI_SHOW_UUID)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    dice.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    dice.uuid,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Text(
                dice.title,
                Modifier
                    .weight(1f)
                    .testTag("${ZoomDiceTestTag.ZOOM_DICE_TITLE}-${dice.title}"),
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (index < diceBagSize - 1) {
            IconButton(
                onClick = {
                    zoomDiceAndroidViewModel.moveDown(dice)
                    coroutineScope.launch {
                        listState.animateScrollToItem(index + 1)
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .testTag(ZoomDiceTestTag.ZOOM_DICE_MOVE_DOWN)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = ""
                )
            }
        }

        if (index > 0) {
            IconButton(
                onClick = {
                    zoomDiceAndroidViewModel.moveUp(dice)
                    coroutineScope.launch {
                        listState.animateScrollToItem(index - 1)
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .testTag(ZoomDiceTestTag.ZOOM_DICE_MOVE_UP)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
private fun ZoomDiceSidesRow(
    dice: com.github.jameshnsears.chance.data.domain.core.Dice,
    zoomDiceAndroidViewModel: ZoomDiceAndroidViewModel,
    resizeView: androidx.compose.ui.unit.Dp
) {
    val stateFlowZoom by zoomDiceAndroidViewModel.stateFlowZoom.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    val scrollPosition = stateFlowZoom.horizontalScrollPositions[dice.uuid] ?: (0 to 0)

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = scrollPosition.first,
        initialFirstVisibleItemScrollOffset = scrollPosition.second
    )

    LaunchedEffect(listState) {
        snapshotFlow {
            Pair(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset)
        }.collect { (index, offset) ->
            zoomDiceAndroidViewModel.saveHorizontalScrollPosition(dice.uuid, index, offset)
        }
    }

    LazyRow(
        state = listState,
        contentPadding = PaddingValues(horizontal = 0.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top,
        modifier = Modifier.padding(bottom = 6.dp),
    ) {
        items(
            dice.sides,
            key = {
                it.uuid
            }
        ) { side ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ZoomFaceImageShape(
                    zoomDiceAndroidViewModel,
                    dice,
                    side,
                    zoomDiceAndroidViewModel.showDialog,
                    zoomDiceAndroidViewModel.cardDice,
                    zoomDiceAndroidViewModel.cardSide,
                    resizeView,
                )

                ZoomFaceImageSVG(
                    zoomDiceAndroidViewModel,
                    dice,
                    side,
                    zoomDiceAndroidViewModel.showDialog,
                    zoomDiceAndroidViewModel.cardDice,
                    zoomDiceAndroidViewModel.cardSide,
                    resizeView,
                )

                ZoomSideDescription(zoomDiceAndroidViewModel, dice, side)
            }
        }
    }
}

@Composable
private fun ZoomDiceDialog(zoomDiceAndroidViewModel: ZoomDiceAndroidViewModel) {
    Timber.d("ZoomBag: dice.epoch=${zoomDiceAndroidViewModel.cardDice.value.epoch}; side.uuid=${zoomDiceAndroidViewModel.cardSide.value.uuid}")

    val context = LocalContext.current
    val application = context.applicationContext as? Application ?: object : Application() {
        override fun getSharedPreferences(name: String?, mode: Int): SharedPreferences {
            return context.getSharedPreferences(name, mode)
        }
    }

    val dialogDiceAndroidViewModel: DialogDiceAndroidViewModel = viewModel(
        key = "${zoomDiceAndroidViewModel.cardDice.value.uuid}-${zoomDiceAndroidViewModel.cardSide.value.uuid}",
        factory = DialogDiceAndroidViewModelFactory(
            application,
            zoomDiceAndroidViewModel.repositoryBag,
            zoomDiceAndroidViewModel.cardDice.value,
            zoomDiceAndroidViewModel.cardSide.value
        )
    )

    DialogBag(
        zoomDiceAndroidViewModel.showDialog,
        dialogDiceAndroidViewModel
    )
}
