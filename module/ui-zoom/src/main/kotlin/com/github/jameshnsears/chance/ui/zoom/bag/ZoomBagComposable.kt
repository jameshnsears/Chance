package com.github.jameshnsears.chance.ui.zoom.bag

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.jameshnsears.chance.ui.zoom.DiceDescription
import com.github.jameshnsears.chance.ui.zoom.SideDescription
import com.github.jameshnsears.chance.ui.zoom.SideImageSVG
import com.github.jameshnsears.chance.ui.zoom.SideOutlineBag
import com.github.jameshnsears.chance.ui.zoom.ZoomViewModel

@Composable
fun ZoomBag(
    zoomBagViewModel: ZoomViewModel,
) {
    val listState = rememberLazyListState()

    val diceBag by zoomBagViewModel.diceBag.collectAsStateWithLifecycle()

    LazyColumn(
        state = listState,
        modifier = Modifier.padding(start = 8.dp, bottom = 35.dp),
    ) {
        var rowCountIndex = 0

        items(items = diceBag) { dice ->
            rowCountIndex += 1

            DiceDescription(dice)

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 5.dp, bottom = 5.dp),
            ) {
                var sideIndex = 0

                items(dice.sides) { side ->
                    sideIndex += 1

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        SideOutlineBag(zoomBagViewModel, dice, side)

                        SideImageSVG(zoomBagViewModel, side)

                        SideDescription(zoomBagViewModel, side)
                    }

                    if (sideIndex == dice.sides.size) {
                        Column(modifier = Modifier.padding(4.dp)) {}
                    }
                }
            }

            if (rowCountIndex != diceBag.size)
                Divider(Modifier.padding(top = 8.dp, bottom = 4.dp))
        }
    }
}
