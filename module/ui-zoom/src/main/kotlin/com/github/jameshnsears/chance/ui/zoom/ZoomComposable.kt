package com.github.jameshnsears.chance.ui.zoom

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.jameshnsears.chance.data.R
import com.github.jameshnsears.chance.data.domain.Dice
import com.github.jameshnsears.chance.data.domain.Side

@Composable
fun ZoomColumn(viewModel: ZoomViewModel) {
    val listState = rememberLazyListState()

    val bagDemo = remember { viewModel.bagDemo() }

    LazyColumn(
        state = listState
    ) {
        items(items = bagDemo) { dice ->

            TextDiceDescription(viewModel, dice)

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 5.dp, bottom = 5.dp)
            ) {
                items(dice.sides) { side ->

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        ImageSide(viewModel, dice, side)

                        SideImage(viewModel, side)

                        SideUnicodeCharacter(viewModel, dice, side)

                        SideText(viewModel, side)
                    }
                }
            }

            Divider()
        }
    }
}

@Composable
fun SideUnicodeCharacter(viewModel: ZoomViewModel, dice: Dice, side: Side) {
    if (side.unicodeCharacter != 0) {
        Text(
            fontSize = viewModel.scaleTextFontSize(dice),
            text = "" + side.unicodeCharacter.toChar()
        )
    }
}

@Composable
fun TextDiceDescription(viewModel: ZoomViewModel, dice: Dice) {
    if (dice.description != "") {
        Text(
            modifier = Modifier.padding(start = 5.dp, top = 5.dp),
            text = dice.description
        )
    } else if (dice.descriptionStringsId != 0) {
        Text(
            modifier = Modifier.padding(start = 5.dp, top = 5.dp),
            text = stringResource(id = dice.descriptionStringsId)
        )
    }
}

@Composable
fun ImageSide(viewModel: ZoomViewModel, dice: Dice, side: Side) {
    Box {
        val diceSideResourceId = when (dice.sides.size) {
            2 -> R.drawable.d2
            6 -> R.drawable.d6
            10 -> R.drawable.d10
            12 -> R.drawable.d12
            else -> R.drawable.d4_d8_d20
        }
        Image(
            painter = painterResource(id = diceSideResourceId),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .size(viewModel.scale())
                .clickable {
                    // Handle click
                }
        )

        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = viewModel.scaleTextPaddingTop(dice)),
            fontSize = viewModel.scaleTextFontSize(dice),
            text = "${side.sideIndex}"
        )
    }
}

@Composable
fun SideText(viewModel: ZoomViewModel, side: Side) {
    if (side.text != "") {
    Text(
        text = side.text
    )
}
    else if (side.textStringsId != 0) {
        Text(
            text = stringResource(id = side.textStringsId)
        )
    }
}

@Composable
fun SideImage(viewModel: ZoomViewModel, side: Side) {
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
