package com.github.jameshnsears.chance.ui.zoom.bag.compose

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.ui.zoom.ZoomAndroidViewModel
import com.github.jameshnsears.chance.ui.zoom.compose.DiceTitle
import com.github.jameshnsears.chance.ui.zoom.compose.SideDescriptionBag
import com.github.jameshnsears.chance.ui.zoom.compose.SideImageNumberDialog
import com.github.jameshnsears.chance.ui.zoom.compose.SideImageSVG
import timber.log.Timber

@Composable
fun ZoomBag(
    zoomAndroidViewModel: ZoomAndroidViewModel
) {
    val listState = rememberLazyListState()

    val diceBag by zoomAndroidViewModel.diceBag.collectAsStateWithLifecycle()

    val resize = zoomAndroidViewModel.resizeView.collectAsStateWithLifecycle()

    LazyColumn(
        state = listState,
        modifier = Modifier.padding(start = 8.dp, bottom = 110.dp, end = 8.dp),
    ) {
        Timber.d("resize.Bag=${resize.value}")

        itemsIndexed(items = diceBag) { index, dice ->
            Row(
                Modifier.padding(top = 8.dp)
            ) {
                DiceTitle(dice)
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 5.dp, bottom = 5.dp),
            ) {
                items(
                    dice.sides,
                    key = {
                        it.uuid
                    }
                ) { side ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SideImageNumberDialog(zoomAndroidViewModel, dice, side)

                        SideDescriptionBag(zoomAndroidViewModel, dice, side)

                        SideImageSVG(zoomAndroidViewModel, side)
                    }
                }
            }

            if (index < diceBag.size)
                HorizontalDivider(Modifier.padding(top = 8.dp, bottom = 4.dp))
        }
    }
}
