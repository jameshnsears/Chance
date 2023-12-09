package com.github.jameshnsears.chance.ui.zoom

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import com.github.jameshnsears.chance.data.R
import com.github.jameshnsears.chance.data.domain.Dice

@Composable
fun ZoomColumn(viewModel: ZoomViewModel) {
    val listState = rememberLazyListState()

    val bagDemo = remember { viewModel.bagDemo() }

    LazyColumn(
        state = listState
    ) {
        items(items = bagDemo) { dice ->

            var showDiceIndex = true

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                items(dice.sides) { side ->

                    if (showDiceIndex) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 20.dp)
                                .clickable {
                                    // Handle click
                                },
                            text = "" + dice.diceIndex
                        )
                        showDiceIndex = false
                    }

                    Column {
                        ImageSide(viewModel, dice)

                        if (viewModel.imageDrawableAvailable(side)) {
                            Image(
                                painter = painterResource(id = side.imageDrawableId),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(horizontal = 5.dp, vertical = 5.dp)
                                    .size(viewModel.zoomSize())
                                    .clickable {
                                        // Handle click
                                    }
                            )
                        }
                    }
                }
            }

            Divider()
        }
    }
}

@Composable
fun ImageSide(viewModel: ZoomViewModel, dice: Dice) {
    val diceSideResourceId = when (dice.sides.size) {
        2 -> R.drawable.d2
        4, 8, 20 -> R.drawable.d4_d8_d20
        6 -> R.drawable.d6
        10 -> R.drawable.d10
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
}

/*
roll # : cumulative side #'s | dice side # | dice side # | ...

                                UTF-8 code  |

                                Text        |

                                SVG image   |

-----------------
 */
