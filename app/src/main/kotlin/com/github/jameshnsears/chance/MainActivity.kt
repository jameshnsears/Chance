package com.github.jameshnsears.chance

import ZoomColumn
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

                        ZoomColumn()
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
