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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
            Text(dice.description)

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {

                items(dice.sides) { side ->
                    Text("${side.sideIndex}")

                    Image(
                        painter = painterResource(id = R.drawable.demo_bag_crocodile),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .size(50.dp)
                            .clickable {
                                // Handle click
                            }
                    )
                }
            }
        }
    }
}

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