import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.github.jameshnsears.chance.ui.zoom.R

@Composable
fun ZoomColumn() {
    // coil is NOT required to load drawable!
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    val horizontalScrollState = rememberScrollState(0)

    Column {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(horizontalScrollState)
        ) {


            Image(
                painter = painterResource(id = R.drawable.demo_d10_crocodile),
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


    /*

    1               |       6       |       5       |   ...

    ---- detail view...
                        Character

    description         Text

                        Image

    -----------------

    throw # : cumulative side #'s | dice side # | dice side # | ...

                                    UTF-8 code  |

                                    Text        |

                                    SVG image   |

    -----------------

    354px x 354px svg
     */
}

@Composable
fun ZoomColumnWithCoilExample() {
    // not usable with @Preview

    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()


    Image(
        painter = rememberAsyncImagePainter(R.drawable.d4_d8_d20_20, imageLoader),
        contentDescription = null,
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .size(50.dp)
            .clickable {
                // Handle click
            }
    )
}