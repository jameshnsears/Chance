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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                items(dice.sides) { side ->

                    Column {
                        ImageSide(viewModel, dice, side)

                        if (viewModel.imageDrawableAvailable(side)) {
                            ImageSideDrawable(viewModel, side)
                        }
                    }
                }
            }

            Divider()
        }
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
                .size(viewModel.zoomSize())
                .clickable {
                    // Handle click
                }
        )

        val sideIndexTopPadding = when (dice.sides.size) {
            10 -> 25.dp
            4, 8, 20 -> 40.dp
            else -> 0.dp
        }
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = sideIndexTopPadding),
            fontSize = 36.sp,
            text = "${side.sideIndex}"
        )
    }
}

@Composable
fun ImageSideDrawable(viewModel: ZoomViewModel, side: Side) {
    Image(
        painter = painterResource(id = side.imageDrawableId),
        contentDescription = null,
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 5.dp)
            .size(viewModel.zoomSize())
    )
}

/*
roll # : cumulative side #'s | dice side # | dice side # | ...

                                UTF-8 code  |

                                Text        |

                                SVG image   |

-----------------
 */
