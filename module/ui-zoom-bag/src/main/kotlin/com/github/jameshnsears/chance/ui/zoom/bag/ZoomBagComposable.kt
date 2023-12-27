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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Side
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBag
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagViewModel

@Composable
fun ZoomColumn(viewModel: ZoomBagViewModel) {
    val listState = rememberLazyListState()

    val bagDemo = remember { viewModel.bagRepository.fetch() }

    LazyColumn(
        state = listState
    ) {
        items(items = bagDemo) { dice ->

            DiceDescription(viewModel, dice)

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 5.dp, bottom = 5.dp)
            ) {
                items(dice.sides) { side ->

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Side(viewModel, dice, side)

                        SideImage(viewModel, side)

                        SideText(viewModel, side)
                    }
                }
            }

            Divider()
        }
    }
}

@Composable
fun DiceDescription(viewModel: ZoomBagViewModel, dice: Dice) {
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
fun Side(viewModel: ZoomBagViewModel, dice: Dice, side: Side) {
    Box {
        SideShape(viewModel, dice)

        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = viewModel.scaleValuePaddingTop(dice)),
            fontSize = viewModel.scaleTextFontSize(dice),
            text = "${side.number}",
            color = Color.White
        )
    }
}

@Composable
fun SideShape(viewModel: ZoomBagViewModel, dice: Dice) {
    val showDialog = remember { mutableStateOf(false) }

    Image(
        painter = painterResource(viewModel.sideAppearance(dice)),
        contentDescription = null,
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 5.dp)
            .size(viewModel.scale())
            .clickable {
                showDialog.value = true
            }
    )

    if (showDialog.value) {
        DialogBag(
            showDialog,
            DialogBagViewModel(
                viewModel.bagRepository,
                1
            )
        )
    }
}

@Composable
fun SideText(viewModel: ZoomBagViewModel, side: Side) {
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
fun SideImage(viewModel: ZoomBagViewModel, side: Side) {
    if (viewModel.imageDrawableIdAvailable(side)) {
        Image(
            painter = painterResource(id = side.imageDrawableId),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .size(viewModel.scale())
        )
    }
}
