package com.github.jameshnsears.chance.ui.zoom.bag

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Side
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBag

@Composable
fun ZoomBag(
    zoomBagViewModel: ZoomBagViewModel,
) {
    val listState = rememberLazyListState()

    val context = LocalContext.current

    val bagRepositoryBag = remember { zoomBagViewModel.bagRepository.fetch() }

    LazyColumn(
        state = listState
    ) {
        items(items = bagRepositoryBag) { dice ->

            DiceDescription(zoomBagViewModel, dice)

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 5.dp, bottom = 5.dp)
            ) {
                items(dice.sides) { side ->
                    // TODO when tap on a column then raise dialog!
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Side(zoomBagViewModel, dice, side)

                        SideImage(zoomBagViewModel, side)

                        SideText(zoomBagViewModel, side)
                    }
                }
            }

            Divider()
        }
    }
}

@Composable
fun DiceDescription(zoomBagViewModel: ZoomBagViewModel, dice: Dice) {
    val description = if (dice.title != "")
        dice.title
    else
        stringResource(id = dice.titleStringsId)

    Text(
        modifier = Modifier.padding(start = 5.dp, top = 5.dp),
        text = description,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun Side(
    zoomBagViewModel: ZoomBagViewModel,
    dice: Dice,
    side: Side
) {
    Box {
        SideShape(zoomBagViewModel, dice, side)

        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = zoomBagViewModel.scaleValuePaddingTop(dice)),
            fontSize = zoomBagViewModel.scaleTextFontSize(dice),
            text = "${side.number}",
            color = Color.White
        )
    }
}

@Composable
fun SideShape(
    zoomBagViewModel: ZoomBagViewModel,
    dice: Dice,
    side: Side
) {
    val showDialog = remember { mutableStateOf(false) }

    Image(
        painter = painterResource(zoomBagViewModel.sideAppearance(dice)),
        contentDescription = null,
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 5.dp)
            .size(zoomBagViewModel.scale())
            .clickable {
                showDialog.value = true
            }
    )

    if (showDialog.value) {
        DialogBag(
            showDialog,
            zoomBagViewModel.bagRepository,
            dice,
            side
        )
    }
}

@Composable
fun SideText(zoomBagViewModel: ZoomBagViewModel, side: Side) {
    if (side.description != "") {
        Text(
            text = side.description
        )
    } else if (side.descriptionStringsId != 0) {
        Text(
            text = stringResource(id = side.descriptionStringsId)
        )
    }
}

@Composable
fun SideImage(zoomBagViewModel: ZoomBagViewModel, side: Side) {
    if (zoomBagViewModel.imageDrawableIdAvailable(side)) {
        Image(
            painter = painterResource(id = side.imageDrawableId),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .size(zoomBagViewModel.scale())
        )
    }
}
