package com.github.jameshnsears.chance.ui.zoom.bag

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.jameshnsears.chance.common.utility.feature.UtilityFeature
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBag
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModelFactory
import com.github.jameshnsears.chance.ui.zoom.ZoomSideDescription
import com.github.jameshnsears.chance.ui.zoom.ZoomSideImageSVG
import com.github.jameshnsears.chance.ui.zoom.ZoomSideImageShape
import timber.log.Timber

@Composable
fun ZoomBag(
    zoomBagAndroidViewModel: ZoomBagAndroidViewModel
) {
    val diceBagListState by zoomBagAndroidViewModel.diceBagList.collectAsState()

    val stateFlowZoom by zoomBagAndroidViewModel.stateFlowZoom.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    val resizeView = stateFlowZoom.resizeViewDp

    val listState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.padding(top = 8.dp, start = 0.dp, bottom = 110.dp, end = 8.dp),
        state = listState,
    ) {
        itemsIndexed(
            items = diceBagListState,
            key = { index, item -> "${item.uuid}_${index}" }
        ) { index, dice ->
            Row(
                modifier = Modifier.padding(start = 8.dp),
            ) {
                if (UtilityFeature.isEnabled(UtilityFeature.Flag.UI_SHOW_EPOCH_UUID)) {
                    Column {
                        Text(dice.title)
                        Text("${dice.epoch}")
                        Text(dice.uuid)
                    }
                } else {
                    Text(
                        dice.title,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(bottom = 12.dp),
            ) {
                items(
                    dice.sides,
                    key = {
                        it.uuid
                    }
                ) { side ->
                    Column(
                        Modifier.padding(start = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        ZoomSideImageShape(
                            zoomBagAndroidViewModel,
                            dice,
                            side,
                            zoomBagAndroidViewModel.showDialog,
                            zoomBagAndroidViewModel.cardDice,
                            zoomBagAndroidViewModel.cardSide,
                            resizeView,
                        )

                        ZoomSideImageSVG(
                            zoomBagAndroidViewModel,
                            dice,
                            side,
                            zoomBagAndroidViewModel.showDialog,
                            zoomBagAndroidViewModel.cardDice,
                            zoomBagAndroidViewModel.cardSide,
                            resizeView,
                        )

                        ZoomSideDescription(zoomBagAndroidViewModel, dice, side)
                    }
                }
            }

            if (index < diceBagListState.size - 1)
                HorizontalDivider(Modifier.padding(bottom = 12.dp))
        }
    }

    if (zoomBagAndroidViewModel.showDialog.value) {
        Timber.d("ZoomBag: dice.epoch=${zoomBagAndroidViewModel.cardDice.value.epoch}; side.uuid=${zoomBagAndroidViewModel.cardSide.value.uuid}")

        val dialogBagAndroidViewModel: DialogBagAndroidViewModel = viewModel(
            key = "${zoomBagAndroidViewModel.cardDice.value.epoch}_${zoomBagAndroidViewModel.cardSide.value.uuid}",
            factory = DialogBagAndroidViewModelFactory(
                LocalContext.current.applicationContext as Application,
                zoomBagAndroidViewModel.repositoryBag,
                zoomBagAndroidViewModel.cardDice.value,
                zoomBagAndroidViewModel.cardSide.value
            )
        )

        DialogBag(
            zoomBagAndroidViewModel.showDialog,
            dialogBagAndroidViewModel
        )
    }
}
