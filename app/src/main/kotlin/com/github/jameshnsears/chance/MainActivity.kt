package com.github.jameshnsears.chance

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
import com.github.jameshnsears.chance.data.bag.demo.BagDemo
import com.github.jameshnsears.chance.data.bag.repository.BagRepositoryMock
import com.github.jameshnsears.chance.data.bag.sample.BagSampleData
import com.github.jameshnsears.chance.ui.dialog.dice.DialogDice
import com.github.jameshnsears.chance.ui.dialog.dice.DialogDiceViewModel
import com.github.jameshnsears.chance.ui.theme.ChanceTheme
import com.github.jameshnsears.chance.ui.zoom.ZoomColumn
import com.github.jameshnsears.chance.ui.zoom.ZoomViewModel
import com.github.jameshnsears.chance.utils.logging.LoggingLineNumberTree
import timber.log.Timber


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Timber.treeCount == 0) {
            Timber.plant(LoggingLineNumberTree())
        }

        val bagRepository = BagRepositoryMock
        bagRepository.store(BagDemo.dice)

        setContent {
            ChanceTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val showDialog = remember { mutableStateOf(false) }

                    val diceIndex = remember { mutableIntStateOf(0) }

                    Column {
                        Button(
                            onClick = {
                                showDialog.value = true
                                diceIndex.intValue = 0
                                bagRepository.store(BagSampleData.oneDice)
                            }
                        ) {
                            Text("Single Dice")
                        }

                        Button(
                            onClick = {
                                showDialog.value = true
                                diceIndex.intValue = 1
                                bagRepository.store(BagSampleData.twoDice)
                            }
                        ) {
                            Text("Two Dice")
                        }

                        ZoomColumn(ZoomViewModel(bagRepository))
                    }

                    if (showDialog.value) {
                        DialogDice(
                            showDialog,
                            DialogDiceViewModel(bagRepository, diceIndex.intValue)
                        )
                    }
                }
            }
        }
    }
}
