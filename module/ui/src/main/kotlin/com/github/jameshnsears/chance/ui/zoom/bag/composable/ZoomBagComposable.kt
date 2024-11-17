package com.github.jameshnsears.chance.ui.zoom.bag.composable

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.ui.dialog.bag.composable.DialogBag
import com.github.jameshnsears.chance.ui.zoom.bag.ZoomBagAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.composable.ZoomSideDescription
import com.github.jameshnsears.chance.ui.zoom.composable.ZoomSideImageSVG
import com.github.jameshnsears.chance.ui.zoom.composable.ZoomSideImageShape
import com.github.jameshnsears.chance.utility.feature.UtilityFeature
import timber.log.Timber

@Composable
fun ZoomBag(
    zoomBagAndroidViewModel: ZoomBagAndroidViewModel
) {
    val diceBagListState by zoomBagAndroidViewModel.diceBagList.collectAsState()

    val cardDice = remember { mutableStateOf(Dice()) }
    val cardSide = remember { mutableStateOf(Side()) }

    val showDialog = remember { mutableStateOf(false) }

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
                    Text(dice.title)
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
                            showDialog,
                            cardDice,
                            cardSide
                        )

                        ZoomSideDescription(zoomBagAndroidViewModel, dice, side)

                        ZoomSideImageSVG(
                            zoomBagAndroidViewModel,
                            dice,
                            side,
                            showDialog,
                            cardDice,
                            cardSide
                        )
                    }
                }
            }

            if (index < diceBagListState.size)
                HorizontalDivider(Modifier.padding(bottom = 12.dp))
        }
    }

    if (showDialog.value) {
        Timber.d("ZoomBag: dice.epoch=${cardDice.value.epoch}; side.uuid=${cardSide.value.uuid}")

        DialogBag(
            showDialog,
            zoomBagAndroidViewModel.repositoryBag,
            cardDice.value,
            cardSide.value,
        )
    }
}
