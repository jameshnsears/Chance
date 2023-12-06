package com.github.jameshnsears.chance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.github.jameshnsears.chance.data.repository.dice.DiceRepositoryMock
import com.github.jameshnsears.chance.data.repository.dice.sample.DiceSampleData
import com.github.jameshnsears.chance.ui.dialog.dice.DialogDice
import com.github.jameshnsears.chance.ui.dialog.dice.DialogDiceViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.utils.logging.LoggingLineNumberTree
import timber.log.Timber


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Timber.treeCount == 0) {
            Timber.plant(LoggingLineNumberTree())
        }

        val diceRepository = DiceRepositoryMock

        setContent {
            ChanceTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val showDialog = remember { mutableStateOf(false) }

                    val diceIndex = remember { mutableIntStateOf(0) }

                    /////////////////////

                    Column {
                        Button(
                            onClick = {
                                showDialog.value = true
                                diceIndex.intValue = 0
                                diceRepository.store(DiceSampleData.singleDice)
                            }
                        ) {
                            Text("Single Dice")
                        }

                        Button(
                            onClick = {
                                showDialog.value = true
                                diceIndex.intValue = 1
                                diceRepository.store(DiceSampleData.twoDice)
                            }
                        ) {
                            Text("Two Dice")
                        }

                        HorizontalScrollingColumn()
                    }

                    if (showDialog.value) {
                        DialogDice(
                            showDialog,
                            DialogDiceViewModel(diceRepository, diceIndex.intValue)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HorizontalScrollingColumn() {
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
        repeat(10) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .size(50.dp)
                    .background(Color.Blue)
                    .clickable {
                        // Handle click
                    },
                text = "Item ${it + 1}",
                color = Color.White
            )
        }
    }


//                    Image(
//                        painter = rememberAsyncImagePainter(R.drawable.d12, imageLoader),
//                        contentDescription = null,
//                        modifier = Modifier.fillMaxSize()
//                    )
//
//                    Row {
//                        Box(modifier = Modifier.fillMaxSize()) {
//                            Image(
//                                painter = rememberAsyncImagePainter(R.drawable.d2, imageLoader),
//                                contentDescription = null,
//                                modifier = Modifier.fillMaxSize()
//                            )
//                            Image(
//                                painter = rememberAsyncImagePainter(
//                                    R.drawable.d4_d8_d20,
//                                    imageLoader
//                                ),
//                                contentDescription = null,
//                                modifier = Modifier.fillMaxSize()
//                            )
//                            Image(
//                                painter = rememberAsyncImagePainter(R.drawable.d6, imageLoader),
//                                contentDescription = null,
//                                modifier = Modifier.fillMaxSize()
//                            )
//                            Image(
//                                painter = rememberAsyncImagePainter(R.drawable.d10, imageLoader),
//                                contentDescription = null,
//                                modifier = Modifier.fillMaxSize()
//                            )
//                            Image(
//                                painter = rememberAsyncImagePainter(R.drawable.d12, imageLoader),
//                                contentDescription = null,
//                                modifier = Modifier.fillMaxSize()
//                            )
//                        }
//                    }
//
//                    Row {
//                        Image(
//                            painter = rememberAsyncImagePainter(
//                                R.drawable.mrbenn_crocodile,
//                                imageLoader
//                            ),
//                            contentDescription = null,
//                            modifier = Modifier.fillMaxSize()
//                        )
//                    }

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
