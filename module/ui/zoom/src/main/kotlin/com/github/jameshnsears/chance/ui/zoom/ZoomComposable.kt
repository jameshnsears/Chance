import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.github.jameshnsears.chance.ui.zoom.R

@Composable
fun ZoomColumn() {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    val horizontalScrollState = rememberScrollState(0)

    Row(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(horizontalScrollState)
    ) {
        Image(
            painter = rememberAsyncImagePainter(R.drawable.d4_d8_d20__20, imageLoader),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .size(50.dp)
                .clickable {
                    // Handle click
                }
        )

        Image(
            painter = rememberAsyncImagePainter(R.drawable.d4_d8_d20__19, imageLoader),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .size(50.dp)
                .clickable {
                    // Handle click
                }
        )

        Image(
            painter = rememberAsyncImagePainter(
                R.drawable.mrbenn_crocodile,
                imageLoader
            ),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .size(50.dp)
                .clickable {
                    // Handle click
                }
        )

        Image(
            painter = rememberAsyncImagePainter(R.drawable.d4_d8_d20__19, imageLoader),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .size(50.dp)
                .clickable {
                    // Handle click
                }
        )

        Image(
            painter = rememberAsyncImagePainter(R.drawable.d12, imageLoader),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .size(50.dp)
                .clickable {
                    // Handle click
                }
        )

        Image(
            painter = rememberAsyncImagePainter(R.drawable.d4_d8_d20__19, imageLoader),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .size(50.dp)
                .clickable {
                    // Handle click
                }
        )

        Image(
            painter = rememberAsyncImagePainter(R.drawable.d10, imageLoader),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .size(50.dp)
                .clickable {
                    // Handle click
                }
        )

        Image(
            painter = rememberAsyncImagePainter(R.drawable.d4_d8_d20__19, imageLoader),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .size(50.dp)
                .clickable {
                    // Handle click
                }
        )

        Image(
            painter = rememberAsyncImagePainter(R.drawable.d6, imageLoader),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .size(50.dp)
                .clickable {
                    // Handle click
                }
        )
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
