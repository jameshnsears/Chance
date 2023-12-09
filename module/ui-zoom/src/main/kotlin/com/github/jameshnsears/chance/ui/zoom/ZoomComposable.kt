package com.github.jameshnsears.chance.ui.zoom

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.github.jameshnsears.chance.data.R

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
                    } else {
//                        SideImage(dice)
                    }
                }
            }

            Divider()
        }
    }
}

//@Composable
//fun SideImage(dice: Dice) {
//    when (dice.sides.size) {
//        2 -> {
//
//        }
//    }
//
//    Image(
//        painter = painterResource(id = side.imageDrawableId),
//        contentDescription = null,
//        modifier = Modifier
//            .padding(horizontal = 5.dp, vertical = 5.dp)
//            .size(viewModel.zoomSize())
//            .clickable {
//                // Handle click
//            }
//    )
//}

/*
roll # : cumulative side #'s | dice side # | dice side # | ...

                                UTF-8 code  |

                                Text        |

                                SVG image   |

-----------------
 */


@Composable
fun ZoomColumnWithCoilExample() {
    // not usable with @Preview

    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    Image(
        painter = rememberAsyncImagePainter(R.drawable.d4_d8_d20, imageLoader),
        contentDescription = null,
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .size(50.dp)
            .clickable {
                // Handle click
            }
    )
}