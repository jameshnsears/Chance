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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jameshnsears.chance.ui.zoom.DiceDescription
import com.github.jameshnsears.chance.ui.zoom.SideDescription
import com.github.jameshnsears.chance.ui.zoom.SideImageSVG
import com.github.jameshnsears.chance.ui.zoom.SideOutlineBag

@Composable
fun ZoomBag(
    zoomBagViewModel: ZoomBagViewModel,
) {
    val listState = rememberLazyListState()

    val bagRepositoryBag = remember { zoomBagViewModel.bagRepository.fetch() }

    LazyColumn(
        state = listState,
        modifier = Modifier.padding(start = 8.dp)
    ) {
        items(items = bagRepositoryBag) { dice ->
            DiceDescription(dice)

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 5.dp, bottom = 5.dp)
            ) {
                items(dice.sides) { side ->
                    // TODO when tap on a column then raise dialog!
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        SideOutlineBag(zoomBagViewModel, dice, side)

                        SideImageSVG(zoomBagViewModel, side)

                        SideDescription(zoomBagViewModel, side)
                    }
                }
            }

            Divider(Modifier.padding(top = 8.dp, bottom = 4.dp))
        }
    }
}